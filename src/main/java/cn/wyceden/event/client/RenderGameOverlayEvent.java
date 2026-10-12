package cn.wyceden.event.client;

import cn.wyceden.event.Event;

import net.minecraft.client.gui.DrawContext;

public class RenderGameOverlayEvent extends Event {
	
	private final DrawContext context;
	
	public RenderGameOverlayEvent(DrawContext context) {
		this.context = context;
	}

	public DrawContext getContext() {
		return context;
	}
}
