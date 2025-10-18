package com.site21.bittermelon.datagen.property;

import com.mojang.serialization.MapCodec;
import com.site21.bittermelon.common.systems.component.SubstanceContents;
import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record SubstanceVolume() implements RangeSelectItemModelProperty {
    public static final MapCodec<SubstanceVolume> MAP_CODEC = MapCodec.unit(new SubstanceVolume());

    @Override
    public float get(@NotNull ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        return stack.getOrDefault(BitterDataComponents.SUBSTANCE_CONTENTS, SubstanceContents.EMPTY).getTotalVolume();
    }

    @Override
    public @NotNull MapCodec<? extends RangeSelectItemModelProperty> type() {
        return MAP_CODEC;
    }
}
