package cn.wyceden;

import cn.wyceden.animation.Delta;
import cn.wyceden.event.EventBus;
import cn.wyceden.event.server.PacketHandler;
import cn.wyceden.libraries.browser.JCefBrowser;
import cn.wyceden.management.color.ColorManager;
import cn.wyceden.management.config.ConfigManager;
import cn.wyceden.management.hypixel.HypixelManager;
import cn.wyceden.management.mod.ModManager;
import cn.wyceden.management.music.MusicManager;
import cn.wyceden.management.profile.ProfileManager;
import cn.wyceden.management.user.UserManager;
import cn.wyceden.management.websocket.WebSocketManager;
import cn.wyceden.skia.font.Fonts;
import cn.wyceden.utils.file.FileLocation;
import cn.wyceden.utils.language.I18n;
import cn.wyceden.utils.language.Language;

public class Eden {

	private final static Eden instance = new Eden();

	private final String name = "Eden";
	private final String version = "8.0";

	private long launchTime;

	private ModManager modManager;
	private ColorManager colorManager;
	private MusicManager musicManager;
	private ConfigManager configManager;
	private ProfileManager profileManager;
	private WebSocketManager webSocketManager;
	private UserManager userManager;
	private HypixelManager hypixelManager;

	public void start() {

		JCefBrowser.download();
		Fonts.loadAll();
		FileLocation.init();
		I18n.setLanguage(Language.ENGLISH);

		launchTime = System.currentTimeMillis();

		modManager = new ModManager();
		modManager.init();
		colorManager = new ColorManager();
		musicManager = new MusicManager();
		configManager = new ConfigManager();
		profileManager = new ProfileManager();
		webSocketManager = new WebSocketManager();
		userManager = new UserManager();
		hypixelManager = new HypixelManager();

		EventBus.getInstance().register(new EdenHandler());
		EventBus.getInstance().register(new PacketHandler());
		EventBus.getInstance().register(new Delta());
	}

	public static Eden getInstance() {
		return instance;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public long getLaunchTime() {
		return launchTime;
	}

	public ModManager getModManager() {
		return modManager;
	}

	public ColorManager getColorManager() {
		return colorManager;
	}

	public MusicManager getMusicManager() {
		return musicManager;
	}

	public ConfigManager getConfigManager() {
		return configManager;
	}

	public ProfileManager getProfileManager() {
		return profileManager;
	}

	public WebSocketManager getWebSocketManager() {
		return webSocketManager;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public HypixelManager getHypixelManager() {
		return hypixelManager;
	}
}
