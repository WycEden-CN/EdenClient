package cn.wyceden.utils.language;

import java.util.Locale;

public enum Language {
	ENGLISH("en"),
	CHINESE("zh");

	private final String id;

	private Language(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public static Language getSystemLanguage() {
		String lang = Locale.getDefault().getLanguage();
		if (lang.equalsIgnoreCase("zh")) {
			return CHINESE;
		}
		return ENGLISH;
	}
}
