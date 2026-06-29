package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public class BitterFluidTags {
    public static final TagKey<Fluid> SUBSTANCE = create("substance");

    private static TagKey<Fluid> create(String name) {
        return TagKey.create(Registries.FLUID, Bittermelon.identifier(name));
    }

    public static TagKey<Fluid> create(Identifier name) {
        return TagKey.create(Registries.FLUID, name);
    }
}
