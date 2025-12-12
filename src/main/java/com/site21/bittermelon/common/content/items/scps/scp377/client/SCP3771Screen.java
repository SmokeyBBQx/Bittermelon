package com.site21.bittermelon.common.content.items.scps.scp377.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.items.scps.scp377.Fortune;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SCP3771Screen extends Screen {
    private static final ResourceLocation FORTUNE_BACKGROUND = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "fortune_background");

    private final Fortune fortune;

    public SCP3771Screen(Fortune fortune) {
        super(Component.literal("Fortune"));
        this.fortune = fortune;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int backgroundWidth = (int) (123 * 2.5);
        int backgroundHeight = (int) (23 * 2.5);

        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, FORTUNE_BACKGROUND, width / 2 - backgroundWidth / 2, height / 3, backgroundWidth, backgroundHeight);
        int textWidth = minecraft.font.width(fortune.getMessage());

        guiGraphics.drawString(minecraft.font, fortune.getMessage(), width / 2 - textWidth / 2, height / 3 + backgroundHeight / 2 - 5, 0xFF000000, false);
    }

    public boolean isPauseScreen() {
        return false;
    }
}
