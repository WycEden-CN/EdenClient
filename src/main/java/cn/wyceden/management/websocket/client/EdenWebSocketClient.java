package cn.wyceden.management.websocket.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import cn.wyceden.logger.EdenLogger;
import cn.wyceden.management.websocket.handler.WebSocketHandler;
import cn.wyceden.management.websocket.handler.impl.HypixelStatsHandler;
import cn.wyceden.management.websocket.handler.impl.EdenUserHandler;
import cn.wyceden.utils.JsonUtils;

public class EdenWebSocketClient extends WebSocketClient {

	private static final String DEFAULT_URI = "ws://localhost:8080/websocket";

	private final Map<String, WebSocketHandler> handlers = new HashMap<>();
	private final Gson gson = new Gson();
	private final Runnable closeTask;

	public EdenWebSocketClient(Map<String, String> headers, Runnable closeTask) throws URISyntaxException {
		super(new URI(System.getProperty("eden.websocket.url", DEFAULT_URI)), headers);
		this.closeTask = closeTask;
		initializeHandlers();
	}

    private void initializeHandlers() {
    	register("eden-hypixel-stats", new HypixelStatsHandler());
    	register("eden-user", new EdenUserHandler());
    }
    
	@Override
	public void onOpen(ServerHandshake handshakedata) {
		EdenLogger.info("API", "WebSocket connection opened");
	}

	@Override
	public void onMessage(String message) {

		JsonObject jsonObject = gson.fromJson(message, JsonObject.class);
		
		String type = JsonUtils.getStringProperty(jsonObject, "type", "");

		WebSocketHandler handler = handlers.get(type);

		if (handler != null) {
			handler.handle(jsonObject);
		} else {
			EdenLogger.warn("API", "No handler found for message type: " + type);
		}
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		EdenLogger.info("API", "WebSocket connection closed: " + reason);
		closeTask.run();
	}

	@Override
	public void onError(Exception ex) {
		EdenLogger.error("API", "WebSocket error occurred", ex);
	}

	private void register(String type, WebSocketHandler handler) {
		handlers.put(type, handler);
	}
}