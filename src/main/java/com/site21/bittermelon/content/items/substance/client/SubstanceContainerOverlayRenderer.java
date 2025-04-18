package com.site21.bittermelon.content.items.substance.client;

import com.mojang.blaze3d.platform.Window;
import com.site21.bittermelon.content.items.substance.FluidContainerItem;
import com.site21.bittermelon.content.items.substance.GasContainerItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;

@EventBusSubscriber(value = Dist.CLIENT)
public class SubstanceContainerOverlayRenderer {
    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGuiLayerEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            ItemStack heldItem = minecraft.player.getMainHandItem();
            Item item = heldItem.getItem();
            if (item instanceof FluidContainerItem) {
                int transferRate = FluidContainerItem.getTransferRate(heldItem);
                Component text = Component.literal("Transfer Rate: " + transferRate);
                GuiGraphics guiGraphics = event.getGuiGraphics();
                Window window = minecraft.getWindow();
                int width = window.getGuiScaledWidth();
                int height = window.getGuiScaledHeight();
                int x = (width - minecraft.font.width(text)) / 2;
                int y = height - 35;
                guiGraphics.drawString(minecraft.font, text, x, y, 0xFFFFFF);
            } else if (item instanceof GasContainerItem gasContainerItem) {
                int releasePressure = gasContainerItem.getReleasePressure(heldItem);
                Component text = Component.literal("Release Pressure: " + releasePressure + " kPa");
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
