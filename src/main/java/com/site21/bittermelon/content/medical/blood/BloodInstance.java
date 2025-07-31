package com.site21.bittermelon.content.medical.blood;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.content.medical.drugs.DrugInstance;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record BloodInstance(float volume, BloodData data) {
    @Contract("_, _, _ -> new")
    public static @NotNull BloodInstance of(float volume, BloodType bloodType, List<DrugInstance> drugs) {
        return new BloodInstance(volume, new BloodData(bloodType, drugs));
    }

    public static final Codec<BloodInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("volume").forGetter(BloodInstance::volume),
            BloodData.CODEC.fieldOf("data").forGetter(BloodInstance::data)
    ).apply(instance, BloodInstance::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, BloodInstance> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            BloodInstance::volume,
            BloodData.STREAM_CODEC,
            BloodInstance::data,
            BloodInstance::new
    );
}
