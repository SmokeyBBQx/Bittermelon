package com.site21.bittermelon.datagen.property;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.core.component.DataComponents.BASE_COLOR;

public record BaseColor(DyeColor defaultColor) implements ItemTintSource {
    public static final MapCodec<BaseColor> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    DyeColor.CODEC.fieldOf("default").forGetter(BaseColor::defaultColor))
            .apply(instance, BaseColor::new));

    @Override
    public int calculate(@NotNull ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity) {
        return stack.getOrDefault(BASE_COLOR, defaultColor).getTextColor();
    }

    @Contract(pure = true)
    @Override
    public @NotNull MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }
}
