package com.site21.bittermelon.common.systems.medical.wound;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.UUID;

public record Wound(UUID id, int u, int v, long timeAdded, int seed) {
    public static final StreamCodec<ByteBuf, Wound> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            Wound::id,
            ByteBufCodecs.INT,
            Wound::u,
            ByteBufCodecs.INT,
            Wound::v,
            ByteBufCodecs.LONG,
            Wound::timeAdded,
            ByteBufCodecs.INT,
            Wound::seed,
            Wound::new
    );

    public Wound(UUID id, int u, int v, long timeAdded) {
        this(id, u, v, timeAdded, (int) (id.getMostSignificantBits() ^ id.getLeastSignificantBits()));
    }
}
