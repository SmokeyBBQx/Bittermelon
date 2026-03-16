package com.site21.bittermelon.common.systems.chemistry.conditions;

import com.site21.bittermelon.common.systems.chemistry.ReactionCondition;
import com.site21.bittermelon.common.systems.chemistry.Reactor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class IgniteCondition implements ReactionCondition {
    @Override
    public boolean test(Reactor reactor, Level level, BlockPos pos) {
        BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();
        for (Direction dir : Direction.values()) {
            neighborPos.setWithOffset(pos, dir);
            if (level.getBlockState(neighborPos).is(Blocks.FIRE)) {
                return true;
            }
        }
        return false;
    }
}
