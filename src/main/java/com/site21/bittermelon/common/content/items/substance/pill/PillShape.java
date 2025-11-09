package com.site21.bittermelon.common.content.items.substance.pill;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum PillShape implements StringRepresentable {
    ROUND("round"),
    OVAL("oval"),
    CAPSULE("capsule");

    private final String name;

    PillShape(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    public static final EnumCodec<PillShape> CODEC = StringRepresentable.fromEnum(PillShape::values);

    public static final StreamCodec<ByteBuf, PillShape> STREAM_CODEC = ByteBufCodecs.idMapper(
            i -> PillShape.values()[i],
            PillShape::ordinal
    );
}
