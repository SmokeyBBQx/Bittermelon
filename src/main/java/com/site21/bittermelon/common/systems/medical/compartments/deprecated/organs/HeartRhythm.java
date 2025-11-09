package com.site21.bittermelon.common.systems.medical.compartments.deprecated.organs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;

import java.util.function.IntFunction;

public enum HeartRhythm {
    SINUS_RHYTHM(0),
    ATRIAL_FIBRILLATION(1),
    ATRIAL_FLUTTER(2),
    VENTRICULAR_FIBRILLATION(3),
    BRADYCARDIA(4),
    TACHYCARDIA(5),
    ASYSTOLE(6),
    PEA(7);

    public static final IntFunction<HeartRhythm> BY_ID = ByIdMap.continuous(
            HeartRhythm::getId,
            HeartRhythm.values(),
            ByIdMap.OutOfBoundsStrategy.ZERO
    );

    public static final Codec<HeartRhythm> CODEC = Codec.INT.flatXmap(
            id -> DataResult.success(BY_ID.apply(id)),
            heartRhythm -> DataResult.success(heartRhythm.getId())
    );

    public static final StreamCodec<ByteBuf, HeartRhythm> STREAM_CODEC = ByteBufCodecs.idMapper(
            BY_ID,
            HeartRhythm::getId
    );

    private final int id;

    HeartRhythm(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}