package com.site21.bittermelon.common.systems.medical.factory;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum Anatomy implements StringRepresentable {
    HUMAN(new HumanFactoryNew());

    private final AnatomyFactory factory;

    Anatomy(AnatomyFactory factory) {
        this.factory = factory;
    }

    public AnatomyFactory getFactory() {
        return factory;
    }

    @Contract(pure = true)
    @Override
    public @NotNull String getSerializedName() {
        return name().toLowerCase();
    }

    public static final EnumCodec<Anatomy> CODEC = StringRepresentable.fromEnum(Anatomy::values);

    public static final StreamCodec<ByteBuf, Anatomy> STREAM_CODEC = ByteBufCodecs.idMapper(
            i -> Anatomy.values()[i],
            Anatomy::ordinal
    );
}
