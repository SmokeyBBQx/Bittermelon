package com.site21.bittermelon.common.content.blocks.electronics.intercom.client;

import com.mojang.blaze3d.platform.Window;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.blocks.electronics.intercom.IntercomBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;


@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class ActionTipRenderer {
    @SubscribeEvent
    public static void onRenderGUILayer(RenderGuiLayerEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        HitResult hit = mc.hitResult;
        if (hit != null && hit.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = ((BlockHitResult) hit).getBlockPos();
            Level level = mc.level;

            if (level == null) return;
            if (!(level.getBlockState(pos).getBlock() instanceof IntercomBlock)) return;

            Window window = mc.getWindow();
            int width = window.getGuiScaledWidth();
            int height = window.getGuiScaledHeight();

            Font font = mc.font;
            Component text = Component.literal("Shift + Right Click To Pick Up Phone");

            int x = width - 120 - font.width(text) / 2;
            int y = height - 15;

            GuiGraphics guiGraphics = event.getGuiGraphics();
            guiGraphics.drawString(font, text, x, y, 0xFFFFFF);
        }
    }
}
