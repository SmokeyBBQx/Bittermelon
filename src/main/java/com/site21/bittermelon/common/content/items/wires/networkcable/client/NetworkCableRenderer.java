package com.site21.bittermelon.common.content.items.wires.networkcable.client;

import com.mojang.blaze3d.platform.Window;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.items.wires.networkcable.NetworkCable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.*;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class NetworkCableRenderer {
    @SubscribeEvent
    public static void onRenderGUILayer(RenderGuiLayerEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            ItemStack heldItem = minecraft.player.getMainHandItem();
            if (heldItem.getItem() instanceof NetworkCable) {
                BlockPos devicePos = heldItem.get(CORD_CONNECTION.get());

                if (devicePos == null) return;
                MutableComponent deviceName = minecraft.player.level().getBlockState(devicePos).getBlock().getName();

                Component text = Component.literal("Linking from " + deviceName);
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
