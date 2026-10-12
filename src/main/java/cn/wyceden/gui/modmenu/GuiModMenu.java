package cn.wyceden.gui.modmenu;

import java.util.ArrayList;
import java.util.List;

import cn.wyceden.gui.api.EdenGui;
import cn.wyceden.gui.api.page.SimplePage;
import cn.wyceden.gui.modmenu.component.NavigationRail;
import cn.wyceden.gui.modmenu.pages.HomePage;
import cn.wyceden.gui.modmenu.pages.ModsPage;
import cn.wyceden.gui.modmenu.pages.MusicPage;
import cn.wyceden.gui.modmenu.pages.ProfilePage;
import cn.wyceden.gui.modmenu.pages.SettingsPage;

public class GuiModMenu extends EdenGui {

	private NavigationRail navigationRail;

	public GuiModMenu() {
		super(false);
	}

	@Override
	public void init() {
		components.clear();
		navigationRail = new NavigationRail(this, getX(), getY(), 90, getHeight());
		components.add(navigationRail);
		super.init();
	}

	@Override
	public void setPageSize(SimplePage p) {
		p.setX(getX() + navigationRail.getWidth());
		p.setY(getY());
		p.setWidth(getWidth() - navigationRail.getWidth());
		p.setHeight(getHeight());
	}

	@Override
	public List<SimplePage> createPages() {

		List<SimplePage> pages = new ArrayList<>();

		pages.add(new HomePage(this));
		pages.add(new ModsPage(this));
		pages.add(new MusicPage(this));
		pages.add(new ProfilePage(this));
		pages.add(new SettingsPage(this));

		return pages;
	}

	@Override
	public float getX() {
		return (client.getWindow().getWidth() / 2) - (getWidth() / 2);
	}

	@Override
	public float getY() {
		return (client.getWindow().getHeight() / 2) - (getHeight() / 2);
	}

	@Override
	public float getWidth() {
		return 938;
	}

	@Override
	public float getHeight() {
		return 580;
	}
}
