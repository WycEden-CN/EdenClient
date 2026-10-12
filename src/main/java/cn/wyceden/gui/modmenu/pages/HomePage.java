package cn.wyceden.gui.modmenu.pages;

import cn.wyceden.gui.api.EdenGui;
import cn.wyceden.gui.api.page.Page;
import cn.wyceden.gui.api.page.impl.RightLeftTransition;
import cn.wyceden.skia.font.Icon;

public class HomePage extends Page {

	public HomePage(EdenGui parent) {
		super(parent, "text.home", Icon.HOME, new RightLeftTransition(true));
	}

}
