package com.site21.bittermelon.common.systems.medical.anatomy;

import com.mojang.serialization.Codec;
import com.site21.bittermelon.common.systems.medical.anatomy.factory.AnatomyFactory;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.ANATOMY_REGISTRY;
import static com.site21.bittermelon.init.neoforge.BitterRegistries.ANATOMY_REGISTRY_KEY;

public abstract class Anatomy {
    public static final Codec<Holder<Anatomy>> CODEC = ANATOMY_REGISTRY.holderByNameCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Anatomy>> STREAM_CODEC = ByteBufCodecs.holderRegistry(ANATOMY_REGISTRY_KEY);

    public final AnatomyFactory factory;

    public Anatomy(AnatomyFactory factory) {
        this.factory = factory;
    }

    public MedicalStats toInstance(Entity entity) {
        return factory.build();
    }


    public void tick(LivingEntity entity, MedicalStats medicalStats) {

    }


    public DataComponentMap components() {
        return DataComponentMap.EMPTY;
    }
}
