package com.site21.bittermelon.content.items.cables.networkcable.client;

import com.mojang.blaze3d.platform.Window;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.devices.IDeviceEntity;
import com.site21.bittermelon.content.items.cables.networkcable.NetworkCable;
import com.site21.bittermelon.content.items.laserdesignator.LaserDesignatorItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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
                BlockPos device = heldItem.get(CORD_CONNECTION.get());

                if (device == null) return;

                if (minecraft.player.level().getBlockEntity(device) instanceof IDeviceEntity deviceEntity) {
                    Component text = Component.literal("Linking from " + deviceEntity.getAddress());
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
}
