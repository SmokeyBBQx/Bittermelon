package com.site21.bittermelon.content.items.handheldsysteminterface;

import com.site21.bittermelon.systems.electronics.privilege.networking.OpenPrivilegeEditorScreen;
import com.site21.bittermelon.systems.personnel.privilege.PrivilegeManager;
import com.site21.bittermelon.systems.personnel.privilege.PrivilegeOwner;
import com.site21.bittermelon.systems.personnel.privilege.networking.SyncPrivileges;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class HandheldSystemInterface extends Item {
    public HandheldSystemInterface(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (level.isClientSide) return InteractionResult.PASS;

        if (level.getBlockEntity(pos) instanceof PrivilegeOwner privilegeOwner && context.getPlayer() instanceof ServerPlayer player) {
            if (!privilegeOwner.canAccess()) return InteractionResult.FAIL;

            PacketDistributor.sendToPlayer(player,
                    new SyncPrivileges(PrivilegeManager.get(level).getPrivilegeGroups(), PrivilegeManager.get(level).getPrivileges()));
            PacketDistributor.sendToPlayer(player, new OpenPrivilegeEditorScreen(pos));
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }
}
