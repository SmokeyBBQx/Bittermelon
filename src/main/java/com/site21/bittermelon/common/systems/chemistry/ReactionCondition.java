package com.site21.bittermelon.common.systems.chemistry;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.REACTION_CONDITION_TYPE;

public interface ReactionCondition {
    Codec<ReactionCondition> CODEC = REACTION_CONDITION_TYPE.byNameCodec()
            .dispatch(ReactionCondition::getType, ReactionConditionType::codec);

    ReactionConditionType<?> getType();

    boolean test(Reactor reactor, Level level, BlockPos pos);
}
