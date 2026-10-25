package cn.wyceden.management.mod.settings.impl;

import java.util.List;
import java.util.function.Consumer;

import cn.wyceden.Eden;
import cn.wyceden.management.mod.Mod;
import cn.wyceden.management.mod.settings.Setting;

public class ComboSetting extends Setting {

	private List<String> options;
	private String defaultOption, option;

	private Consumer<String> changeListener;

	public ComboSetting(String name, String description, String icon, Mod parent, List<String> options, String option) {
		super(name, description, icon, parent);
		this.options = options;
		this.option = option;
		this.defaultOption = option;

		Eden.getInstance().getModManager().addSetting(this);
	}

	@Override
	public void reset() {
		this.option = defaultOption;
		if (changeListener != null) {
			changeListener.accept(option);
		}
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
		if (changeListener != null) {
			changeListener.accept(option);
		}
	}

	public void setOnChanged(Consumer<String> listener) {
		this.changeListener = listener;
	}

	public List<String> getOptions() {
		return options;
	}

	public String getDefaultOption() {
		return defaultOption;
	}

	public void setDefaultOption(String defaultOption) {
		this.defaultOption = defaultOption;
	}

	public boolean has(String s) {

		for (String option : options) {
			if (option.equals(s)) {
				return true;
			}
		}

		return false;
	}
}
