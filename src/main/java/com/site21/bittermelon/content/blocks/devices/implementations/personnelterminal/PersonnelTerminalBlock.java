package com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal;

import com.site21.bittermelon.content.blocks.base.IndentedSmallBlock;
import com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.networking.OpenPersonnelScreen;
import com.site21.bittermelon.content.personnel.registry.PersonnelRegistry;
import com.site21.bittermelon.content.personnel.privilege.PrivilegeManager;
import com.site21.bittermelon.content.personnel.registry.networking.SyncPersonnelRegistry;
import com.site21.bittermelon.content.personnel.privilege.networking.SyncPrivileges;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PersonnelTerminalBlock extends IndentedSmallBlock implements EntityBlock {
    public PersonnelTerminalBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level,
                                                        @NotNull BlockPos pos, @NotNull Player player,
                                                        @NotNull BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            if (player instanceof ServerPlayer serverPlayer) {
                PacketDistributor.sendToPlayer(serverPlayer,
                        new SyncPersonnelRegistry(PersonnelRegistry.get(level).getPersonnelEntries()));
                PacketDistributor.sendToPlayer(serverPlayer,
                        new SyncPrivileges(PrivilegeManager.get(level).getPrivilegeGroups(), PrivilegeManager.get(level).getPrivileges()));
                PacketDistributor.sendToPlayer(serverPlayer, new OpenPersonnelScreen(pos));
            }
        }

        return InteractionResult.SUCCESS_NO_ITEM_USED;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new PersonnelTerminalBlockEntity(blockPos, blockState);
    }
}
