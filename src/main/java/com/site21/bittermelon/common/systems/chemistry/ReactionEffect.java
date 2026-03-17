package com.site21.bittermelon.common.systems.chemistry;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.REACTION_EFFECT_TYPE;

public interface ReactionEffect {
    Codec<ReactionEffect> CODEC = REACTION_EFFECT_TYPE.byNameCodec()
            .dispatch(ReactionEffect::getType, ReactionEffectType::codec);

    ReactionEffectType<?> getType();

    void apply(Reactor reactor, Level level, BlockPos pos, int amount);
}
