package cn.wyceden.management.websocket;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;

import cn.wyceden.logger.EdenLogger;
import cn.wyceden.management.websocket.client.EdenWebSocketClient;
import cn.wyceden.management.websocket.packet.EdenPacket;
import cn.wyceden.utils.HttpUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.session.Session;

public class WebSocketManager {

    private static final int MAX_RETRY = 3;
    private static final long RECONNECT_INTERVAL_MS = 10_000L;
    private static final String SESSION_JOIN_URL = "https://sessionserver.mojang.com/session/minecraft/join";
    private static final String SERVER_ID = "cbd2c3f65d7ba5cceba0cc9647ff9a85c371f4";

    private final MinecraftClient client = MinecraftClient.getInstance();

    private GameProfile gameProfile;
    private EdenWebSocketClient webSocket;
    private int retryCount = 0;

    public WebSocketManager() {

        Thread webSocketThread = new Thread("Websocket Thread") {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {

                    try {
                        Thread.sleep(RECONNECT_INTERVAL_MS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }

                    try {
                        tick();
                    } catch (Exception e) {
                        EdenLogger.error("API", "Unexpected error in the WebSocket manager", e);
                    }
                }
            }
        };

        webSocketThread.setDaemon(true);
        webSocketThread.start();
    }

    private void tick() {

        GameProfile currentProfile = client.getGameProfile();

        if (currentProfile == null) {
            // Not authenticated yet (or logged out); there is nothing to connect with.
            return;
        }

        boolean profileChanged = gameProfile != null && !gameProfile.equals(currentProfile);

        if (profileChanged) {
            retryCount = 0;
        }

        if (webSocket != null && webSocket.isOpen() && !profileChanged) {
            // Already connected to the correct profile.
            retryCount = 0;
            return;
        }

        if (retryCount >= MAX_RETRY) {
            return;
        }

        gameProfile = currentProfile;

        if (webSocket != null) {
            webSocket.close();
            webSocket = null;
        }

        Session session = client.getSession();
        UUID uuid = session.getUuidOrNull();

        JsonObject joinRequest = new JsonObject();
        joinRequest.addProperty("accessToken", session.getAccessToken());
        joinRequest.addProperty("selectedProfile", uuid == null ? "" : uuid.toString().replace("-", ""));
        joinRequest.addProperty("serverId", SERVER_ID);
        HttpUtils.postJson(SESSION_JOIN_URL, joinRequest);

        Map<String, String> headers = new HashMap<>();
        headers.put("name", currentProfile.getName());
        headers.put("uuid", currentProfile.getId().toString().replace("-", ""));

        try {
            webSocket = new EdenWebSocketClient(headers, () -> retryCount++);
            webSocket.connect();
        } catch (URISyntaxException e) {
            EdenLogger.error("API", "Invalid WebSocket URI", e);
        }
    }

    public void send(EdenPacket packet) {
        if (webSocket != null && webSocket.isOpen()) {
            webSocket.send(packet.toJson().toString());
        }
    }
}
