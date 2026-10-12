package cn.wyceden.management.mod.impl.player;

import cn.wyceden.management.mod.Mod;
import cn.wyceden.management.mod.ModCategory;
import cn.wyceden.skia.font.Icon;

public class NoJumpDelayMod extends Mod {

	private static NoJumpDelayMod instance;

	public NoJumpDelayMod() {
		super("mod.nojumpdelay.name", "mod.nojumpdelay.description", Icon.KEYBOARD_DOUBLE_ARROW_UP, ModCategory.PLAYER);

		instance = this;
	}

	public static NoJumpDelayMod getInstance() {
		return instance;
	}
}