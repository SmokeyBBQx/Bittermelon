package com.site21.bittermelon.common.content.items.gun.client;

import com.mojang.blaze3d.platform.Window;
import com.site21.bittermelon.common.content.items.gun.IGunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.AMMO;

@EventBusSubscriber(value = Dist.CLIENT)
public class AmmoCounterRenderer {
    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGuiLayerEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) return;

        ItemStack heldItem = minecraft.player.getMainHandItem();
        Item item = heldItem.getItem();

        if (item instanceof IGunItem gun) {
            int maxAmmo = gun.getMaxAmmo();
            int ammo = heldItem.getOrDefault(AMMO, 0);
            String text = ammo + "/" + maxAmmo;

            GuiGraphics guiGraphics = event.getGuiGraphics();
            Window window = minecraft.getWindow();

            int width = window.getGuiScaledWidth();
            int height = window.getGuiScaledHeight();
            int x = (width - minecraft.font.width(text)) / 2 + 105;
            int y = height - 15;

            guiGraphics.drawString(minecraft.font, text, x, y, 0xFFFFFF);
        }
    }
}
