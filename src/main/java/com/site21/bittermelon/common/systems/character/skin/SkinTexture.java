package com.site21.bittermelon.common.systems.character.skin;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SkinTexture extends SimpleTexture {
    private static final Logger LOGGER = LogManager.getLogger();

    private final File cacheFile;
    private final String imageUrl;
    private final Runnable onLoadCallback;
    private final SkinDownloader downloader;
    private boolean downloaded;

    public SkinTexture(Identifier location, File cacheFile, String imageUrl, Runnable onLoadCallback) {
        super(location);
        this.cacheFile = cacheFile;
        this.imageUrl = imageUrl;
        this.onLoadCallback = onLoadCallback;
        downloader = new SkinDownloader();
    }

    @Override
    public @NotNull TextureContents loadContents(@NotNull ResourceManager resourceManager) {
        // Try cache first
        if (cacheFile != null && cacheFile.isFile()) {
            try (FileInputStream fis = new FileInputStream(cacheFile)) {
                downloaded = true;
                return new TextureContents(NativeImage.read(fis), null);
            } catch (IOException e) {
                LOGGER.warn("Failed to load cached skin, will download", e);
            }
        }

        if (!downloaded) {
            downloader.download(imageUrl, cacheFile).thenAcceptAsync(this::handleDownload, Minecraft.getInstance());
        }

        return TextureContents.createMissing();
    }

    private void handleDownload(NativeImage image) {
        if (image != null) {
            try {
                downloaded = true;
                apply(new TextureContents(image, null));
                if (onLoadCallback != null) {
                    onLoadCallback.run();
                }
            } catch (Exception e) {
                LOGGER.error("Failed to upload skin texture", e);
                image.close();
            }
        }
    }
}