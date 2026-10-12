package cn.wyceden.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EdenLogger {

	private static final Logger logger = LogManager.getLogger("Eden Client");

	public static void info(String prefix, String message) {
		logger.info("[EDEN/INFO] [" + prefix + "] " + message);
	}

	public static void warn(String prefix, String message) {
		logger.warn("[EDEN/WARN] [" + prefix + "] " + message);
	}

	public static void error(String prefix, String message) {
		logger.error("[EDEN/ERROR] [" + prefix + "] " + message);
	}

	public static void error(String prefix, String message, Exception e) {
		logger.error("[EDEN/ERROR] [" + prefix + "] " + message, e);
	}

	public static Logger getLogger() {
		return logger;
	}
}