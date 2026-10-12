package cn.wyceden.management.websocket.packet.impl;

import com.google.gson.JsonObject;
import cn.wyceden.management.websocket.packet.EdenPacket;

public class EdenUserPacket extends EdenPacket {

	private final String uuid;
	
	public EdenUserPacket(String uuid) {
		super("eden-user");
		this.uuid = uuid;
	}

	@Override
	public JsonObject toJson() {
		jsonObject.addProperty("uuid", uuid);
		return jsonObject;
	}
}
