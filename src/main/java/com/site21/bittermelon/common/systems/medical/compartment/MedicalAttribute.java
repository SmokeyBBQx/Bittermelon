package com.site21.bittermelon.common.systems.medical.compartment;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum MedicalAttribute implements StringRepresentable {
    HEALTH,
    FUNCTION,
    TREMOR,
    PAIN,
    BLEED,
    HEALING,
    BLOOD_REGENERATION,
    IMMUNITY,
    ELIMINATION,
    RESPIRATION,
    DIGESTION,
    NERVOUS,
    CIRCULATION,
    MOVEMENT,
    SIGHT,
    HEARING,
    TASTE,
    BITE,
    FLIGHT,
    MANIPULATION,
    BRAIN_VOLUNTARY_MOVEMENT,
    BRAIN_LANGUAGE,
    BRAIN_MOTOR_ABILITY,
    BRAIN_EMOTIONS,
    BRAIN_MEMORY,
    BRAIN_CONSCIOUSNESS,
    BRAIN_SIGHT,
    BRAIN_HEARING,
    BRAIN_SMELL,
    BRAIN_TASTE,
    BRAIN_TOUCH,
    BRAIN_LOCATION,
    BRAIN_VITALS;

    @Contract(pure = true)
    @Override
    public @NotNull String getSerializedName() {
        return name();
    }

    public static final EnumCodec<MedicalAttribute> CODEC = StringRepresentable.fromEnum(MedicalAttribute::values);

    public static final StreamCodec<ByteBuf, MedicalAttribute> STREAM_CODEC = ByteBufCodecs.idMapper(
            i -> MedicalAttribute.values()[i],
            MedicalAttribute::ordinal
    );
}
