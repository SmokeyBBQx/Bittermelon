package com.site21.bittermelon.content.items.handheldsysteminterface;

import com.site21.bittermelon.content.blocks.devices.privilege.networking.OpenPrivilegeEditorScreen;
import com.site21.bittermelon.content.personnel.privilege.PrivilegeOwner;
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

        if (level.getBlockEntity(pos) instanceof PrivilegeOwner privilegeOwner && context.getPlayer() instanceof ServerPlayer player) {
            if (!privilegeOwner.canAccess()) return InteractionResult.FAIL;

            PacketDistributor.sendToPlayer(player, new OpenPrivilegeEditorScreen(pos));
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }
}
