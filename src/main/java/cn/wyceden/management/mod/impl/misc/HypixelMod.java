package cn.wyceden.management.mod.impl.misc;

import cn.wyceden.event.EventBus;
import cn.wyceden.event.client.ClientTickEvent;
import cn.wyceden.management.mod.Mod;
import cn.wyceden.management.mod.ModCategory;
import cn.wyceden.management.mod.settings.impl.BooleanSetting;
import cn.wyceden.skia.font.Icon;
import cn.wyceden.utils.TimerUtils;
import cn.wyceden.utils.server.Server;
import cn.wyceden.utils.server.ServerUtils;

public class HypixelMod extends Mod {

	private static HypixelMod instance;
	private BooleanSetting levelHeadSetting = new BooleanSetting("setting.levelhead", "setting.levelhead.description",
			Icon._123, this, false);
	private BooleanSetting autoTipSetting = new BooleanSetting("setting.autotip", "setting.autotip.description",
			Icon.MONEY, this, false);

	private TimerUtils tipTimer = new TimerUtils();

	public HypixelMod() {
		super("mod.hypixel.name", "mod.hypixel.description", Icon.CONSTRUCTION, ModCategory.MISC);

		instance = this;
	}

	public final EventBus.EventListener<ClientTickEvent> onClientTick = event -> {
		if (autoTipSetting.isEnabled() && client.player != null && ServerUtils.isJoin(Server.HYPIXEL)) {
			if (tipTimer.delay(1200000)) {
				client.player.networkHandler.sendChatCommand("tip all");
				tipTimer.reset();
			}
		} else {
			tipTimer.reset();
		}
	};

	public static HypixelMod getInstance() {
		return instance;
	}

	public BooleanSetting getLevelHeadSetting() {
		return levelHeadSetting;
	}
}