
package cn.wyceden.management.mod.impl.player;

import org.lwjgl.glfw.GLFW;

import cn.wyceden.animation.SimpleAnimation;
import cn.wyceden.event.EventBus;
import cn.wyceden.event.client.ClientTickEvent;
import cn.wyceden.event.client.MouseScrollEvent;
import cn.wyceden.management.mod.Mod;
import cn.wyceden.management.mod.ModCategory;
import cn.wyceden.management.mod.settings.impl.BooleanSetting;
import cn.wyceden.management.mod.settings.impl.KeybindSetting;
import cn.wyceden.management.mod.settings.impl.NumberSetting;
import cn.wyceden.skia.font.Icon;

import net.minecraft.client.util.InputUtil;

public class ZoomMod extends Mod {

	private static ZoomMod instance;

	private SimpleAnimation animation = new SimpleAnimation();

	private boolean active;
	private float currentFactor = 1;

	public boolean wasSmooth;

	private BooleanSetting scrollSetting = new BooleanSetting("setting.scroll", "setting.zoom.scroll.description",
			Icon.SCROLLABLE_HEADER, this, false);
	private BooleanSetting smoothZoomSetting = new BooleanSetting("setting.smoothzoom",
			"setting.smoothzoom.description", Icon.MOTION_BLUR, this, false);
	private NumberSetting zoomSpeedSetting = new NumberSetting("setting.zoomspeed", "setting.zoomspeed.description",
			Icon.SPEED, this, 0.6F, 0, 1, 0.1F);
	private NumberSetting factorSetting = new NumberSetting("setting.zoomfactor", "setting.zoomfactor.description",
			Icon.ZOOM_OUT, this, 4, 2, 15, 1);
	private BooleanSetting smoothCameraSetting = new BooleanSetting("setting.smoothcamera",
			"setting.smoothcamera.description", Icon.MOTION_BLUR, this, true);
	private KeybindSetting keybindSetting = new KeybindSetting("setting.keybind", "setting.keybind.description",
			Icon.KEYBOARD, this, InputUtil.fromKeyCode(GLFW.GLFW_KEY_C, 0));

	public ZoomMod() {
		super("mod.zoom.name", "mod.zoom.description", Icon.ZOOM_IN, ModCategory.PLAYER);

		instance = this;
	}

	public final EventBus.EventListener<ClientTickEvent> onClientTick = event -> {
		if (keybindSetting.isKeyDown()) {
			if (!active) {
				active = true;
				resetFactor();
				wasSmooth = client.options.smoothCameraEnabled;
				client.options.smoothCameraEnabled = smoothCameraSetting.isEnabled();
			}
		} else if (active) {
			active = false;
			setFactor(1);
			client.options.smoothCameraEnabled = wasSmooth;
		}
	};

	public final EventBus.EventListener<MouseScrollEvent> onMouseScroll = event -> {
		if (active && scrollSetting.isEnabled()) {
			event.setCancelled(true);
			if (event.getAmount() < 0) {
				if (currentFactor < 0.98) {
					currentFactor += 0.03;
				}
			} else if (event.getAmount() > 0) {
				if (currentFactor > 0.06) {
					currentFactor -= 0.03;
				}
			}
		}
	};

	public void resetFactor() {
		setFactor(1 / factorSetting.getValue());
	}

	public void setFactor(float factor) {
		currentFactor = factor;
	}

	public float getFov(float fov) {

		if (smoothZoomSetting.isEnabled()) {
			animation.onTick(currentFactor, zoomSpeedSetting.getValue() * 10);
		}

		return fov * (smoothZoomSetting.isEnabled() ? animation.getValue() : currentFactor);
	}

	public static ZoomMod getInstance() {
		return instance;
	}
}
