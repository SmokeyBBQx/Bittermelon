package com.site21.bittermelon.common.content.items.scps;

import com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.LargeSlidingDoorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
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
        Player player = context.getPlayer();

        System.out.println("SCP-005 used on block: " + state.getBlock().getClass().getName());

        // Check if it's a large sliding door
        if (state.getBlock() instanceof LargeSlidingDoorBlock slidingDoorBlock) {
            System.out.println("Block is a large sliding door!");
            if (!level.isClientSide) {
                System.out.println("Server side - toggling large sliding door");

                // Find the master block if this isn't one
                BlockPos masterPos = pos;
                BlockState masterState = state;

                if (!state.getValue(LargeSlidingDoorBlock.MASTER)) {
                    masterPos = slidingDoorBlock.findMasterBlock(level, pos);
                    if (masterPos == null) {
                        System.out.println("Could not find master block!");
                        return InteractionResult.FAIL;
                    }
                    masterState = level.getBlockState(masterPos);
                }

                // Toggle the door (opposite of current state)
                boolean shouldOpen = masterState.getValue(LargeSlidingDoorBlock.STATE) == LargeSlidingDoorBlock.State.CLOSED;
                slidingDoorBlock.handleMoving(masterState, level, masterPos, shouldOpen);
            }

            return level.isClientSide ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
        }

        // Check if the clicked block is a regular door
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

                // Play the door sound manually
                BlockSetType blockSetType = doorBlock.type();
                if (!isOpen) {
                    // Opening sound
                    level.playSound(null, pos, blockSetType.doorOpen(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
                } else {
                    // Closing sound
                    level.playSound(null, pos, blockSetType.doorClose(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
                }
            }

            return level.isClientSide ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
        }

        System.out.println("Block is NOT a door");
        return InteractionResult.PASS;
    }
}