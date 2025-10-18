package com.site21.bittermelon.datagen.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.site21.bittermelon.common.content.items.substance.pill.PillShape;
import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record StackPillShape() implements SelectItemModelProperty<PillShape> {
    public static final SelectItemModelProperty.Type<StackPillShape, PillShape> TYPE = SelectItemModelProperty.Type.create(
            MapCodec.unit(new StackPillShape()),
            PillShape.CODEC
    );

    @Override
    public @Nullable PillShape get(@NotNull ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed, @NotNull ItemDisplayContext displayContext) {
        return stack.get(BitterDataComponents.PILL_SHAPE);
    }

    @Override
    public @NotNull Codec<PillShape> valueCodec() {
        return PillShape.CODEC;
    }

    @Override
    public @NotNull Type<? extends SelectItemModelProperty<PillShape>, PillShape> type() {
        return TYPE;
    }
}
