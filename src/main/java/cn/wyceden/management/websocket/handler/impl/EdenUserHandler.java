package cn.wyceden.management.websocket.handler.impl;

import com.google.gson.JsonObject;
import cn.wyceden.Eden;
import cn.wyceden.management.user.UserManager;
import cn.wyceden.management.websocket.handler.WebSocketHandler;
import cn.wyceden.utils.JsonUtils;

public class EdenUserHandler extends WebSocketHandler {

	@Override
	public void handle(JsonObject jsonObject) {
		
		UserManager userManager = Eden.getInstance().getUserManager();
		
		String uuid = JsonUtils.getStringProperty(jsonObject, "uuid", "null");
		boolean isUser = JsonUtils.getBooleanProperty(jsonObject, "edenUser", false);
		
		if(!uuid.equals("null")) {
			userManager.add(uuid, isUser);
		}
	}
}
