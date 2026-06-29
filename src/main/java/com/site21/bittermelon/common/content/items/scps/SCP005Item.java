package com.site21.bittermelon.common.content.items.scps;

import com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.LargeSlidingDoorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
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

        if (state.getBlock() instanceof DoorBlock doorBlock) {
            if (!level.isClientSide()) {
                boolean isOpen = state.getValue(BlockStateProperties.OPEN);
                level.setBlock(pos, state.setValue(BlockStateProperties.OPEN, !isOpen)
                        .setValue(BlockStateProperties.POWERED, !isOpen), 3);
                level.playSound(null, pos,
                        isOpen ? doorBlock.type().doorClose() : doorBlock.type().doorOpen(),
                        SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
            }
            return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
        }

        if (state.getBlock() instanceof LargeSlidingDoorBlock slidingDoor && !level.isClientSide()) {
            BlockPos masterPos = state.getValue(LargeSlidingDoorBlock.MASTER) ? pos : slidingDoor.findMasterBlock(level, pos);
            if (masterPos == null) return InteractionResult.FAIL;

            BlockState masterState = level.getBlockState(masterPos);
            LargeSlidingDoorBlock.State doorState = masterState.getValue(LargeSlidingDoorBlock.STATE);

            if (doorState == LargeSlidingDoorBlock.State.CLOSED)
                slidingDoor.handleMoving(masterState, level, masterPos, true);
            else if (doorState == LargeSlidingDoorBlock.State.OPEN)
                slidingDoor.handleMoving(masterState, level, masterPos, false);
        }

        if (state.getBlock() instanceof LargeSlidingDoorBlock)
            return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.CONSUME;

        return InteractionResult.PASS;
    }
}