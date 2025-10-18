package com.site21.bittermelon.common.content.blocks.electronics.securedoor;

import com.site21.bittermelon.init.neoforge.BitterSounds;
import com.site21.bittermelon.util.LocalMessageHelper;
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
        if (level.isClientSide) return InteractionResult.PASS;

        if (player.isShiftKeyDown()) {
            level.playSound(null, pos, BitterSounds.KNOCK.value(), SoundSource.PLAYERS, 1.0f, 1.0f);
            LocalMessageHelper.sendEmoteMessage(level, player, 10, "knocks on the door.");
            return InteractionResult.SUCCESS;
        }

        if (level.getBlockEntity(pos) instanceof SecureDoorBlockEntity blockEntity) {
            if (blockEntity.isLocked()) return InteractionResult.PASS;

            blockEntity.setMotors(!isOpen(state));
            playSound(player, level, pos, state.getValue(OPEN));
            level.gameEvent(player, !isOpen(state) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);

            if (isOpen(state)) {
                blockEntity.setLocked(true);
                blockEntity.runForOtherHalf(otherHalf -> otherHalf.setLocked(true));
                level.playSound(null, pos, SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE.value(), SoundSource.BLOCKS);
            }

            return InteractionResult.SUCCESS;

        }
        return InteractionResult.FAIL;
    }
}
