package com.site21.bittermelon.blocks;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public interface IBlockHealth {
    default IntegerProperty getHealthProperty() {
        return IntegerProperty.create("health", 1, getMaxHealth());
    }

    int getMaxHealth();

    default void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(getHealthProperty());
    }

    default BlockState getDefaultState(BlockState state) {
        return state.setValue(getHealthProperty(), getMaxHealth());
    }


}
