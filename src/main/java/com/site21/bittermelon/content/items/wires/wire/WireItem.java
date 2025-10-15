package com.site21.bittermelon.content.items.wires.wire;

import com.site21.bittermelon.systems.electronics.ElectronicDevice;
import com.site21.bittermelon.systems.electronics.PanelDevice;
import com.site21.bittermelon.content.items.wires.wire.networking.OpenWiringScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.CORD_CONNECTION;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.PORT_ID;

public class WireItem extends Item {
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
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, level, entity, slot);

        if (slot != EquipmentSlot.MAINHAND && slot != EquipmentSlot.OFFHAND) {
            stack.remove(CORD_CONNECTION);
            stack.remove(PORT_ID);
        }
    }
}
