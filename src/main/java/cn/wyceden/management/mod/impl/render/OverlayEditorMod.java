package cn.wyceden.management.mod.impl.render;

import cn.wyceden.management.mod.Mod;
import cn.wyceden.management.mod.ModCategory;
import cn.wyceden.management.mod.settings.impl.BooleanSetting;
import cn.wyceden.skia.font.Icon;

public class OverlayEditorMod extends Mod {

	private static OverlayEditorMod instance;

	private BooleanSetting clearWaterSetting = new BooleanSetting("setting.clearwater",
			"setting.clearwater.description", Icon.WATER_DROP, this, false);
	private BooleanSetting clearFireSetting = new BooleanSetting("setting.clearfire", "setting.clearfire.description",
			Icon.LOCAL_FIRE_DEPARTMENT, this, false);

	public OverlayEditorMod() {
		super("mod.overlayeditor.name", "mod.overlayeditor.description", Icon.FILTER_FRAMES, ModCategory.RENDER);

		instance = this;
	}

	public static OverlayEditorMod getInstance() {
		return instance;
	}

	public boolean isClearWater() {
		return clearWaterSetting.isEnabled();
	}

	public boolean isClearFire() {
		return clearFireSetting.isEnabled();
	}
}