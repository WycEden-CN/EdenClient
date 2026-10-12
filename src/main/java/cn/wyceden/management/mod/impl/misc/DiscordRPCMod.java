package cn.wyceden.management.mod.impl.misc;

import java.time.OffsetDateTime;

import com.google.gson.JsonObject;

import cn.wyceden.Eden;
import cn.wyceden.libraries.discordipc.IPCClient;
import cn.wyceden.libraries.discordipc.IPCListener;
import cn.wyceden.libraries.discordipc.entities.ActivityType;
import cn.wyceden.libraries.discordipc.entities.Packet;
import cn.wyceden.libraries.discordipc.entities.RichPresence;
import cn.wyceden.libraries.discordipc.entities.User;
import cn.wyceden.libraries.discordipc.entities.pipe.PipeStatus;
import cn.wyceden.libraries.discordipc.exceptions.NoDiscordClientException;
import cn.wyceden.logger.EdenLogger;
import cn.wyceden.management.mod.Mod;
import cn.wyceden.management.mod.ModCategory;
import cn.wyceden.skia.font.Icon;

public class DiscordRPCMod extends Mod {

	/**
	 * The Discord Application ID used to publish Rich Presence.
	 *
	 * <p>The application id decides the displayed application name, the uploaded art assets and
	 * therefore the {@code "icon"} large-image key below. The current value still points to the
	 * original author's Discord application, so <b>replace it with your own</b> (and upload your
	 * own {@code icon} asset) from the Discord Developer Portal.</p>
	 *
	 * <p>It can be overridden at runtime without recompiling via
	 * {@code -Deden.discord.applicationId=<your-id>}.</p>
	 */
	private static final long DISCORD_APPLICATION_ID = Long.getLong("eden.discord.applicationId",
			1059341815205068901L);

	private IPCClient client;

	public DiscordRPCMod() {
		super("mod.discordrpc.name", "mod.discordrpc.description", Icon.VERIFIED, ModCategory.MISC);
	}

	@Override
	public void onEnable() {
		super.onEnable();

		client = new IPCClient(DISCORD_APPLICATION_ID);
		client.setListener(new IPCListener() {
			@Override
			public void onReady(IPCClient client) {

				RichPresence.Builder builder = new RichPresence.Builder();

				builder.setState("Playing Eden Client v" + Eden.getInstance().getVersion())
						.setStartTimestamp(OffsetDateTime.now().toEpochSecond()).setLargeImage("icon")
						.setActivityType(ActivityType.Playing);

				client.sendRichPresence(builder.build());
			}

			@Override
			public void onPacketSent(IPCClient client, Packet packet) {
			}

			@Override
			public void onPacketReceived(IPCClient client, Packet packet) {
			}

			@Override
			public void onActivityJoin(IPCClient client, String secret) {
			}

			@Override
			public void onActivitySpectate(IPCClient client, String secret) {
			}

			@Override
			public void onActivityJoinRequest(IPCClient client, String secret, User user) {
			}

			@Override
			public void onClose(IPCClient client, JsonObject json) {
			}

			@Override
			public void onDisconnect(IPCClient client, Throwable t) {
			}
		});

		try {
			client.connect();
		} catch (NoDiscordClientException e) {
			EdenLogger.warn("DiscordRPC", "No running Discord client was found");
		} catch (Exception e) {
			EdenLogger.error("DiscordRPC", "Failed to connect to Discord", e);
		}
	}

	@Override
	public void onDisable() {
		super.onDisable();

		if (client != null && client.getStatus() == PipeStatus.CONNECTED) {
			client.close();
		}

		client = null;
	}
}
