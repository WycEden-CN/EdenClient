package cn.wyceden.management.mod.impl.hud;

import cn.wyceden.event.EventBus;
import cn.wyceden.event.client.RenderSkiaEvent;
import cn.wyceden.management.mod.api.hud.SimpleHUDMod;
import cn.wyceden.skia.font.Icon;

public class HealthDisplayMod extends SimpleHUDMod {

	public HealthDisplayMod() {
		super("mod.healthdisplay.name", "mod.healthdisplay.description", Icon.FAVORITE);
	}

	public EventBus.EventListener<RenderSkiaEvent> onRenderSkia = event -> {
		this.draw();
	};

	@Override
	public String getText() {
		return (int) client.player.getHealth() + " Health";
	}

	@Override
	public String getIcon() {
		return Icon.FAVORITE;
	}
}