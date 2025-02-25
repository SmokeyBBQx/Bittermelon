package com.site21.bittermelon.content.items.wires.wire.client;

import com.mojang.blaze3d.platform.Window;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.devices.IElectronic;
import com.site21.bittermelon.content.items.wires.networkcable.NetworkCable;
import com.site21.bittermelon.content.items.wires.wire.Wire;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.CORD_CONNECTION;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.PORT_ID;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class WireRenderer {
    @SubscribeEvent
    public static void onRenderGUILayer(RenderGuiLayerEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            ItemStack heldItem = minecraft.player.getMainHandItem();
            if (heldItem.getItem() instanceof Wire) {
                BlockPos device = heldItem.get(CORD_CONNECTION.get());
                String port = heldItem.get(PORT_ID.get());

                if (device == null || port == null) return;

                if (minecraft.player.level().getBlockEntity(device) instanceof IElectronic deviceEntity) {
                    Component text = Component.literal("Wiring from " + deviceEntity.getAddress());
                    Component portID = Component.literal("(" + port + ")").withStyle(ChatFormatting.GRAY);
                    GuiGraphics guiGraphics = event.getGuiGraphics();
                    Window window = minecraft.getWindow();
                    int width = window.getGuiScaledWidth();
                    int height = window.getGuiScaledHeight();
                    int x = width / 2;
                    int y = height - 45;
                    guiGraphics.drawCenteredString(minecraft.font, text, x, y, 0xFFFFFF);
                    guiGraphics.drawCenteredString(minecraft.font, portID, x, y + 10, 0xFFFFFF);
                }
            }
        }
    }
}
