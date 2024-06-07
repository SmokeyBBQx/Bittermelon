package net.smokeybbq.bittermelon.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.smokeybbq.bittermelon.Bittermelon;
import net.smokeybbq.bittermelon.medical.symptoms.Rash;

public class ItchProgressScreen extends Screen {
    private static final ResourceLocation TEXTURE = new ResourceLocation(Bittermelon.MODID, "textures/gui/itch_progress.png");
    private final Rash rash;
    private int progress;

    public ItchProgressScreen(Rash rash) {
        super(Component.literal("Scratch Rash"));
        this.rash = rash;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int centerX = (this.width - 182) / 2;
        int centerY = (this.height - 22) / 2;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
