package com.site21.bittermelon.common.content.items.base;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum ItemSize implements StringRepresentable {
    TINY("Tiny"),
    SMALL("Small"),
    NORMAL("Normal"),
    BULKY("Bulky"),
    HUGE("Huge"),
    GIGANTIC("Gigantic");

    public final String description;

    ItemSize(String description) {
        this.description = description;
    }

    @Override
    public @NotNull String getSerializedName() {
        return description.toLowerCase();
    }

    public static final EnumCodec<ItemSize> CODEC = StringRepresentable.fromEnum(ItemSize::values);

    public static final StreamCodec<ByteBuf, ItemSize> STREAM_CODEC = ByteBufCodecs.idMapper(
            i -> ItemSize.values()[i],
            ItemSize::ordinal
    );
}
