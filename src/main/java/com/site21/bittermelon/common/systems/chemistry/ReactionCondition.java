package com.site21.bittermelon.common.systems.chemistry;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface ReactionCondition {
    boolean test(Reactor reactor, Level level, BlockPos pos);
}
