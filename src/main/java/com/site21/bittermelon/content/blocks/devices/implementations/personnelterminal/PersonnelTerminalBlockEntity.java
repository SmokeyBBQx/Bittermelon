package com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.PERSONNEL_TERMINAL_BLOCK_ENTITY;

public class PersonnelTerminalBlockEntity extends BlockEntity {
    public PersonnelTerminalBlockEntity(BlockPos pos, BlockState blockState) {
        super(PERSONNEL_TERMINAL_BLOCK_ENTITY.get(), pos, blockState);
    }

    public boolean canEdit() {
        return true;
    }

}
