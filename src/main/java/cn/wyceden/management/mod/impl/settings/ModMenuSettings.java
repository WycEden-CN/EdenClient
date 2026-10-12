package cn.wyceden.management.mod.impl.settings;

import org.lwjgl.glfw.GLFW;

import cn.wyceden.event.EventBus;
import cn.wyceden.event.client.ClientTickEvent;
import cn.wyceden.gui.modmenu.GuiModMenu;
import cn.wyceden.libraries.material3.hct.Hct;
import cn.wyceden.management.mod.Mod;
import cn.wyceden.management.mod.ModCategory;
import cn.wyceden.management.mod.settings.impl.BooleanSetting;
import cn.wyceden.management.mod.settings.impl.HctColorSetting;
import cn.wyceden.management.mod.settings.impl.KeybindSetting;
import cn.wyceden.management.mod.settings.impl.NumberSetting;
import cn.wyceden.skia.font.Icon;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;

public class ModMenuSettings extends Mod {

	private static ModMenuSettings instance;

	private KeybindSetting keybindSetting = new KeybindSetting("setting.keybind", "setting.keybind.description",
			Icon.KEYBOARD, this, InputUtil.fromKeyCode(GLFW.GLFW_KEY_RIGHT_SHIFT, 0));
	private BooleanSetting darkModeSetting = new BooleanSetting("setting.darkmode", "setting.darkmode.description",
			Icon.DARK_MODE, this, false);
	private HctColorSetting hctColorSetting = new HctColorSetting("setting.color", "setting.color.description",
			Icon.PALETTE, this, Hct.from(220, 26, 6));
	private BooleanSetting blurSetting = new BooleanSetting("setting.blur", "setting.blur.description", Icon.LENS_BLUR,
			this, true);
	private NumberSetting blurIntensitySetting = new NumberSetting("setting.blurintensity",
			"setting.blurintensity.description", Icon.BLUR_LINEAR, this, 5, 1, 20, 1);

	private Screen modMenu;

	public ModMenuSettings() {
		super("mod.modmenu.name", "mod.modmenu.description", Icon.MENU, ModCategory.MISC);

		instance = this;
		this.setHidden(true);
		this.setEnabled(true);
	}

	public final EventBus.EventListener<ClientTickEvent> onClientTick = event -> {

		if (keybindSetting.isPressed()) {

			if (modMenu == null) {
				modMenu = new GuiModMenu().build();
			}

			client.setScreen(modMenu);
		}
	};

	@Override
	public void onDisable() {
		this.setEnabled(true);
	}

	public static ModMenuSettings getInstance() {
		return instance;
	}

	public BooleanSetting getDarkModeSetting() {
		return darkModeSetting;
	}

	public HctColorSetting getHctColorSetting() {
		return hctColorSetting;
	}

	public BooleanSetting getBlurSetting() {
		return blurSetting;
	}

	public NumberSetting getBlurIntensitySetting() {
		return blurIntensitySetting;
	}

	public Screen getModMenu() {
		return modMenu;
	}
}
