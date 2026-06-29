package com.site21.bittermelon.common.systems.character.skin;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.Minecraft;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.Identifier;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class SkinManager {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final File CACHE_DIR = new File(Minecraft.getInstance().gameDirectory, "cache/skins");

    private static final long MAX_CACHE_SIZE_MB = 100;
    private static final int MAX_CACHE_FILES = 1000;

    static {
        initializeCache();
    }

    public static ClientAsset.Texture loadSkin(String url, String imageName) {
        return loadSkin(url, imageName, null);
    }

    public static ClientAsset.Texture loadSkin(String url, String imageName, Runnable callback) {
        Identifier location = Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "skins/" + imageName);

        File cacheFile = new File(CACHE_DIR, imageName + ".png");

        Minecraft.getInstance().execute(() -> {
            SkinTexture texture = new SkinTexture(location, cacheFile, url, callback);
            Minecraft.getInstance().getTextureManager().registerAndLoad(location, texture);
        });

        return new ClientAsset.ResourceTexture(location);
    }

    public static void clearCache() {
        if (CACHE_DIR.exists()) {
            try {
                FileUtils.deleteDirectory(CACHE_DIR);
                LOGGER.info("Cleared skin cache");
            } catch (IOException e) {
                LOGGER.warn("Failed to clear cache", e);
            }
        }
    }

    public static void clearCachedSkin(String imageName) {
        File cacheFile = new File(CACHE_DIR, imageName + ".png");
        if (cacheFile.exists() && cacheFile.delete()) {
            LOGGER.debug("Cleared cached skin: {}", imageName);
        }
    }

    private static void initializeCache() {
        if (!CACHE_DIR.exists()) {
            CACHE_DIR.mkdirs();
        }

        checkFileCount();
        checkCacheSize();
        clearTempFiles();
    }

    private static void checkFileCount() {
        if (!CACHE_DIR.exists()) return;

        File[] files = CACHE_DIR.listFiles();
        if (files != null && files.length > MAX_CACHE_FILES) {
            LOGGER.info("Cache has {} files, exceeding limit of {}, clearing cache",
                    files.length, MAX_CACHE_FILES);
            clearCache();
        }
    }

    private static void checkCacheSize() {
        if (!CACHE_DIR.exists()) return;

        long totalSize = FileUtils.sizeOfDirectory(CACHE_DIR);
        long maxSizeBytes = MAX_CACHE_SIZE_MB * 1024 * 1024;

        if (totalSize > maxSizeBytes) {
            LOGGER.info("Cache size ({} MB) exceeds limit ({} MB), clearing cache",
                    totalSize / (1024 * 1024), MAX_CACHE_SIZE_MB);
            clearCache();
        }
    }

    private static void clearTempFiles() {
        if (!CACHE_DIR.exists()) return;

        File[] files = CACHE_DIR.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.getName().contains("temp")) file.delete();
        }
    }
}