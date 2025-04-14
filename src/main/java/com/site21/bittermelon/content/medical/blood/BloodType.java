package com.site21.bittermelon.content.medical.blood;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum BloodType {
    A_PLUS(0),
    A_MINUS(1),
    B_PLUS(2),
    B_MINUS(3),
    O_PLUS(4),
    O_MINUS(5),
    AB_PLUS(6),
    AB_MINUS(7);

    public static final IntFunction<BloodType> BY_ID = ByIdMap.continuous(
            BloodType::getId,
            BloodType.values(),
            ByIdMap.OutOfBoundsStrategy.ZERO
    );

    public static final Codec<BloodType> CODEC = Codec.INT.flatXmap(
            id -> DataResult.success(BY_ID.apply(id)),
            bloodType -> DataResult.success(bloodType.getId())
    );

    public static final StreamCodec<ByteBuf, BloodType> STREAM_CODEC = ByteBufCodecs.idMapper(
            BY_ID,
            BloodType::getId
    );

    private final int id;

    BloodType(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}