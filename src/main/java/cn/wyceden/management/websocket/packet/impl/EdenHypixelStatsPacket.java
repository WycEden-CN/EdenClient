package cn.wyceden.management.websocket.packet.impl;

import com.google.gson.JsonObject;
import cn.wyceden.management.websocket.packet.EdenPacket;

public class EdenHypixelStatsPacket extends EdenPacket {

	private final String uuid;
	
	public EdenHypixelStatsPacket(String uuid) {
		super("eden-hypixel-stats");
		this.uuid = uuid;
	}

	@Override
	public JsonObject toJson() {
		jsonObject.addProperty("uuid", uuid);
		return jsonObject;
	}
}
