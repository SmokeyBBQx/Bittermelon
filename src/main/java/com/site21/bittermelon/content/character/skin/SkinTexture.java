package com.site21.bittermelon.content.character.skin;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
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

@OnlyIn(Dist.CLIENT)
public class SkinTexture extends SimpleTexture {
    private static final Logger LOGGER = LogManager.getLogger();

    private final File cacheFile;
    private final String imageUrl;
    private final Runnable onLoadCallback;
    private CompletableFuture<?> downloadFuture;
    private boolean textureUploaded;

    public SkinTexture(ResourceLocation location, File cacheFile, String imageUrl, Runnable onLoadCallback) {
        super(location);
        this.cacheFile = cacheFile;
        this.imageUrl = imageUrl;
        this.onLoadCallback = onLoadCallback;
    }

    @Override
    public void load(@NotNull ResourceManager manager) throws IOException {
//        Minecraft.getInstance().execute(() -> {
//            if (!textureUploaded) {
//                try {
//                    super.load(manager);
//                } catch (IOException e) {
//                    LOGGER.warn("Failed to load fallback texture: {}", location, e);
//                }
//                textureUploaded = true;
//            }
//        });

        if (downloadFuture == null) {
            NativeImage cachedImage = null;

            if (cacheFile != null && cacheFile.isFile()) {
                LOGGER.debug("Loading cached image from {}", cacheFile);
                try (FileInputStream fis = new FileInputStream(cacheFile)) {
                    cachedImage = NativeImage.read(fis);
                } catch (IOException e) {
                    LOGGER.warn("Failed to load cached image", e);
                }
            }

            if (cachedImage != null) {
                uploadImage(cachedImage);
            } else {
                startDownload();
            }
        }
    }

    private void startDownload() {
        this.downloadFuture = CompletableFuture.runAsync(() -> {
            HttpURLConnection connection = null;
            LOGGER.debug("Downloading image from {} to {}", imageUrl, cacheFile);

            try {
                connection = (HttpURLConnection) new URL(this.imageUrl).openConnection(Minecraft.getInstance().getProxy());
                connection.setDoInput(true);
                connection.setDoOutput(false);
                connection.setRequestProperty("Accept", "image/png");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.connect();

                if (connection.getResponseCode() != 200) {
                    LOGGER.warn("HTTP error {} downloading image from {}", connection.getResponseCode(), imageUrl);
                    return;
                }

                InputStream inputStream;
                if (cacheFile != null) {
                    FileUtils.copyInputStreamToFile(connection.getInputStream(), cacheFile);
                    inputStream = new FileInputStream(cacheFile);
                } else {
                    inputStream = connection.getInputStream();
                }

                try (inputStream) {
                    NativeImage image = NativeImage.read(inputStream);
                    uploadImage(image);
                }
            } catch (Exception e) {
                LOGGER.error("Failed to download image from {}", imageUrl, e);
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }, Util.backgroundExecutor());
    }

    private void uploadImage(NativeImage image) {
        if (onLoadCallback != null) {
            onLoadCallback.run();
        }

        Minecraft.getInstance().execute(() -> {
            textureUploaded = true;
            if (!RenderSystem.isOnRenderThread()) {
                RenderSystem.recordRenderCall(() -> doUpload(image));
            } else {
                doUpload(image);
            }
        });
    }

    private void doUpload(@NotNull NativeImage image) {
        TextureUtil.prepareImage(this.getId(), image.getWidth(), image.getHeight());
        image.upload(0, 0, 0, true);
    }
}
