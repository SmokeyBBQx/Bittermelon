package com.site21.bittermelon.init.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.chemistry.ReactionEffectType;
import com.site21.bittermelon.common.systems.chemistry.effects.*;
import com.site21.bittermelon.common.systems.substance.Substance;
import com.site21.bittermelon.init.neoforge.BitterRegistries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ReactionEffects {
    public static final DeferredRegister<ReactionEffectType<?>> REACTION_EFFECT_TYPES =
            DeferredRegister.create(BitterRegistries.REACTION_EFFECT_TYPE_KEY, Bittermelon.MOD_ID);

    public static final DeferredHolder<ReactionEffectType<?>, ReactionEffectType<ExplosionEffect>> EXPLOSION =
            REACTION_EFFECT_TYPES.register("explosion", () -> new ReactionEffectType<>(MapCodec.unit(new ExplosionEffect())));

    public static final DeferredHolder<ReactionEffectType<?>, ReactionEffectType<BurnEffect>> BURN =
            REACTION_EFFECT_TYPES.register("burn", () -> new ReactionEffectType<>(MapCodec.unit(new BurnEffect())));

    public static final DeferredHolder<ReactionEffectType<?>, ReactionEffectType<SynthesisEffect>> SYNTHESIS =
            REACTION_EFFECT_TYPES.register("synthesis", () -> new ReactionEffectType<>(
                    RecordCodecBuilder.mapCodec(instance -> instance.group(
                            Codec.unboundedMap(Substance.CODEC, Codec.INT)
                                    .fieldOf("products")
                                    .forGetter(SynthesisEffect::getProducts)
                    ).apply(instance, SynthesisEffect::new))
            ));

    public static final DeferredHolder<ReactionEffectType<?>, ReactionEffectType<SpawnEntityEffect>> SPAWN_ENTITY =
            REACTION_EFFECT_TYPES.register("spawn_entity", () -> new ReactionEffectType<>(
                    RecordCodecBuilder.mapCodec(instance -> instance.group(
                            BuiltInRegistries.ENTITY_TYPE.byNameCodec()
                                    .fieldOf("entity")
                                    .forGetter(SpawnEntityEffect::entityType),
                            Codec.INT
                                    .fieldOf("max_amount")
                                    .forGetter(SpawnEntityEffect::maxAmount)
                    ).apply(instance, SpawnEntityEffect::new))
            ));

    public static final DeferredHolder<ReactionEffectType<?>, ReactionEffectType<SetBlockEffect>> SET_BLOCK =
            REACTION_EFFECT_TYPES.register("set_block", () -> new ReactionEffectType<>(
                    RecordCodecBuilder.mapCodec(instance -> instance.group(
                            BuiltInRegistries.BLOCK.byNameCodec()
                                    .fieldOf("block")
                                    .forGetter(SetBlockEffect::block)
                    ).apply(instance, SetBlockEffect::new))
            ));

    public static final DeferredHolder<ReactionEffectType<?>, ReactionEffectType<ChanceSetBlockEffect>> CHANCE_SET_BLOCK =
            REACTION_EFFECT_TYPES.register("chance_set_block", () -> new ReactionEffectType<>(
                    RecordCodecBuilder.mapCodec(instance -> instance.group(
                            BuiltInRegistries.BLOCK.byNameCodec()
                                    .fieldOf("block")
                                    .forGetter(ChanceSetBlockEffect::block),
                            Codec.FLOAT
                                    .fieldOf("chance")
                                    .forGetter(ChanceSetBlockEffect::chance)
                    ).apply(instance, ChanceSetBlockEffect::new))
            ));
}
