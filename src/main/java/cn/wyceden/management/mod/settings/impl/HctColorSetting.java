package cn.wyceden.management.mod.settings.impl;

import cn.wyceden.Eden;
import cn.wyceden.libraries.material3.hct.Hct;
import cn.wyceden.management.mod.Mod;
import cn.wyceden.management.mod.settings.Setting;

public class HctColorSetting extends Setting {

	private Hct hct;
	private Hct defaultHct;

	public HctColorSetting(String name, String description, String icon, Mod parent, Hct hct) {
		super(name, description, icon, parent);
		this.hct = hct;
		this.defaultHct = hct;

		Eden.getInstance().getModManager().addSetting(this);
	}

	@Override
	public void reset() {
		this.hct = defaultHct;
	}

	public Hct getHct() {
		return hct;
	}

	public void setHct(Hct hct) {
		this.hct = hct;
	}

	public Hct getDefaultHct() {
		return defaultHct;
	}
}