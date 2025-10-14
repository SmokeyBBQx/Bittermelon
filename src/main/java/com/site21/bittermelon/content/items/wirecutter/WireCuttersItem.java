package com.site21.bittermelon.content.items.wirecutter;

import com.site21.bittermelon.content.blocks.devices.ElectronicDevice;
import com.site21.bittermelon.content.blocks.devices.PanelDevice;
import com.site21.bittermelon.content.items.wirecutter.networking.OpenWireCutterScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class WireCuttersItem extends Item {
    public WireCuttersItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        if (player == null || level.isClientSide) return InteractionResult.FAIL;

        if (level.getBlockEntity(pos) instanceof ElectronicDevice) {
            if (player instanceof ServerPlayer serverPlayer) {
                if (level.getBlockEntity(pos) instanceof PanelDevice panelDevice && !panelDevice.isPanelOpen())
                    return InteractionResult.FAIL;
                PacketDistributor.sendToPlayer(serverPlayer, new OpenWireCutterScreen(pos));
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }
}
