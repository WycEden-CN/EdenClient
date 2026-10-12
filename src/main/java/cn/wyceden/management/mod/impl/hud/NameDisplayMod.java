package cn.wyceden.management.mod.impl.hud;

import cn.wyceden.event.EventBus;
import cn.wyceden.event.client.RenderSkiaEvent;
import cn.wyceden.management.mod.api.hud.SimpleHUDMod;
import cn.wyceden.skia.font.Icon;

public class NameDisplayMod extends SimpleHUDMod {

	public NameDisplayMod() {
		super("mod.namedisplay.name", "mod.namedisplay.description", Icon.PERSON);
	}

	public final EventBus.EventListener<RenderSkiaEvent> onRenderSkia = event -> {
		this.draw();
	};

	@Override
	public String getText() {
		return "Name: " + client.player.getGameProfile().getName();
	}

	@Override
	public String getIcon() {
		return Icon.PERSON;
	}
}