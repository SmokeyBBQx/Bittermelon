package com.site21.bittermelon.content.character.skin;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

@OnlyIn(Dist.CLIENT)
public class SkinManager {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final File CACHE_DIR = new File(Minecraft.getInstance().gameDirectory, "cache/skins");

    public static @NotNull ResourceLocation loadSkin(String url, String imageName) {
        return loadSkin(url, imageName, null);
    }

    public static @NotNull ResourceLocation loadSkin(@NotNull String url, String imageName, Runnable callback) {
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "skins/" + imageName);

        File cacheFile = new File(CACHE_DIR, imageName + ".png");
        SkinTexture texture = new SkinTexture(location, cacheFile, url, callback);

        Minecraft.getInstance().getTextureManager().register(location, texture);
        return location;
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
}
