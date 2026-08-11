package com.site21.bittermelon.common.systems.medical.legacy.compartment;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public enum CompartmentTag {
    SOFT_TISSUE,
    HARD_TISSUE,
    MAJOR_BODY_PART;

    public static final Codec<CompartmentTag> CODEC = Codec.stringResolver(CompartmentTag::name, name -> {
        try {
            return CompartmentTag.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    });

    public static final StreamCodec<ByteBuf, CompartmentTag> STREAM_CODEC = ByteBufCodecs.idMapper(
            i -> CompartmentTag.values()[i],
            CompartmentTag::ordinal
    );
}
