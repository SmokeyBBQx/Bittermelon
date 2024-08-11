package net.smokeybbq.bittermelon.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.smokeybbq.bittermelon.Bittermelon;
import net.smokeybbq.bittermelon.items.substancecontainers.SubstanceContainerItem;

@Mod.EventBusSubscriber(modid = Bittermelon.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class TransferRateOverlayRenderer {
    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGuiOverlayEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            ItemStack heldItem = minecraft.player.getMainHandItem();
            if (heldItem.getItem() instanceof SubstanceContainerItem) {
                int transferRate = SubstanceContainerItem.getTransferRate(heldItem);
                Component text = Component.literal("Transfer Rate: " + transferRate);
                GuiGraphics guiGraphics = event.getGuiGraphics();
                int width = event.getWindow().getGuiScaledWidth();
                int height = event.getWindow().getGuiScaledHeight();
                int x = (width - minecraft.font.width(text)) / 2;
                int y = height - 35;
                guiGraphics.drawString(minecraft.font, text, x, y, 0xFFFFFF);
            }
        }
    }

}