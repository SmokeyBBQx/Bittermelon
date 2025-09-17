package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BitterPaintings {
    public static final DeferredRegister<PaintingVariant> PAINTING_VARIANTS =
            DeferredRegister.create(Registries.PAINTING_VARIANT, Bittermelon.MOD_ID);

    public static final Supplier<PaintingVariant> BERRY_POSTER = PAINTING_VARIANTS.register("berry_poster",
            () -> new PaintingVariant(32, 32, ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "berry_poster")));
}
