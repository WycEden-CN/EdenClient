package cn.wyceden.management.mod.impl.hud;

import cn.wyceden.event.EventBus;
import cn.wyceden.event.client.RenderSkiaEvent;
import cn.wyceden.management.mod.api.hud.SimpleHUDMod;
import cn.wyceden.skia.font.Icon;

public class PlayerCounterMod extends SimpleHUDMod {

	public PlayerCounterMod() {
		super("mod.playercounter.name", "mod.playercounter.description", Icon.GROUPS);
	}

	public final EventBus.EventListener<RenderSkiaEvent> onRenderSkia = event -> {
		this.draw();
	};

	@Override
	public String getText() {
		return "Player: " + client.world.getPlayers().size();
	}

	@Override
	public String getIcon() {
		return Icon.GROUPS;
	}
}