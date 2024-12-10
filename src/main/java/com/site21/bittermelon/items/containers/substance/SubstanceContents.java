package com.site21.bittermelon.items.containers.substance;

import com.mojang.serialization.Codec;
import com.site21.bittermelon.substance.SubstanceStack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;

import java.util.ArrayList;
import java.util.List;

public final class SubstanceContents {
    public static final SubstanceContents EMPTY = new SubstanceContents(List.of());
    public static final Codec<SubstanceContents> CODEC = SubstanceStack.CODEC.listOf().xmap(SubstanceContents::new, container -> container.substances);
    public static final StreamCodec<RegistryFriendlyByteBuf, SubstanceContents> STREAM_CODEC = SubstanceStack.STREAM_CODEC
            .apply(ByteBufCodecs.list())
            .map(SubstanceContents::new, container -> container.substances);
    final List<SubstanceStack> substances;

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

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else {
            return other instanceof SubstanceContents substanceContents && SubstanceStack.listMatches(this.substances, substanceContents.substances);
        }
    }

    @Override
    public int hashCode() {
        return SubstanceStack.hashStackList(this.substances);
    }

    @Override
    public String toString() {
        return "SubstanceContainerContents" + this.substances;
    }

    public Mutable toMutable() {
        return new Mutable(this);
    }

    public static class Mutable {
        final List<SubstanceStack> substances;

        public Mutable(SubstanceContents substanceContents) {
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

        public void setSubstances(List<SubstanceStack> substances) {
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
