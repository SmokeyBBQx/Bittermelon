package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class BitterBlockTags {
    public static final TagKey<Block> PASSES_ATMOS = TagKey.create(
            Registries.BLOCK,
            ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "passes_atmos")
    );
}
