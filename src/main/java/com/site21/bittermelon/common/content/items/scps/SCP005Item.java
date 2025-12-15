package com.site21.bittermelon.common.content.items.scps;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class SCP005Item extends Item {
    public SCP005Item(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        System.out.println("SCP-005 used on block: " + state.getBlock().getClass().getName());

        // Check if the clicked block is a door
        if (state.getBlock() instanceof DoorBlock doorBlock) {
            System.out.println("Block is a door!");
            if (!level.isClientSide) {
                System.out.println("Server side - toggling door");
                // Get the current open state
                boolean isOpen = state.getValue(BlockStateProperties.OPEN);
                System.out.println("Door is currently open: " + isOpen);

                // Create the new state with toggled open value
                BlockState newState = state.setValue(BlockStateProperties.OPEN, !isOpen);

                // If the door has a POWERED property (like iron doors), set it
                if (newState.hasProperty(BlockStateProperties.POWERED)) {
                    newState = newState.setValue(BlockStateProperties.POWERED, !isOpen);
                    System.out.println("Set powered state to: " + !isOpen);
                }

                // Update the block state with proper flags
                level.setBlock(pos, newState, 3);

            }

            return level.isClientSide ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
        }

        System.out.println("Block is NOT a door");
        return InteractionResult.PASS;
    }
}