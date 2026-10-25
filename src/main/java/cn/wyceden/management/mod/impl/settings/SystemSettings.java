package cn.wyceden.management.mod.impl.settings;

import java.io.File;
import java.util.Arrays;

import cn.wyceden.management.mod.Mod;
import cn.wyceden.management.mod.ModCategory;
import cn.wyceden.management.mod.settings.impl.ComboSetting;
import cn.wyceden.management.mod.settings.impl.FileSetting;
import cn.wyceden.management.mod.settings.impl.StringSetting;
import cn.wyceden.skia.font.Icon;
import cn.wyceden.utils.OS;
import cn.wyceden.utils.language.I18n;
import cn.wyceden.utils.language.Language;

public class SystemSettings extends Mod {

	public static final String LANG_AUTO = "setting.language.auto";
	public static final String LANG_EN = "setting.language.en";
	public static final String LANG_ZH = "setting.language.zh";

	public static final String PM_AUTO = "setting.performancemode.auto";
	public static final String PM_BALANCED = "setting.performancemode.balanced";
	public static final String PM_PERFORMANCE = "setting.performancemode.performance";
	public static final String PM_QUALITY = "setting.performancemode.quality";

	private static SystemSettings instance;
	private ComboSetting blurTypeSetting = new ComboSetting("setting.blurtype", "setting.blurtype.description",
			Icon.BLUR_MEDIUM, this, Arrays.asList("setting.fastblur", "setting.normalblur"), "setting.normalblur");
	private ComboSetting languageSetting = new ComboSetting("setting.language", "setting.language.description",
			Icon.LANGUAGE, this, Arrays.asList(LANG_AUTO, LANG_EN, LANG_ZH), LANG_AUTO);
	private ComboSetting performanceModeSetting = new ComboSetting("setting.performancemode",
			"setting.performancemode.description", Icon.TUNE, this,
			Arrays.asList(PM_AUTO, PM_BALANCED, PM_PERFORMANCE, PM_QUALITY), PM_AUTO);

	private FileSetting ytdlpPathSetting;
	private FileSetting ffmpegPathSetting;
	private StringSetting ytdlpCommandSetting;

	public SystemSettings() {
		super("mod.systemsettings.name", "mod.systemsettings.description", Icon.SETTINGS, ModCategory.MISC);
		instance = this;
		this.setHidden(true);
		this.setEnabled(true);

		languageSetting.setOnChanged(this::applyLanguage);

		if (OS.isWindows()) {
			ytdlpPathSetting = new FileSetting("setting.ytdlppath", "setting.ytdlppath.description",
					Icon.CONVERSION_PATH, this, null, "exe");
			ffmpegPathSetting = new FileSetting("setting.ffmpegpath", "setting.ffmpegpath.description",
					Icon.CONVERSION_PATH, this, null, "exe");
		} else if (OS.isMacOS() || OS.isLinux()) {
			ytdlpCommandSetting = new StringSetting("setting.ytdlpcommand", "setting.ytdlpcommand.description",
					Icon.TERMINAL, this, "yt-dlp");
		}
	}

	public void applyLanguage(String option) {
		Language language;
		if (LANG_ZH.equals(option)) {
			language = Language.CHINESE;
		} else if (LANG_EN.equals(option)) {
			language = Language.ENGLISH;
		} else {
			language = Language.getSystemLanguage();
		}
		I18n.setLanguage(language);
	}

	@Override
	public void onDisable() {
		this.setEnabled(true);
	}

	public static SystemSettings getInstance() {
		return instance;
	}

	public ComboSetting getLanguageSetting() {
		return languageSetting;
	}

	public ComboSetting getPerformanceModeSetting() {
		return performanceModeSetting;
	}

	public boolean isFastBlur() {
		return blurTypeSetting.getOption().contains("fastblur");
	}

	public File getYtdlpPath() {
		return ytdlpPathSetting != null && ytdlpPathSetting.getFile() != null ? ytdlpPathSetting.getFile() : null;
	}

	public File getFFmpegPath() {
		return ffmpegPathSetting != null && ffmpegPathSetting.getFile() != null ? ffmpegPathSetting.getFile() : null;
	}

	public String getYtdlpCommand() {
		return ytdlpCommandSetting != null ? ytdlpCommandSetting.getValue() : "";
	}
}
