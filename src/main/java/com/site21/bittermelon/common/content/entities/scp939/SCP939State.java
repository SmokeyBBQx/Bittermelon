package com.site21.bittermelon.common.content.entities.scp939;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum SCP939State {
    IDLE,
    LISTENING,
    ATTACKING,
    HURT,
    DEAD;

    public static final IntFunction<SCP939State> BY_ID = ByIdMap.continuous(SCP939State::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, SCP939State> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, SCP939State::ordinal);
}
