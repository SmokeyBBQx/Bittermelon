package com.site21.bittermelon.common.systems.medical.legacy.blood;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.common.systems.medical.legacy.drug.DrugInstance;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

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
}
