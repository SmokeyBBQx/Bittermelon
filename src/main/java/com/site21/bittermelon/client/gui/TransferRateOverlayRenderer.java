package com.site21.bittermelon.client.gui;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.items.containers.substance.FluidContainerItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;

@EventBusSubscriber(value = Dist.CLIENT)
public class TransferRateOverlayRenderer {
    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGuiLayerEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            ItemStack heldItem = minecraft.player.getMainHandItem();
            if (heldItem.getItem() instanceof FluidContainerItem) {
                int transferRate = FluidContainerItem.getTransferRate(heldItem);
                Component text = Component.literal("Transfer Rate: " + transferRate);
                GuiGraphics guiGraphics = event.getGuiGraphics();
                Window window = minecraft.getWindow();
                int width = window.getGuiScaledWidth();
                int height = window.getGuiScaledHeight();
                int x = (width - minecraft.font.width(text)) / 2;
                int y = height - 35;
                guiGraphics.drawString(minecraft.font, text, x, y, 0xFFFFFF);
            }
        }
    }
}
