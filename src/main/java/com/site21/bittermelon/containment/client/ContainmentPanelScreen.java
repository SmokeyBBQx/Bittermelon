package com.site21.bittermelon.containment.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.blocks.blockentities.ContainmentPanelBlockEntity;
import com.site21.bittermelon.networking.server.ContainmentNameUpdate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.BitterSounds.BOOT_UP_TUNE;
import static com.site21.bittermelon.init.BitterSounds.TERMINAL_HUM;

@OnlyIn(Dist.CLIENT)
public class ContainmentPanelScreen extends Screen {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/containment_panel.png");
    private static final int TEXTURE_WIDTH = 256;
    private static final int TEXTURE_HEIGHT = 166;
    private PostChain backgroundShader;
    private PostChain foregroundShader;
    private static final ResourceLocation SHADER_LOCATION =
            ResourceLocation.fromNamespaceAndPath("bittermelon", "shaders/post/crt.json");

    private final ContainmentPanelBlockEntity blockEntity;
    private EditBox nameField;
    private int leftPos;
    private int topPos;
    private final boolean hasAccess;

    private boolean isBooted = false;
    private int bootUpTimer = 0;
    private static final int BOOT_UP_TIME = 140;
    private float loadingProgress = 0;
    private static final int LOADING_BAR_WIDTH = 180; // pixels
    private static final int LOADING_BAR_HEIGHT = 10;

    private int soundDelay = 0;

    public ContainmentPanelScreen(ContainmentPanelBlockEntity blockEntity, boolean hasAccess) {
        super(Component.literal("Containment Panel"));
        this.blockEntity = blockEntity;
        this.hasAccess = hasAccess;
    }

