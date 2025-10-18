package com.site21.bittermelon.common.systems.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.common.systems.substance.SubstanceStack;
import com.site21.bittermelon.util.ColorUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record SubstanceContents(List<SubstanceStack> substances) {
    public static final SubstanceContents EMPTY = new SubstanceContents(List.of());
    public static final Codec<SubstanceContents> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, SubstanceContents> STREAM_CODEC;

    public SubstanceContents(List<SubstanceStack> substances) {
        this.substances = new ArrayList<>(substances);
    }

    public float getTotalVolume() {
        return substances.stream()
                .map(SubstanceStack::getVolume)
                .reduce(0f, Float::sum);
    }

    public float getTotalAmount() {
        return substances.stream()
                .map(SubstanceStack::getAmount)
                .reduce(0f, Float::sum);
    }

    public int getColor() {
        Map<Integer, Float> colors = new HashMap<>();
        for (SubstanceStack stack : substances) {
            colors.put(stack.getSubstance().getColor(), stack.getAmount());
        }
        return ColorUtil.mixColors(colors);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else {
            return other instanceof SubstanceContents(List<SubstanceStack> substances1)
                    && SubstanceStack.listMatches(this.substances, substances1);
        }
    }

    @Override
    public int hashCode() {
        return SubstanceStack.hashStackList(this.substances);
    }

    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "SubstanceContainerContents" + this.substances;
    }

    @Contract(value = " -> new", pure = true)
    public @NotNull Mutable toMutable() {
        return new Mutable(this);
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                SubstanceStack.CODEC.listOf().fieldOf("substances").forGetter(SubstanceContents::substances)
        ).apply(instance, SubstanceContents::new));

        STREAM_CODEC = StreamCodec.composite(
                SubstanceStack.STREAM_CODEC.apply(ByteBufCodecs.list()),
                SubstanceContents::substances,
                SubstanceContents::new
        );
    }

    public static class Mutable {
        public final List<SubstanceStack> substances;

        @Contract(pure = true)
        public Mutable(@NotNull SubstanceContents substanceContents) {
            this.substances = new ArrayList<>(substanceContents.substances);
        }

        public void updateSubstance(SubstanceStack stack) {
            for (SubstanceStack substance : this.substances) {
                if (substance.canMergeWith(stack)) {
                    substance.modifyAmount(stack.getAmount());
                    return;
                }
            }
            this.substances.add(stack.copy());
        }

        public void setSubstances(@NotNull List<SubstanceStack> substances) {
            this.substances.clear();
            substances.stream()
                    .filter(s -> s.getAmount() > 0)
                    .map(SubstanceStack::copy)
                    .forEach(this.substances::add);
        }

        public SubstanceContents toImmutable() {
            return new SubstanceContents(List.copyOf(this.substances));
        }
    }
}
