package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class BitterItemTags {
    public static final TagKey<Item> BASEBALL = TagKey.create(
            Registries.ITEM,
            ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "baseball")
    );
}
