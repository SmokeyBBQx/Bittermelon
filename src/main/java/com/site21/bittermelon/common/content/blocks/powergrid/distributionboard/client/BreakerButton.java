package com.site21.bittermelon.common.content.blocks.powergrid.distributionboard.client;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterSounds.BREAKER_SWITCH;

public class BreakerButton extends AbstractWidget {
    private final OnPress onPress;

    private final Identifier onIcon;
    private final Identifier onHighlightedIcon;
    private final Identifier offIcon;
    private final Identifier offHighlightedIcon;

    private boolean on = false;

    public BreakerButton(int x, int y, int width, int height, OnPress onPress, Identifier onIcon, Identifier onHighlightedIcon, Identifier offIcon, Identifier offHighlightedIcon) {
        super(x, y, width, height, Component.literal("Breaker"));
        this.onPress = onPress;
        this.onIcon = onIcon;
        this.onHighlightedIcon = onHighlightedIcon;
        this.offIcon = offIcon;
        this.offHighlightedIcon = offHighlightedIcon;
    }

    public BreakerButton(int x, int y, int width, int height, OnPress onPress) {
        this(x, y, width, height, onPress,
                Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "distributionboard/breaker_on"),
                Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "distributionboard/breaker_on_highlighted"),
                Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "distributionboard/breaker_off"),
                Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "distributionboard/breaker_off_highlighted")
        );
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        if (isHovered) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, on ? onHighlightedIcon : offHighlightedIcon, x, y, width, height);
        } else {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, on ? onIcon : offIcon, x, y, width, height);
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
    public void onClick(MouseButtonEvent event, boolean doubleClick) {
        on = !on;
        onPress.onPress(this);
    }

    public interface OnPress {
        void onPress(BreakerButton button);
    }
}
