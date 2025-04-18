package com.site21.bittermelon.content.items.itemcontainers.client;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public record SlotData(int topLeftSlot, List<Integer> slots, ItemStack stack) {
    public static final Codec<SlotData> CODEC = RecordCodecBuilder.create(
            slot -> slot.group(
                            Codec.intRange(0, 255).fieldOf("topLeftSlot").forGetter(SlotData::topLeftSlot),
                            Codec.list(Codec.intRange(0, 255)).fieldOf("slots").forGetter(SlotData::slots),
                            ItemStack.CODEC.fieldOf("item").forGetter(SlotData::stack)
                    )
                    .apply(slot, SlotData::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, SlotData> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull SlotData decode(@NotNull RegistryFriendlyByteBuf buf) {
            int topLeftSlot = ByteBufCodecs.VAR_INT.decode(buf);
            List<Integer> slots = ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.VAR_INT).decode(buf);
            ItemStack stack = ItemStack.STREAM_CODEC.decode(buf);
            return new SlotData(topLeftSlot, slots, stack);
        }

        @Override
        public void encode(@NotNull RegistryFriendlyByteBuf buf, SlotData value) {
            ByteBufCodecs.VAR_INT.encode(buf, value.topLeftSlot());
            ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.VAR_INT).encode(buf, new ArrayList<>(value.slots()));
            ItemStack.STREAM_CODEC.encode(buf, value.stack());
        }
    };

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SlotData slot)) {
            return false;
        }
        return topLeftSlot == slot.topLeftSlot
                && slots.equals(slot.slots)
                && ItemStack.matches(stack, slot.stack);
    }


    @Override
    public int hashCode() {
        int result = topLeftSlot;
        result = 31 * result + slots.hashCode();
        result = 31 * result + ItemStack.hashItemAndComponents(stack);
        return result;
    }

    @Override
    public String toString() {
        return "SlotData{topLeftSlot=" + topLeftSlot +
                ", slots=" + slots +
                ", stack=" + stack + "}";
    }


}
