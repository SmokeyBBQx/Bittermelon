package com.site21.bittermelon.common.content.items.itemcontainers.client;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;
public record ItemContainerContents(List<SlotData> items) {
    public static final ItemContainerContents EMPTY = new ItemContainerContents(List.of());
    public static final Codec<ItemContainerContents> CODEC = SlotData.CODEC.listOf().xmap(
            ItemContainerContents::new,
            itemContainerContents -> itemContainerContents.items
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemContainerContents> STREAM_CODEC = SlotData.STREAM_CODEC
            .apply(ByteBufCodecs.list())
            .map(ItemContainerContents::new, itemContainerContents -> itemContainerContents.items);

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else {
            return other instanceof ItemContainerContents itemContainerContents && listMatches(this.items, itemContainerContents.items);
        }
    }

    public boolean listMatches(List<SlotData> list, List<SlotData> other) {
        if (list.size() != other.size()) {
            return false;
        } else {
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).equals(other.get(i))) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int i = 0;

        for (SlotData slot : items) {
            i = i * 31 + slot.hashCode();
        }

        return i;
    }

    @Override
    public String toString() {
        return "ItemContainerContents" + this.items;
    }


}
