package com.site21.bittermelon.content.blocks.electronics.securedoor;

import com.site21.bittermelon.systems.personnel.registry.PersonnelRegistry;
import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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

import java.util.Map;

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

    @Override
    protected @NotNull InteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof KeycardReaderSecureDoorBlockEntity door) {
            if (!door.isLocked()) return InteractionResult.TRY_WITH_EMPTY_HAND;

            if (stack.is(KEYCARD.get())) {
                int id = stack.getOrDefault(BitterDataComponents.ID_NUMBER, 0);
                if (id == 0) return InteractionResult.TRY_WITH_EMPTY_HAND;

                scan(id, level, pos, door);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }

    public void scan(int id, Level level, BlockPos pos, KeycardReaderSecureDoorBlockEntity door) {
        if (level == null) return;
        if (!door.isOn()) return;

        Map<String, Boolean> requiredPrivileges = door.getPrivileges();
        Map<String, Boolean> privileges = PersonnelRegistry.get(level).getEntry(id).getPrivileges();
        if (door.hasPermission(privileges, requiredPrivileges)) {
            door.setLocked(false);
            door.runForOtherHalf(otherHalf -> otherHalf.setLocked(false));
            level.playSound(null, pos, SoundEvents.NOTE_BLOCK_BELL.value(), SoundSource.BLOCKS);
        }
    }
}
