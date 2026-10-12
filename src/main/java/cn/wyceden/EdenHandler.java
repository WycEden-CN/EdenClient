package cn.wyceden;

import java.util.List;

import cn.wyceden.event.EventBus;
import cn.wyceden.event.client.ClientTickEvent;
import cn.wyceden.event.client.ServerJoinEvent;
import cn.wyceden.event.server.impl.GameJoinEvent;
import cn.wyceden.management.profile.Profile;

public class EdenHandler {

	public final EventBus.EventListener<ClientTickEvent> onClientTick = event -> {
		Eden.getInstance().getColorManager().onTick();
		Eden.getInstance().getHypixelManager().update();
		Eden.getInstance().getUserManager().update();
	};

	public final EventBus.EventListener<GameJoinEvent> onGameJoin = event -> {
		Eden.getInstance().getHypixelManager().clear();
		Eden.getInstance().getUserManager().clear();
	};

	public final EventBus.EventListener<ServerJoinEvent> onServerJoin = event -> {

		List<Profile> profiles = Eden.getInstance().getProfileManager().getProfiles();

		for (Profile p : profiles) {

			String address = p.getServerIp();

			if (event.getAddress().contains(address)) {
				Eden.getInstance().getProfileManager().load(p);
				break;
			}
		}
	};
}
