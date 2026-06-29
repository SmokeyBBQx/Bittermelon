package com.site21.bittermelon.common.content.items.wire.client;

import com.mojang.blaze3d.platform.Window;
import com.site21.bittermelon.common.content.items.wire.WireItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.CORD_CONNECTION;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.PORT_ID;

public class WireOverlayExtractor {

    public static void extractWiringOverlay(GuiGraphicsExtractor graphics) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) return;

        ItemStack heldItem = minecraft.player.getMainHandItem();
        if (!(heldItem.getItem() instanceof WireItem)) return;

        BlockPos devicePos = heldItem.get(CORD_CONNECTION.get());
        String port = heldItem.get(PORT_ID.get());

        if (devicePos == null || port == null) return;
        MutableComponent deviceName = minecraft.player.level().getBlockState(devicePos).getBlock().getName();

        Component text = Component.literal("Wiring from " + deviceName);
        Component portID = Component.literal("(" + port + ")").withStyle(ChatFormatting.GRAY);
        Window window = minecraft.getWindow();
        int width = window.getGuiScaledWidth();
        int height = window.getGuiScaledHeight();
        int x = width / 2;
        int y = height - 45;
        graphics.centeredText(minecraft.font, text, x, y, 0xFFFFFFFF);
        graphics.centeredText(minecraft.font, portID, x, y + 10, 0xFFFFFFFF);
    }
}
