package cn.wyceden.event.server.impl;

import cn.wyceden.event.Event;

public class DamageEntityEvent extends Event {

	private final int entityId;

	public DamageEntityEvent(int entityId) {
		this.entityId = entityId;
	}

	public int getEntityId() {
		return entityId;
	}
}
