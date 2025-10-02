package com.site21.bittermelon.content.blocks.devices.implementations.slidingdoor;

import com.site21.bittermelon.client.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.content.blocks.devices.implementations.slidingdoor.SlidingDoorBlock.OPEN;
import static com.site21.bittermelon.content.blocks.devices.implementations.slidingdoor.SlidingDoorBlock.VISIBLE;
import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.SLIDING_DOOR_BLOCK_ENTITY;

public class SlidingDoorBlockEntity extends BlockEntity {
    private final LerpedFloat animation;

    public SlidingDoorBlockEntity(BlockPos pos, BlockState blockState) {
        super(SLIDING_DOOR_BLOCK_ENTITY.get(), pos, blockState);

        animation = LerpedFloat.linear().startWithValue(blockState.getValue(OPEN) ? 1 : 0);
    }

    private static void tick(Level level, BlockPos pos, @NotNull BlockState state, @NotNull SlidingDoorBlockEntity door) {
        if (state.getValue(VISIBLE)) return;

        boolean open = state.getValue(OPEN);
        door.animation.animate(open ? 0.9f : 0, 0.15f, LerpedFloat.EasingFunction.LINEAR);
        door.animation.tick();

        if (!open && door.animation.finished()) {
            level.setBlock(pos, state.setValue(SlidingDoorBlock.VISIBLE, true), Block.UPDATE_ALL);
            level.playSound(null, pos, SoundEvents.IRON_DOOR_CLOSE, SoundSource.BLOCKS, .5f, 1);
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, SlidingDoorBlockEntity blockEntity) {
        tick(level, pos, state, blockEntity);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SlidingDoorBlockEntity blockEntity) {
        tick(level, pos, state, blockEntity);
    }

    public float getAnimationProgress(float partialTick) {
        return animation.getLerped(partialTick);
    }
}
