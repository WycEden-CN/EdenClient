package cn.wyceden.gui.modmenu.pages;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import cn.wyceden.Eden;
import cn.wyceden.gui.api.EdenGui;
import cn.wyceden.gui.api.page.Page;
import cn.wyceden.gui.api.page.impl.RightTransition;
import cn.wyceden.gui.modmenu.component.SettingBar;
import cn.wyceden.management.mod.Mod;
import cn.wyceden.management.mod.settings.Setting;
import cn.wyceden.skia.Skia;
import cn.wyceden.skia.font.Icon;
import cn.wyceden.utils.SearchUtils;
import cn.wyceden.utils.language.I18n;

public class SettingsImplPage extends Page {

	private List<SettingBar> bars = new ArrayList<>();

	private Class<? extends Page> prevPage;
	private Mod mod;

	public SettingsImplPage(EdenGui parent, Class<? extends Page> prevPage, Mod mod) {
		super(parent, "text.mods", Icon.SETTINGS, new RightTransition(true));
		this.prevPage = prevPage;
		this.mod = mod;
	}

	@Override
	public void init() {
		super.init();

		bars.clear();

		for (Setting s : Eden.getInstance().getModManager().getSettingsByMod(mod)) {
			SettingBar bar = new SettingBar(s, x + 32, y + 32, width - 64);
			bars.add(bar);
		}

		parent.setClosable(false);
	}

	@Override
	public void draw(double mouseX, double mouseY) {
		super.draw(mouseX, mouseY);

		float offsetY = 96;

		mouseY = mouseY - scrollHelper.getValue();

		Skia.save();
		Skia.translate(0, scrollHelper.getValue());

		for (SettingBar b : bars) {

			if (!searchBar.getText().isEmpty() && !SearchUtils.isSimilar(I18n.get(b.getTitle()), searchBar.getText())) {
				continue;
			}

			b.setY(y + offsetY);
			b.draw(mouseX, mouseY);

			offsetY += b.getHeight() + 18;
		}

		scrollHelper.setMaxScroll(offsetY, height);
		Skia.restore();
	}

	@Override
	public void mousePressed(double mouseX, double mouseY, int button) {
		super.mousePressed(mouseX, mouseY, button);

		mouseY = mouseY - scrollHelper.getValue();

		searchBar.mousePressed(mouseX, mouseY, button);

		for (SettingBar b : bars) {

			if (!searchBar.getText().isEmpty() && !SearchUtils.isSimilar(I18n.get(b.getTitle()), searchBar.getText())) {
				continue;
			}

			b.mousePressed(mouseX, mouseY, button);
		}
	}

	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {
		super.mouseReleased(mouseX, mouseY, button);

		mouseY = (int) (mouseY - scrollHelper.getValue());

		searchBar.mousePressed(mouseX, mouseY, button);

		for (SettingBar b : bars) {

			if (!searchBar.getText().isEmpty() && !SearchUtils.isSimilar(I18n.get(b.getTitle()), searchBar.getText())) {
				continue;
			}

			b.mouseReleased(mouseX, mouseY, button);
		}
	}

	@Override
	public void charTyped(char chr, int modifiers) {
		super.charTyped(chr, modifiers);

		for (SettingBar b : bars) {

			if (!searchBar.getText().isEmpty() && !SearchUtils.isSimilar(I18n.get(b.getTitle()), searchBar.getText())) {
				continue;
			}

			b.charTyped(chr, modifiers);
		}
	}

	@Override
	public void keyPressed(int keyCode, int scanCode, int modifiers) {
		super.keyPressed(keyCode, scanCode, modifiers);

		for (SettingBar b : bars) {

			if (!searchBar.getText().isEmpty() && !SearchUtils.isSimilar(I18n.get(b.getTitle()), searchBar.getText())) {
				continue;
			}

			b.keyPressed(keyCode, scanCode, modifiers);
		}

		if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
			parent.setClosable(true);
			parent.setCurrentPage(prevPage);
		}
	}

	@Override
	public void onClosed() {
		if (!parent.isClosable()) {
			parent.setClosable(true);
			parent.getPage(prevPage).onClosed();
		}
	}
}
