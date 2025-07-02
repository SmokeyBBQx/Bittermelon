package com.site21.bittermelon.content.medical.compartments;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public enum MedicalAttribute {
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

    public static final Codec<MedicalAttribute> CODEC = Codec.stringResolver(MedicalAttribute::name, name -> {
        try {
            return MedicalAttribute.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    });

    public static final StreamCodec<ByteBuf, MedicalAttribute> STREAM_CODEC = ByteBufCodecs.idMapper(
            i -> MedicalAttribute.values()[i],
            MedicalAttribute::ordinal
    );

}
