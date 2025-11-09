package com.site21.bittermelon.common.content.items.wire;

import com.site21.bittermelon.common.content.items.base.BitterItem;
import com.site21.bittermelon.common.content.items.wire.networking.OpenWiringScreen;
import com.site21.bittermelon.common.systems.electronics.ElectronicDevice;
import com.site21.bittermelon.common.systems.electronics.PanelDevice;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.CORD_CONNECTION;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.PORT_ID;

public class WireItem extends BitterItem {
    public WireItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        if (player == null) return InteractionResult.FAIL;

        if (level.getBlockEntity(pos) instanceof ElectronicDevice) {
            if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
                if (level.getBlockEntity(pos) instanceof PanelDevice panelDevice && !panelDevice.isPanelOpen()) return InteractionResult.FAIL;
                PacketDistributor.sendToPlayer(serverPlayer, new OpenWiringScreen(pos, context.getHand()));
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (!isSelected) {
            stack.remove(CORD_CONNECTION);
            stack.remove(PORT_ID);
        }
    }
}