    @Override
    protected void init() {
        super.init();

        try {
            backgroundShader = new PostChain(
                    minecraft.getTextureManager(),
                    minecraft.getResourceManager(),
                    minecraft.getMainRenderTarget(),
                    ResourceLocation.fromNamespaceAndPath("bittermelon", "shaders/post/crt.json")
            );

            backgroundShader.resize(
                    minecraft.getWindow().getWidth(),
                    minecraft.getWindow().getHeight()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            foregroundShader = new PostChain(
                    minecraft.getTextureManager(),
                    minecraft.getResourceManager(),
                    minecraft.getMainRenderTarget(),
                    ResourceLocation.fromNamespaceAndPath("minecraft", "shaders/post/ntsc.json")
            );

            foregroundShader.resize(
                    minecraft.getWindow().getWidth(),
                    minecraft.getWindow().getHeight()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.leftPos = (this.width - TEXTURE_WIDTH) / 2;
        this.topPos = (this.height - TEXTURE_HEIGHT) / 2;

        this.nameField = new EditBox(this.font,
                leftPos + 10, topPos + 10,
                120, 20,
                Component.literal("Containment Designation"));
        this.nameField.setValue(blockEntity.getName() != null ? blockEntity.getName() : "");
        this.nameField.setMaxLength(32);
        this.addRenderableWidget(this.nameField);

        int yOffset = 40;
        addRenderableWidget(new ScoreWidget(
                leftPos + 10, topPos + yOffset,
                "Overall", blockEntity::getContainmentScore));

        yOffset += 30;
        addRenderableWidget(new ScoreWidget(
                leftPos + 10, topPos + yOffset,
                "Security", blockEntity::getSecurityScore));

        yOffset += 25;
        addRenderableWidget(new ScoreWidget(
                leftPos + 10, topPos + yOffset,
                "Research", blockEntity::getResearchScore));

        yOffset += 25;
        addRenderableWidget(new ScoreWidget(
                leftPos + 10, topPos + yOffset,
                "Maintenance", blockEntity::getMaintenanceScore));

        yOffset += 25;
        addRenderableWidget(new ScoreWidget(
                leftPos + 10, topPos + yOffset,
                "Caretaking", blockEntity::getCaretakingScore));
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.drawString(this.font, this.title, leftPos + 8, topPos + 8, 4210752, false);

//        graphics.fill((this.width - TEXTURE_WIDTH) / 2 - 20, this.topPos = (this.height - TEXTURE_HEIGHT) / 2 - 20, width - 200, height - 60, 0xFF000000 );
        graphics.fill(0, 0, width, height, 0xFF000000);

        if (!isBooted) {
            if (loadingProgress > 20) {
                graphics.drawCenteredString(font, "SMART CONTAINMENT INTERFACE", width / 2, (int) (height / 2.5), 0xFF00FF00);
                graphics.drawCenteredString(font, "4.0.5E", width / 2, height / 2, 0xFF00FF00);
            }

            int barX = (width - LOADING_BAR_WIDTH) / 2;
            int barY = height * 2 / 3;

            graphics.fill(barX - 1, barY - 1, barX + LOADING_BAR_WIDTH + 1, barY + LOADING_BAR_HEIGHT + 1, 0xFF001100);

            int progressWidth = (int)((loadingProgress / 100f) * LOADING_BAR_WIDTH);
            graphics.fill(barX, barY, barX + progressWidth, barY + LOADING_BAR_HEIGHT, 0xFF00FF00);

            renderBackground(graphics, mouseX, mouseY, partialTick);

            if (loadingProgress > 20) {
                graphics.drawCenteredString(font, "SMART CONTAINMENT INTERFACE", width / 2, (int) (height / 2.5), 0xFF00FF00);
                graphics.drawCenteredString(font, "4.0.5E", width / 2, height / 2, 0xFF00FF00);
            }
            graphics.fill(barX - 1, barY - 1, barX + LOADING_BAR_WIDTH + 1, barY + LOADING_BAR_HEIGHT + 1, 0xFF001100);
  
            graphics.fill(barX, barY, barX + progressWidth, barY + LOADING_BAR_HEIGHT, 0xFF00FF00);

        } else {

            if (hasAccess) {
                renderMainMenu(graphics, mouseX, mouseY, partialTick);
                renderMainMenu(graphics, mouseX, mouseY, partialTick);
            } else {
                renderNoAccess(graphics, mouseX, mouseY, partialTick);
                renderNoAccess(graphics, mouseX, mouseY, partialTick);
            }
        }

        renderShaders(partialTick);
    }

    private void renderNoAccess(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(font, "*NO ACCESS*", width / 2, (int) (height / 2.5), 0xFFFF0000);
        guiGraphics.drawCenteredString(font, "SCAN TO GAIN ACCESS", width / 2, height / 2, 0xFFFF0000);
    }

    private void renderMainMenu(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.fill((this.width - TEXTURE_WIDTH) / 2 - 22, this.topPos = (this.height - TEXTURE_HEIGHT) / 2 - 22, width - 198, this.topPos = (this.height - TEXTURE_HEIGHT) / 2 - 20, 0xFFFFFF00);
        guiGraphics.fill((this.width - TEXTURE_WIDTH) / 2 - 22, this.topPos = (this.height - TEXTURE_HEIGHT) / 2 - 22, (this.width - TEXTURE_WIDTH) / 2 - 20, height - 60, 0xFFFFFF00);
        guiGraphics.fill(width - 198, this.topPos = (this.height - TEXTURE_HEIGHT) / 2 - 22, width - 200, height - 60, 0xFFFFFF00);
        guiGraphics.fill((this.width - TEXTURE_WIDTH) / 2 - 22, height - 60, width - 198, height - 62, 0xFFFFFF00);

    }

    private void renderShaders(float partialTick) {
//        int shaderLeft = (this.width - TEXTURE_WIDTH) / 2 - 20;
//        int shaderTop = (this.height - TEXTURE_HEIGHT) / 2 - 20;
//        int shaderRight = width - 200;
//        int shaderBottom = height - 60;
//
//        RenderSystem.enableScissor(
//                (int) (shaderLeft * minecraft.getWindow().getGuiScale()),
//                (int) (minecraft.getWindow().getHeight() - shaderBottom * minecraft.getWindow().getGuiScale()),
//                (int) ((shaderRight - shaderLeft) * minecraft.getWindow().getGuiScale()),
//                (int) ((shaderBottom - shaderTop) * minecraft.getWindow().getGuiScale())
//        );

        if (foregroundShader != null) {
            RenderSystem.disableBlend();
            RenderSystem.disableDepthTest();
            RenderSystem.resetTextureMatrix();

            foregroundShader.process(partialTick);
        }

        if (backgroundShader != null) {
            RenderSystem.disableBlend();
            RenderSystem.disableDepthTest();
            RenderSystem.resetTextureMatrix();

            backgroundShader.process(partialTick);
        }

        if (minecraft == null) return;
        minecraft.getMainRenderTarget().bindWrite(true);

//        RenderSystem.disableScissor();
    }

    @Override
    public void tick() {
        super.tick();
        if (!isBooted) {
            bootUpTimer++;
            loadingProgress = (float)bootUpTimer / BOOT_UP_TIME * 100f;
            if (bootUpTimer >= BOOT_UP_TIME) {
                isBooted = true;
            }

            if (loadingProgress == 20) {
                playBootUpSound();
            }
        }

        if (soundDelay > 0) {
            soundDelay--;
        } else {
            playAmbientSound();
        }

        // TODO: Figure out a way to stop sound

        for (Renderable renderable : this.renderables) {
            if (renderable instanceof ScoreWidget widget) {
                widget.tick();
            }
        }
    }

    private void playAmbientSound() {
        if (this.minecraft != null && this.minecraft.player != null) {
            this.minecraft.player.playSound(
                    TERMINAL_HUM.get(),
                    0.7F,
                    0.5F
            );
            soundDelay = 50;
        }
    }

    private void playBootUpSound() {
        if (this.minecraft != null && this.minecraft.player != null) {
            this.minecraft.player.playSound(
                    BOOT_UP_TUNE.get(),
                    0.5F,
                    1.0F
            );
        }
    }

    @Override
    public void onClose() {
        if (backgroundShader != null) {
            backgroundShader.close();
            backgroundShader = null;
        }

        if (foregroundShader != null) {
            foregroundShader.close();
            foregroundShader = null;
        }

        if (!nameField.getValue().equals(blockEntity.getName())) {
            PacketDistributor.sendToServer(new ContainmentNameUpdate(nameField.getValue(), blockEntity.getBlockPos()));
        }

        super.onClose();
    }

    @Override
    public void resize(@NotNull Minecraft minecraft, int width, int height) {
        super.resize(minecraft, width, height);
        if (backgroundShader != null) {
            backgroundShader.resize(width, height);
        }
        if (foregroundShader != null) {
            foregroundShader.resize(width, height);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
