package com.site21.bittermelon.common.content.blocks.powergrid.distributionboard.client;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterSounds.BREAKER_SWITCH;

public class BreakerButton extends AbstractWidget {
    private final OnPress onPress;

    private final ResourceLocation onIcon;
    private final ResourceLocation onHighlightedIcon;
    private final ResourceLocation offIcon;
    private final ResourceLocation offHighlightedIcon;

    private boolean on = false;

    public BreakerButton(int x, int y, int width, int height, OnPress onPress, ResourceLocation onIcon, ResourceLocation onHighlightedIcon, ResourceLocation offIcon, ResourceLocation offHighlightedIcon) {
        super(x, y, width, height, Component.literal("Breaker"));
        this.onPress = onPress;
        this.onIcon = onIcon;
        this.onHighlightedIcon = onHighlightedIcon;
        this.offIcon = offIcon;
        this.offHighlightedIcon = offHighlightedIcon;
    }

    public BreakerButton(int x, int y, int width, int height, OnPress onPress) {
        this(x, y, width, height, onPress,
                ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "distributionboard/breaker_on"),
                ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "distributionboard/breaker_on_highlighted"),
                ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "distributionboard/breaker_off"),
                ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "distributionboard/breaker_off_highlighted")
        );
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (isHovered) {
            guiGraphics.blitSprite(RenderPipelines.GUI, on ? onHighlightedIcon : offHighlightedIcon, x, y, width, height);
        } else {
            guiGraphics.blitSprite(RenderPipelines.GUI, on ? onIcon : offIcon, x, y, width, height);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public void playDownSound(@NotNull SoundManager soundManager) {
        soundManager.play(SimpleSoundInstance.forUI(BREAKER_SWITCH, isOn() ? 1f : 0.95f));
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        on = !on;
        onPress.onPress(this);
    }

    @OnlyIn(Dist.CLIENT)
    public interface OnPress {
        void onPress(BreakerButton button);
    }
}
