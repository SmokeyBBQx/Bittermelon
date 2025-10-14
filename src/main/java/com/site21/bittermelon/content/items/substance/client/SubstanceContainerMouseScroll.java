package com.site21.bittermelon.content.items.substance.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.items.substance.FluidContainerItem;
import com.site21.bittermelon.content.items.substance.GasContainerItem;
import com.site21.bittermelon.content.items.substance.networking.ReleasePressureUpdate;
import com.site21.bittermelon.content.items.substance.networking.TransferRateUpdate;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class SubstanceContainerMouseScroll {

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player != null && player.isShiftKeyDown()) {
            ItemStack heldItem = player.getMainHandItem();
            Item item = heldItem.getItem();

            if (item instanceof FluidContainerItem) {
                int currentRate = FluidContainerItem.getTransferRate(heldItem);
                int newRate = Mth.clamp(currentRate + (event.getScrollDeltaY() > 0 ? 1 : -1),
                        FluidContainerItem.MIN_TRANSFER_RATE,
                        FluidContainerItem.getMaxTransferRate(heldItem));
                FluidContainerItem.setTransferRate(heldItem, newRate);

                PacketDistributor.sendToServer(new TransferRateUpdate(newRate, InteractionHand.MAIN_HAND));

                event.setCanceled(true);
            } else if (item instanceof GasContainerItem gasContainerItem) {
                int currentReleasePressure = gasContainerItem.getReleasePressure(heldItem);
                int newReleasePressure = Math.clamp(currentReleasePressure + (event.getScrollDeltaY() > 0 ? 5 : -5),
                        0,
                        gasContainerItem.getMaxReleasePressure());
                gasContainerItem.setReleasePressure(heldItem, newReleasePressure);

                PacketDistributor.sendToServer(new ReleasePressureUpdate(newReleasePressure, InteractionHand.MAIN_HAND));

                event.setCanceled(true);
            }
        }
    }
}
