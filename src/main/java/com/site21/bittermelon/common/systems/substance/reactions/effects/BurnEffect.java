package com.site21.bittermelon.common.systems.substance.reactions.effects;

import com.site21.bittermelon.common.systems.substance.reactions.ReactionEffect;
import com.site21.bittermelon.common.systems.substance.reactions.Reactor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class BurnEffect implements ReactionEffect {
    @Override
    public void apply(Reactor reactor, Level level, BlockPos pos, int amount) {
        level.setBlock(pos, Blocks.FIRE.defaultBlockState(), 3);
    }
}
