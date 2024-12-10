package com.site21.bittermelon.items.containers.substance;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.networking.server.TransferRateUpdate;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class FluidContainerMouseScroll {

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player != null && player.isShiftKeyDown()) {
            ItemStack heldItem = player.getMainHandItem();
            if (heldItem.getItem() instanceof FluidContainerItem) {
                int currentRate = FluidContainerItem.getTransferRate(heldItem);
                int newRate = Mth.clamp(currentRate + (event.getScrollDeltaY() > 0 ? 1 : -1),
                        FluidContainerItem.MIN_TRANSFER_RATE,
                        FluidContainerItem.MAX_TRANSFER_RATE);
                FluidContainerItem.setTransferRate(heldItem, newRate);

                PacketDistributor.sendToServer(new TransferRateUpdate(newRate, InteractionHand.MAIN_HAND));

                event.setCanceled(true);
            }
        }
    }
}
