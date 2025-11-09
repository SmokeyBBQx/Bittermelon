package com.site21.bittermelon.common.systems.medical.compartments;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public enum CompartmentTag {
    // CONDITIONS
    CONDITION,
    INJURY,
    STAB,
    SCAR,
    CUT,
    BRUISE,
    LACERATION,
    DISLOCATION,
    SAW_CUT,
    NECROTIC,
    INFECTION,
    INFLAMMATION,
    FOREIGN_SUBSTANCE,
    PAIN,
    BLEED,
    ARTERIAL_BLEED,
    MAJOR_ARTERIAL_BLEED,
    VENOUS_BLEED,
    CAPILLARY_BLEED,
    SCAB,
    BITE,
    FRACTURE,
    SCRATCH,
    TRAUMATIC_AMPUTATION,
    CARDIAC_ARREST,

    // FIRST-AID
    FIRST_AID,
    BANDAGE,
    CLAMP,
    OLD_BANDAGE,
    RETRACTOR,
    STITCHES,

    // ANATOMY
    BLOOD_VESSEL,
    MAJOR_ARTERY,
    ARTERY,
    VEIN,
    CAPILLARY,
    HARD_TISSUE,
    SOFT_TISSUE,
    MAJOR_BODY_PART,
    BODY_PART, JOINT,

    // MISC
    DOES_BLEED;

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
