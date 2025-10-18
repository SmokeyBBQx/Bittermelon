package com.site21.bittermelon.common.systems.character.skin;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class SkinDownloader {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final int READ_TIMEOUT = 10000;

    public CompletableFuture<NativeImage> download(String imageUrl, File cacheFile) {
        return CompletableFuture.supplyAsync(() -> {
            HttpURLConnection connection = null;
            try {
                connection = createConnection(imageUrl);

                if (connection.getResponseCode() != 200) {
                    LOGGER.warn("HTTP error {} downloading skin", connection.getResponseCode());
                    return null;
                }

                return readImage(connection.getInputStream(), cacheFile);
            } catch (Exception e) {
                LOGGER.error("Failed to download skin from {}", imageUrl, e);
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }, Util.backgroundExecutor());
    }

    private @NotNull HttpURLConnection createConnection(String imageUrl) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(imageUrl).openConnection(Minecraft.getInstance().getProxy());
        connection.setDoInput(true);
        connection.setDoOutput(false);
        connection.setRequestProperty("Accept", "image/png");
        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);
        connection.connect();
        return connection;
    }

    private @NotNull NativeImage readImage(InputStream inputStream, File cacheFile) throws IOException {
        if (cacheFile != null) {
            FileUtils.copyInputStreamToFile(inputStream, cacheFile);
            try (FileInputStream fis = new FileInputStream(cacheFile)) {
                return NativeImage.read(fis);
            }
        }
        return NativeImage.read(inputStream);
    }
}

