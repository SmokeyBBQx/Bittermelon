package com.site21.bittermelon.datagen.property;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.common.systems.component.SubstanceContents;
import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record SubstanceColor(int defaultColor) implements ItemTintSource {
    public static final MapCodec<SubstanceColor> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    ExtraCodecs.RGB_COLOR_CODEC.fieldOf("default").forGetter(SubstanceColor::defaultColor))
            .apply(instance, SubstanceColor::new));

    public SubstanceColor() {
        this(0xFFFFFF);
    }

    @Override
    public int calculate(@NotNull ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity) {
        SubstanceContents substanceContents = stack.get(BitterDataComponents.SUBSTANCE_CONTENTS);
        return substanceContents != null ? ARGB.opaque(substanceContents.getColor()) : ARGB.opaque(defaultColor);
    }

    @Override
    public @NotNull MapCodec<? extends ItemTintSource> type() {
        return MAP_CODEC;
    }
}
