package com.site21.bittermelon.containment.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class ScoreWidget extends AbstractWidget {
    private final String label;
    private final Supplier<Float> scoreSupplier;
    private static final int WIDTH = 210;
    private static final int HEIGHT = 20;
    private int tickCount = 0;

    public ScoreWidget(int x, int y, String label, Supplier<Float> scoreSupplier) {
        super(x, y, WIDTH, HEIGHT, Component.literal(label));
        this.label = label;
        this.scoreSupplier = scoreSupplier;
    }

    public void tick() {
        tickCount++;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        float score = scoreSupplier.get();

        int textColor = getScoreColor(score);

        if (score > 60 || (tickCount / 10) % 2 == 0) {
            guiGraphics.drawString(Minecraft.getInstance().font,
                    label + ": " + String.format("%.1f%%", score),
                    getX(), getY() + 5,
                    textColor, false);
        }

        int barWidth = 100;
        int filledWidth = (int) (barWidth * (score / 100f));

        guiGraphics.fill(getX() + 110, getY() + 5,
                getX() + 110 + barWidth, getY() + 13,
                0xFF555555);

        guiGraphics.fill(getX() + 110, getY() + 5,
                getX() + 110 + filledWidth, getY() + 13,
                textColor);
    }

    private int getScoreColor(float score) {
        if (score >= 80) return 0xFF00FF00; // Green
        if (score >= 60) return 0xFFFFFF00; // Yellow
        return 0xFFFF0000; // Red
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {

    }
}
