package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.*;
import org.jetbrains.annotations.NotNull;

public class BitterBiomes {
    public static final ResourceKey<Biome> FACILITY = key("facility");
    public static final ResourceKey<Biome> TUNNEL = key("tunnel");

    private static @NotNull ResourceKey<Biome> key(final String name) {
        return ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, name));
    }
}
