package cn.wyceden.management.mod.impl.hud;

import cn.wyceden.event.EventBus;
import cn.wyceden.event.client.RenderSkiaEvent;
import cn.wyceden.management.mod.api.hud.SimpleHUDMod;
import cn.wyceden.skia.font.Icon;

public class FPSDisplayMod extends SimpleHUDMod {

	public FPSDisplayMod() {
		super("mod.fpsdisplay.name", "mod.fpsdisplay.description", Icon.MONITOR);
	}

	public final EventBus.EventListener<RenderSkiaEvent> onRenderSkia = event -> {
		this.draw();
	};

	@Override
	public String getText() {
		return client.getCurrentFps() + " FPS";
	}

	@Override
	public String getIcon() {
		return Icon.MONITOR;
	}
}
