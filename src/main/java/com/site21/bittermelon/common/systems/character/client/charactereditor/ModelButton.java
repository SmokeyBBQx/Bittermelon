package com.site21.bittermelon.common.systems.character.client.charactereditor;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;


public class ModelButton extends AbstractWidget {
    private static final Identifier WIDE_ICON = Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/wide_model_button");
    private static final Identifier WIDE_ICON_HIGHLIGHTED = Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/wide_model_button_highlighted");
    private static final Identifier SLIM_ICON = Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/slim_model_button");
    private static final Identifier SLIM_ICON_HIGHLIGHTED = Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/slim_model_button_highlighted");

    private final OnPress onPress;
    private boolean wide = true;

    public ModelButton(int x, int y, int width, int height, OnPress onPress) {
        super(x, y, width, height, Component.literal("Toggle Model"));
        this.onPress = onPress;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor, int mouseX, int mouseY, float partialTick) {
        if (isHovered) {
            GuiGraphicsExtractor.blitSprite(RenderPipelines.GUI_TEXTURED, wide ? WIDE_ICON_HIGHLIGHTED : SLIM_ICON_HIGHLIGHTED, x, y, width, height);
        } else {
            GuiGraphicsExtractor.blitSprite(RenderPipelines.GUI_TEXTURED, wide ? WIDE_ICON : SLIM_ICON, x, y, width, height);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    public boolean isWide() {
        return wide;
    }

    public void setWide(boolean wide) {
        this.wide = wide;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        wide = !wide;
        onPress.onPress(this);
    }

    
    public interface OnPress {
        void onPress(ModelButton button);
    }
}
