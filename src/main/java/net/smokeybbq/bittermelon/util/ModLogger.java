package net.smokeybbq.bittermelon.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModLogger {
    private static final Logger LOGGER = LogManager.getLogger("Bittermelon");

    public static void info(String message) {
        LOGGER.info("[Bittermelon] " + message);
    }

    public static void warn(String message) {
        LOGGER.warn("[Bittermelon] " + message);
    }

    public static void error(String message) {
        LOGGER.error("[Bittermelon] " + message);
    }

    public static void debug(String message) {
        LOGGER.debug("[Bittermelon] " + message);
    }
}
