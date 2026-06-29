package com.site21.bittermelon.common.content.items.scps.scp377.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.items.scps.scp377.Fortune;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class SCP3771Screen extends Screen {
    private static final Identifier FORTUNE_BACKGROUND = Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "fortune_background");

    private final Fortune fortune;

    public SCP3771Screen(Fortune fortune) {
        super(Component.literal("Fortune"));
        this.fortune = fortune;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        int backgroundWidth = (int) (123 * 2.5);
        int backgroundHeight = (int) (23 * 2.5);

        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, FORTUNE_BACKGROUND, width / 2 - backgroundWidth / 2, height / 3, backgroundWidth, backgroundHeight);
        int textWidth = minecraft.font.width(fortune.getMessage());

        graphics.text(minecraft.font, fortune.getMessage(), width / 2 - textWidth / 2, height / 3 + backgroundHeight / 2 - 5, 0xFF000000, false);
    }

    public boolean isPauseScreen() {
        return false;
    }
}
