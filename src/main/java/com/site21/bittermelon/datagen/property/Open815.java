package com.site21.bittermelon.datagen.property;

import com.mojang.serialization.MapCodec;
import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record Open815() implements ConditionalItemModelProperty {
    public static final MapCodec<Open815> MAP_CODEC = MapCodec.unit(new Open815());

    @Override
    public @NotNull MapCodec<? extends ConditionalItemModelProperty> type() {
        return MAP_CODEC;
    }

    @Override
    public boolean get(@NotNull ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, ItemDisplayContext displayContext) {
        if (level == null) return false;
        Long openedAt = stack.get(BitterDataComponents.OPEN_TIME.get());
        if (openedAt == null) return false;
        return (level.getGameTime() - openedAt) < 15 * 20L;
    }
}