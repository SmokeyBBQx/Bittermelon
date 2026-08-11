package com.site21.bittermelon.common.systems.medical.legacy.blood;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public enum BloodType implements StringRepresentable {
    O_MINUS(new BloodType[]{}),
    O_PLUS(new BloodType[]{O_MINUS}),
    A_MINUS(new BloodType[]{O_MINUS}),
    A_PLUS(new BloodType[]{A_MINUS, O_PLUS, O_MINUS}),
    B_MINUS(new BloodType[]{O_MINUS}),
    B_PLUS(new BloodType[]{B_MINUS, O_PLUS, O_MINUS}),
    AB_MINUS(new BloodType[]{A_MINUS, B_MINUS, O_MINUS}),
    AB_PLUS(new BloodType[]{O_PLUS, O_PLUS, A_MINUS, A_PLUS, B_MINUS, B_PLUS, AB_MINUS});


    private final BloodType[] compatibleBloodTypes;

    BloodType(BloodType[] compatibleBloodTypes) {
        this.compatibleBloodTypes = compatibleBloodTypes;
    }

    public BloodType[] getCompatibleBloodTypes() {
        return compatibleBloodTypes;
    }

    public boolean isBloodTypeCompatible(BloodType bloodType) {
        return Arrays.asList(compatibleBloodTypes).contains(bloodType);
    }

    public static final EnumCodec<BloodType> CODEC = StringRepresentable.fromEnum(BloodType::values);

    public static final StreamCodec<ByteBuf, BloodType> STREAM_CODEC = ByteBufCodecs.idMapper(
            i -> BloodType.values()[i],
            BloodType::ordinal
    );

    @Contract(pure = true)
    @Override
    public @NotNull String getSerializedName() {
        return name();
    }
}