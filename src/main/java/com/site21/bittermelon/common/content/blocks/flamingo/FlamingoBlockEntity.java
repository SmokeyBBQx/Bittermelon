package com.site21.bittermelon.common.content.blocks.flamingo;

import com.site21.bittermelon.init.neoforge.BitterBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FlamingoBlockEntity extends BlockEntity {

    public FlamingoBlockEntity(BlockPos pos, BlockState blockState) {
        super(BitterBlockEntities.PLASTIC_FLAMINGO_BLOCK_ENTITY.get(), pos, blockState);
    }
}
