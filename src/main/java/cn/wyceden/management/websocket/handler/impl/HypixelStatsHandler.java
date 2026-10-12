package cn.wyceden.management.websocket.handler.impl;

import com.google.gson.JsonObject;
import cn.wyceden.Eden;
import cn.wyceden.management.hypixel.HypixelManager;
import cn.wyceden.management.hypixel.api.HypixelUser;
import cn.wyceden.management.websocket.handler.WebSocketHandler;
import cn.wyceden.utils.JsonUtils;

public class HypixelStatsHandler extends WebSocketHandler {

	@Override
	public void handle(JsonObject jsonObject) {
		
		HypixelManager hypixelManager = Eden.getInstance().getHypixelManager();

		String uuid = JsonUtils.getStringProperty(jsonObject, "uuid", "null");
		String networkLevel = JsonUtils.getStringProperty(jsonObject, "networkLevel", "-1");
		String bedwarsLevel = JsonUtils.getStringProperty(jsonObject, "bedwarsLevel", "-1");
		String winLoseRatio = JsonUtils.getStringProperty(jsonObject, "winLoseRatio", "-1");
		String finalKillDeathRatio = JsonUtils.getStringProperty(jsonObject, "finalKillDeathRatio", "-1");
		String bedsBrokeLostratio = JsonUtils.getStringProperty(jsonObject, "bedsBrokeLostRatio", "-1");
		
		hypixelManager.add(new HypixelUser(uuid, networkLevel, bedwarsLevel, winLoseRatio, finalKillDeathRatio,
				bedsBrokeLostratio));
	}
}
