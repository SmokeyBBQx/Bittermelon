package com.site21.bittermelon.content.medical.blood;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.content.medical.drugs.DrugInstance;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.*;

public record BloodData(BloodType bloodType, List<DrugInstance> drugs) {
    public static final Codec<BloodData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BloodType.CODEC.fieldOf("bloodType").forGetter(BloodData::bloodType),
            DrugInstance.CODEC.listOf().fieldOf("drugs").forGetter(BloodData::drugs)
    ).apply(instance, BloodData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, BloodData> STREAM_CODEC = StreamCodec.composite(
            BloodType.STREAM_CODEC,
            BloodData::bloodType,
            DrugInstance.STREAM_CODEC.apply(ByteBufCodecs.list()),
            BloodData::drugs,
            BloodData::new
    );

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof BloodData(BloodType otherType, List<DrugInstance> otherDrugs)) {
            return otherType.equals(bloodType) && otherDrugs.equals(drugs);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(bloodType, drugs);
    }
}
