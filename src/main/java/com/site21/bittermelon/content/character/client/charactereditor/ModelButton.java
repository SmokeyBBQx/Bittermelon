package com.site21.bittermelon.content.character.client.charactereditor;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ModelButton extends AbstractWidget {
    private static final ResourceLocation WIDE_ICON = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/wide_model_button");
    private static final ResourceLocation WIDE_ICON_HIGHLIGHTED = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/wide_model_button_highlighted");
    private static final ResourceLocation SLIM_ICON = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/slim_model_button");
    private static final ResourceLocation SLIM_ICON_HIGHLIGHTED = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "character/slim_model_button_highlighted");

    private final OnPress onPress;
    private boolean wide = true;

    public ModelButton(int x, int y, int width, int height, OnPress onPress) {
        super(x, y, width, height, Component.literal("Toggle Model"));
        this.onPress = onPress;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (isHovered) {
            guiGraphics.blitSprite(RenderPipelines.GUI, wide ? WIDE_ICON_HIGHLIGHTED : SLIM_ICON_HIGHLIGHTED, x, y, width, height);
        } else {
            guiGraphics.blitSprite(RenderPipelines.GUI, wide ? WIDE_ICON : SLIM_ICON, x, y, width, height);
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

    @OnlyIn(Dist.CLIENT)
    public interface OnPress {
        void onPress(ModelButton button);
    }
}
