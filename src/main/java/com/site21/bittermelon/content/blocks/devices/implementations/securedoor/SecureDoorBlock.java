package com.site21.bittermelon.content.blocks.devices.implementations.securedoor;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class SecureDoorBlock extends DoorBlock implements EntityBlock {
    public SecureDoorBlock(BlockSetType type, Properties properties) {
        super(type, properties);
    }

    private void playSound(@Nullable Entity source, @NotNull Level level, BlockPos pos, boolean isOpening) {
        level.playSound(source, pos, isOpening ? BlockSetType.IRON.doorOpen() : BlockSetType.IRON.doorClose(), SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.1F + 0.9F);
    }

    @Override
    protected void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block block, @NotNull BlockPos fromPos, boolean isMoving) {
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new SecureDoorBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : (level0, state0, blockEntityType0, blockEntity) -> ((SecureDoorBlockEntity) blockEntity).tick();
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof SecureDoorBlockEntity blockEntity) {
            if (!blockEntity.isLocked()) {
                blockEntity.setMotors(!isOpen(state));
                this.playSound(player, level, pos, state.getValue(OPEN));
                level.gameEvent(player, !this.isOpen(state) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
                if (this.isOpen(state)) {
                    blockEntity.setLocked(true);
                    blockEntity.runForOtherHalf(otherHalf -> otherHalf.setLocked(true));
                    level.playSound(null, pos, SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE.value(), SoundSource.BLOCKS);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return InteractionResult.FAIL;
    }
}
