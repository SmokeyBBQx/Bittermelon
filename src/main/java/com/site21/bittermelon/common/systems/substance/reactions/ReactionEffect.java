package com.site21.bittermelon.common.systems.substance.reactions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface ReactionEffect {
    void apply(Reactor reactor, Level level, BlockPos pos, int amount);
}
