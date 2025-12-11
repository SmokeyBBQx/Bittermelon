package com.site21.bittermelon.common.systems.medical.compartment.layer;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

public class SpriteUtil {
    public static int @NotNull [] getPixels(ResourceLocation resourceLocation) {
        try {
            InputStream stream = Minecraft.getInstance().getResourceManager().getResource(resourceLocation)
                    .orElseThrow(() -> new IOException("Resource not found: " + resourceLocation))
                    .open();
            NativeImage image = NativeImage.read(stream);
            return image.getPixels();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load texture: " + resourceLocation, e);
        }
    }
}
