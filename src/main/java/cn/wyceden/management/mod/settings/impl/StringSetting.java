package cn.wyceden.management.mod.settings.impl;

import cn.wyceden.Eden;
import cn.wyceden.management.mod.Mod;
import cn.wyceden.management.mod.settings.Setting;

public class StringSetting extends Setting {

	private String defaultValue, value;

	public StringSetting(String name, String description, String icon, Mod parent, String string) {
		super(name, description, icon, parent);

		this.defaultValue = string;
		this.value = string;

		Eden.getInstance().getModManager().addSetting(this);
	}

	@Override
	public void reset() {
		this.value = defaultValue;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String string) {
		this.value = string;
	}

	public String getDefaultValue() {
		return defaultValue;
	}
}