package com.site21.bittermelon.content.blocks.powergrid.distributionboard.client;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterSounds.BREAKER_SWITCH;

public class BreakerButton extends AbstractWidget {
    private static final ResourceLocation ON_ICON = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "distributionboard/breaker_on");
    private static final ResourceLocation ON_HIGHLIGHTED_ICON = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "distributionboard/breaker_on_highlighted");
    private static final ResourceLocation OFF_ICON = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "distributionboard/breaker_off");
    private static final ResourceLocation OFF_HIGHLIGHTED_ICON = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "distributionboard/breaker_off_highlighted");

    private final OnPress onPress;
    private boolean on = false;

    public BreakerButton(int x, int y, int width, int height, OnPress onPress) {
        super(x, y, width, height, Component.literal("Breaker"));
        this.onPress = onPress;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (isHovered) {
            guiGraphics.blitSprite(on ? ON_HIGHLIGHTED_ICON : OFF_HIGHLIGHTED_ICON, x, y, width, height);
        } else {
            guiGraphics.blitSprite(on ? ON_ICON : OFF_ICON, x, y, width, height);
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
