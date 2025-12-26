package com.site21.bittermelon.common.content.anatomies;

import com.mojang.serialization.Codec;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.medical.Anatomy;
import com.site21.bittermelon.common.systems.medical.blood.BloodType;
import com.site21.bittermelon.common.systems.medical.medicalstats.AnimalMedicalStats;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class HumanAnatomy extends Anatomy {
    @Override
    public Codec<? extends MedicalStats> type() {
        return MedicalStats.CODEC.xmap(
                stats -> (AnimalMedicalStats) stats,
                stats -> stats
        );
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, ? extends MedicalStats> streamCodec() {
        return MedicalStats.STREAM_CODEC.map(
                stats -> (AnimalMedicalStats) stats,
                stats -> stats
        );
    }

    @Override
    public MedicalStats build(BloodType bloodType, Character character) {
        return null;
    }

}
