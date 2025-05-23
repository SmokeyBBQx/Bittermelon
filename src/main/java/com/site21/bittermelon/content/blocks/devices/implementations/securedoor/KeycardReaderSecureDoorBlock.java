package com.site21.bittermelon.content.blocks.devices.implementations.securedoor;

import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static com.site21.bittermelon.init.neoforge.BitterItems.KEYCARD;

public class KeycardReaderSecureDoorBlock extends SecureDoorBlock implements EntityBlock {
    public KeycardReaderSecureDoorBlock(BlockSetType type, Properties properties) {
        super(type, properties);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : (level0, state0, blockEntityType0, blockEntity) -> ((KeycardReaderSecureDoorBlockEntity) blockEntity).tick();
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new KeycardReaderSecureDoorBlockEntity(blockPos, blockState);
    }

    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof SecureDoorBlockEntity blockEntity) {
            if (!blockEntity.isLocked()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

            if (stack.is(KEYCARD.get())) {
                int id = stack.getOrDefault(BitterDataComponents.ID_NUMBER, 0);
                scan(id, level, pos, blockEntity);
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public void scan(int id, Level level, BlockPos pos, SecureDoorBlockEntity blockEntity) {
        if (level == null) return;
//        List<String> privileges = PersonnelRegistry.get(level).getEntry(id).getPrivileges();
//        if (privileges.stream().anyMatch(requiredPrivileges::contains)) {
//            setLocked(false);
//        }
        blockEntity.setLocked(false);
        level.playSound(null, pos, SoundEvents.NOTE_BLOCK_BELL.value(), SoundSource.BLOCKS);
    }
}
