package com.site21.bittermelon.common.content.items.substance.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(value = Dist.CLIENT)
public class SubstanceContainerOverlayRenderer {
//    @SubscribeEvent
//    public static void onRenderGameOverlay(RenderGuiLayerEvent.Post event) {
//        Minecraft minecraft = Minecraft.getInstance();
//        if (minecraft.player != null) {
//            ItemStack heldItem = minecraft.player.getMainHandItem();
//            Item item = heldItem.getItem();
//
//            if (item instanceof FluidContainerItem) {
//                int transferRate = FluidContainerItem.getTransferRate(heldItem);
//                Component text = Component.literal("Transfer Rate: " + transferRate);
//                GuiGraphicsExtractor GuiGraphicsExtractor = event.getGuiGraphicsExtractor();
//                Window window = minecraft.getWindow();
//                int width = window.getGuiScaledWidth();
//                int height = window.getGuiScaledHeight();
//                int x = (width - minecraft.font.width(text)) / 2;
//                int y = height - 35;
//                GuiGraphicsExtractor.drawString(minecraft.font, text, x, y, 0xFFFFFF);
//            } else if (item instanceof GasContainerItem gasContainerItem) {
//                int releasePressure = gasContainerItem.getReleasePressure(heldItem);
//                Component text = Component.literal("Release Pressure: " + releasePressure + " kPa");
//                GuiGraphicsExtractor GuiGraphicsExtractor = event.getGuiGraphicsExtractor();
//                Window window = minecraft.getWindow();
//                int width = window.getGuiScaledWidth();
//                int height = window.getGuiScaledHeight();
//                int x = (width - minecraft.font.width(text)) / 2;
//                int y = height - 35;
//                GuiGraphicsExtractor.drawString(minecraft.font, text, x, y, 0xFFFFFF);
//            }
//        }
//    }
}
