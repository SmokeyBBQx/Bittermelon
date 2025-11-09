package com.site21.bittermelon.common.systems.medical.compartments;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record LayerData(ResourceLocation backgroundTexture, String name, int width, int height) {
    @Contract("_, _ -> new")
    public static @NotNull LayerData defaultSize(ResourceLocation backgroundTexture, String name) {
        return new LayerData(backgroundTexture, name, 200, 200);
    }
}
