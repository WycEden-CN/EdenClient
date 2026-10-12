package cn.wyceden.gui.modmenu.pages;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import cn.wyceden.Eden;
import cn.wyceden.animation.SimpleAnimation;
import cn.wyceden.gui.api.EdenGui;
import cn.wyceden.gui.api.page.Page;
import cn.wyceden.gui.api.page.impl.LeftRightTransition;
import cn.wyceden.gui.api.page.impl.RightLeftTransition;
import cn.wyceden.management.color.api.ColorPalette;
import cn.wyceden.management.mod.Mod;
import cn.wyceden.skia.Skia;
import cn.wyceden.skia.font.Fonts;
import cn.wyceden.skia.font.Icon;
import cn.wyceden.ui.component.api.PressAnimation;
import cn.wyceden.utils.ColorUtils;
import cn.wyceden.utils.SearchUtils;
import cn.wyceden.utils.language.I18n;
import cn.wyceden.utils.mouse.MouseUtils;

public class ModsPage extends Page {

	private List<Item> items = new ArrayList<>();

	public ModsPage(EdenGui parent) {
		super(parent, "text.mods", Icon.INVENTORY_2, new RightLeftTransition(true));
	}

	@Override
	public void init() {
		super.init();

		items.clear();
		
		for (Mod m : Eden.getInstance().getModManager().getMods()) {

			Item i = new Item(m);

			if (m.isEnabled()) {
				i.pressAnimation.setPressed();
			}

			items.add(i);
		}
		
		for (Item i : items) {
			i.xAnimation.setFirstTick(true);
			i.yAnimation.setFirstTick(true);
		}
	}

	@Override
	public void draw(double mouseX, double mouseY) {
		super.draw(mouseX, mouseY);

		ColorPalette palette = Eden.getInstance().getColorManager().getPalette();

		int index = 0;
		float offsetX = 26;
		float offsetY = 0;

		mouseY = mouseY - scrollHelper.getValue();

		Skia.save();
		Skia.translate(0, scrollHelper.getValue());

		for (Item i : items) {

			Mod m = i.mod;
			SimpleAnimation focusAnimation = i.focusAnimation;
			SimpleAnimation xAnimation = i.xAnimation;
			SimpleAnimation yAnimation = i.yAnimation;

			if (m.isHidden()) {
				continue;
			}

			if (!searchBar.getText().isEmpty() && !SearchUtils.isSimilar(I18n.get(m.getName()), searchBar.getText())) {
				continue;
			}

			float itemX = x + offsetX;
			float itemY = y + 96 + offsetY;

			focusAnimation.onTick(
					MouseUtils.isInside(mouseX, mouseY, itemX, itemY, 244, 116 + 35) ? i.pressed ? 0.12F : 0.08F : 0,
					8);
			xAnimation.onTick(itemX, 14);
			yAnimation.onTick(itemY, 14);

			itemX = xAnimation.getValue();
			itemY = yAnimation.getValue();

			Skia.drawRoundedRectVarying(itemX, itemY, 244, 116, 26, 26, 0, 0, palette.getSurface());
			Skia.drawRoundedRectVarying(itemX, itemY + 116, 244, 35, 0, 0, 26, 26, palette.getSurfaceContainerLow());

			Skia.drawRoundedRectVarying(itemX, itemY + 116, 244, 35, 0, 0, 26, 26,
					ColorUtils.applyAlpha(palette.getSurfaceContainerLowest(), focusAnimation.getValue()));

			Skia.save();
			Skia.clip(itemX, itemY + 116, 244, 35, 0, 0, 26, 26);
			i.pressAnimation.draw(itemX, itemY + 116, 224, 35, palette.getPrimaryContainer(), 1);
			Skia.restore();

			Skia.drawFullCenteredText(I18n.get(m.getName()), itemX + (244 / 2), itemY + 116 + (35 / 2),
					palette.getOnSurfaceVariant(), Fonts.getRegular(16));
			Skia.drawFullCenteredText(m.getIcon(), itemX + (244 / 2), itemY + (116 / 2), palette.getOnSurfaceVariant(),
					Fonts.getIcon(68));

			index++;
			offsetX += 32 + 244;

			if (index % 3 == 0) {
				offsetX = 26;
				offsetY += 22 + 151;
			}
		}

		scrollHelper.setMaxScroll(151, 22, index, 3, height - 96);
		Skia.restore();
	}

	@Override
	public void mousePressed(double mouseX, double mouseY, int button) {
		super.mousePressed(mouseX, mouseY, button);

		for (Item i : items) {

			float itemX = i.xAnimation.getValue();
			float itemY = i.yAnimation.getValue();

			if (i.mod.isHidden()) {
				continue;
			}

			if (!searchBar.getText().isEmpty()
					&& !SearchUtils.isSimilar(I18n.get(i.mod.getName()), searchBar.getText())) {
				continue;
			}

			if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {

				if (MouseUtils.isInside(mouseX, mouseY, itemX, itemY + 116, 244, 35)) {
					i.pressed = true;
				}
			}
		}
	}

	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {

		mouseY = mouseY - scrollHelper.getValue();

		for (Item i : items) {

			Mod m = i.mod;
			float itemX = i.xAnimation.getValue();
			float itemY = i.yAnimation.getValue();

			if (i.mod.isHidden()) {
				continue;
			}

			if (!searchBar.getText().isEmpty()
					&& !SearchUtils.isSimilar(I18n.get(i.mod.getName()), searchBar.getText())) {
				continue;
			}

			if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {

				if (MouseUtils.isInside(mouseX, mouseY, itemX, itemY + 116, 244, 35)) {
					m.toggle();

					if (m.isEnabled()) {
						i.pressAnimation.onPressed(mouseX, mouseY, itemX, itemY + 116);
					} else {
						i.pressAnimation.onReleased(mouseX, mouseY, itemX, itemY + 116);
					}
				}

				if (MouseUtils.isInside(mouseX, mouseY, itemX, itemY, 244, 116)
						&& !Eden.getInstance().getModManager().getSettingsByMod(m).isEmpty()) {
					parent.setCurrentPage(new SettingsImplPage(parent, this.getClass(), m));
					this.setTransition(new LeftRightTransition(true));
				}
			}

			i.pressed = false;
		}
	}

	@Override
	public void onClosed() {
		this.setTransition(new RightLeftTransition(true));
	}

	private class Item {

		private Mod mod;
		private SimpleAnimation focusAnimation = new SimpleAnimation();
		private SimpleAnimation xAnimation = new SimpleAnimation();
		private SimpleAnimation yAnimation = new SimpleAnimation();
		private PressAnimation pressAnimation = new PressAnimation();
		private boolean pressed;

		private Item(Mod mod) {
			this.mod = mod;
			this.pressed = false;
		}
	}
}
