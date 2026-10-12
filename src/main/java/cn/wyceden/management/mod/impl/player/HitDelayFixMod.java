package cn.wyceden.management.mod.impl.player;

import cn.wyceden.management.mod.Mod;
import cn.wyceden.management.mod.ModCategory;
import cn.wyceden.skia.font.Icon;

public class HitDelayFixMod extends Mod {

	private static HitDelayFixMod instance;

	public HitDelayFixMod() {
		super("mod.hitdelayfix.name", "mod.hitdelayfix.description", Icon.TIMER_OFF, ModCategory.PLAYER);

		instance = this;
	}

	public static HitDelayFixMod getInstance() {
		return instance;
	}
}
