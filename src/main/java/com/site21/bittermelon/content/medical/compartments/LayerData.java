package com.site21.bittermelon.content.medical.compartments;

import net.minecraft.resources.ResourceLocation;

public class LayerData {
    private final String name;
    private final ResourceLocation backgroundTexture;
    private final int width;
    private final int height;

    public LayerData(ResourceLocation backgroundTexture, String name, int width, int height) {
        this.backgroundTexture = backgroundTexture;
        this.name = name;
        this.width = width;
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public ResourceLocation getBackgroundTexture() {
        return backgroundTexture;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
