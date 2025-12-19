package com.site21.bittermelon.common.systems.medical;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.medical.blood.BloodType;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.ANATOMY_REGISTRY;
import static com.site21.bittermelon.init.neoforge.BitterRegistries.ANATOMY_REGISTRY_KEY;

public abstract class Anatomy {
    public static final Codec<Holder<Anatomy>> CODEC = ANATOMY_REGISTRY.holderByNameCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Anatomy>> STREAM_CODEC = ByteBufCodecs.holderRegistry(ANATOMY_REGISTRY_KEY);

    public abstract MapCodec<? extends Anatomy> type();

    public abstract StreamCodec<? super RegistryFriendlyByteBuf, ? extends Anatomy> streamCodec();

    public abstract MedicalStats build(BloodType bloodType, Character character);

    public DataComponentMap components() {
        return DataComponentMap.EMPTY;
    }
}
