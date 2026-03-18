package com.site21.bittermelon.common.systems.chemistry.conditions;

import com.site21.bittermelon.common.systems.chemistry.ReactionCondition;
import com.site21.bittermelon.common.systems.chemistry.ReactionConditionType;
import com.site21.bittermelon.common.systems.substance.SubstanceContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import static com.site21.bittermelon.init.custom.ReactionConditions.IGNITE;

public class IgniteCondition implements ReactionCondition {

    @Override
    public ReactionConditionType<?> getType() {
        return IGNITE.get();
    }

    @Override
    public boolean test(SubstanceContainer substanceContainer, Level level, BlockPos pos) {
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
