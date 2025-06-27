package com.site21.bittermelon.content.medical.client.screen;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.content.medical.compartments.VisualData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CompartmentNodeWidget extends AbstractWidget {
//    private static final ResourceLocation TITLE_BOX_SPRITE = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/title_box");
//    private static final ResourceLocation BOX_HEALTHY = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/box_healthy");
//    private static final ResourceLocation BOX_DAMAGED = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/box_damaged");
//    private static final ResourceLocation BOX_CRITICAL = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/box_critical");

    private static final ResourceLocation TITLE_BOX_SPRITE = ResourceLocation.withDefaultNamespace("advancements/title_box");
    private static final ResourceLocation BOX_OBTAINED = ResourceLocation.withDefaultNamespace("advancements/box_obtained");
    private static final ResourceLocation BOX_UNOBTAINED = ResourceLocation.withDefaultNamespace("advancements/box_unobtained");
    private static final ResourceLocation FRAME_TASK = ResourceLocation.withDefaultNamespace("advancements/task_frame_unobtained");
    private static final ResourceLocation FRAME_CHALLENGE = ResourceLocation.withDefaultNamespace("advancements/challenge_frame_unobtained");
    private static final ResourceLocation FRAME_GOAL = ResourceLocation.withDefaultNamespace("advancements/goal_frame_unobtained");

    private HealthScreenV2 healthScreen;
    private CompartmentInstance compartment;

    private int relativeX;
    private int relativeY;

    public CompartmentNodeWidget(int x, int y, int width, int height, Component message, HealthScreenV2 healthScreen, CompartmentInstance compartment) {
        super(x, y, width, height, message);
        this.relativeX = x;
        this.relativeY = y;
        this.healthScreen = healthScreen;
        this.compartment = compartment;
    }

    public int getRelativeX() {
        return relativeX;
    }

    public int getRelativeY() {
        return relativeY;
    }

    public void setRelativeX(int relativeX) {
        this.relativeX = relativeX;
    }

    public void setRelativeY(int relativeY) {
        this.relativeY = relativeY;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int i, int i1, float v) {
        if (!compartment.isHidden() || (compartment.getHealthRaw() <= 0)) {
//            ResourceLocation frameSprite = getFrameSpriteForHealth();
//            guiGraphics.blitSprite(frameSprite, this.getX() + 3, this.getY(), 26, 26);

            VisualData visualData = compartment.getVisualData();
            float scaleFactor = visualData.scale;

            guiGraphics.pose().pushPose();

            guiGraphics.pose().translate(this.getX(), this.getY(), visualData.getZ());

            guiGraphics.pose().scale(scaleFactor, scaleFactor, 0);

            if (compartment.getIcon() != null) {
                int width = visualData.width;
                int height = visualData.height;
                RenderSystem.enableBlend();
                guiGraphics.blit(compartment.getIcon(), 0, 0, 0, 0, width, height, width, height);
                RenderSystem.disableBlend();
            } else {
                guiGraphics.renderFakeItem(compartment.getItem(), 0, 0);
            }

            guiGraphics.pose().popPose();

        }
    }

    private float getScaleFactor() {
        float minScale = 0.5f;
        float maxScale = 7.5f;
        float minHealth = 1.0f;
        float maxHealth = 40.0f;

        float healthValue = Math.max(minHealth, Math.min(maxHealth, compartment.getTrueMaxHealth()));

        return minScale + (healthValue - minHealth) * (maxScale - minScale) / (maxHealth - minHealth);
    }

    public void drawHover(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float fade, int screenWidth, int screenHeight) {
        guiGraphics.pose().pushPose();

        guiGraphics.pose().translate(
                0,
                0,
                100
        );

        boolean isRightSide = screenWidth + mouseX + this.getX() + 200 >= healthScreen.width;

        List<Component> tooltipLines = new ArrayList<>();
        tooltipLines.add(Component.literal(compartment.getHealthRaw() + "/" + compartment.getMaxHealth()));
        tooltipLines.add(Component.literal("\uE002 Inspect").withStyle(style -> style.withFont(
                ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "default"))));

        int tooltipWidth = 0;
        for (Component line : tooltipLines) {
            int lineWidth = Minecraft.getInstance().font.width(line);
            if (lineWidth > tooltipWidth) {
                tooltipWidth = lineWidth;
            }
        }
        tooltipWidth += 40;

        int nameLength = Minecraft.getInstance().font.width(compartment.getName());

        if (nameLength + 40 > tooltipWidth) {
            tooltipWidth = nameLength + 40;
        }

        int tooltipX = isRightSide ? this.getX() - tooltipWidth + 36 : this.getX() + 29;
        int tooltipY = this.getY() + 5;

        RenderSystem.enableBlend();
        int y1 = this.getY();
        int x1;
        if (isRightSide) {
            x1 = this.getX() - tooltipWidth + 32;
        } else {
            x1 = this.getX();
        }

        int boxHeight = 32 + tooltipLines.size() * 9;
        guiGraphics.blitSprite(TITLE_BOX_SPRITE, x1, tooltipY, tooltipWidth, boxHeight);

        float healthRatio = compartment.getHealthRaw() / compartment.getMaxHealth();
        int progressWidth = Mth.floor(healthRatio * tooltipWidth);
        int remainingWidth = tooltipWidth - progressWidth;

        guiGraphics.blitSprite(BOX_OBTAINED, 200, 26, 0, 0, x1, y1, progressWidth - 2, 26);
        guiGraphics.blitSprite(BOX_UNOBTAINED, 200, 26, 200 - remainingWidth - 2, 0, x1 + progressWidth - 2, y1, remainingWidth + 2, 26);
        if (healthRatio == 1) {
            guiGraphics.blitSprite(BOX_OBTAINED, 200, 26, 198, 0, x1 + progressWidth - 2, y1, 2, 26);
        }

        ResourceLocation frameSprite = getFrameSpriteForHealth();
        guiGraphics.blitSprite(frameSprite, this.getX() + 3, this.getY(), 26, 26);

        guiGraphics.drawString(Minecraft.getInstance().font, compartment.getName(), tooltipX + 5, tooltipY + 3, -1);
        for (int i = 0; i < tooltipLines.size(); i++) {
            guiGraphics.drawString(Minecraft.getInstance().font, tooltipLines.get(i), tooltipX + 5, tooltipY + 20 + (i * 12), -1);
        }

        if (compartment.getIcon() != null && compartment.getItem().isEmpty()) {
            guiGraphics.blitSprite(compartment.getIcon(), this.getX() + 8, this.getY() + 5, 16, 16);
        } else {
            guiGraphics.renderFakeItem(compartment.getItem(), this.getX() + 8, this.getY() + 5);
        }

        guiGraphics.pose().popPose();
    }


    private ResourceLocation getFrameSpriteForHealth() {
        float healthPercent = compartment.getHealthRaw() / compartment.getMaxHealth();
        if (healthPercent > 0.66f) {
            return FRAME_TASK;
        } else if (healthPercent > 0.33f) {
            return FRAME_GOAL;
        } else {
            return FRAME_CHALLENGE;
        }
    }

    private ResourceLocation getBoxSpriteForHealth() {
        float healthPercent = compartment.getHealthRaw() / compartment.getMaxHealth();
        if (healthPercent > 0.5f) {
            return BOX_OBTAINED;
        } else {
            return BOX_UNOBTAINED;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 1) {
            healthScreen.addCompartmentSpace(Component.literal(compartment.getName()), compartment.getCompartmentSpace());
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        if (!compartment.isHidden() || (compartment.getHealthRaw() <= 0)) {
            VisualData visualData = compartment.getVisualData();
            float scaleFactor = visualData.scale;

            int centerX = this.getX() + 8;
            int centerY = this.getY() + 8;

            int relX = mouseX - centerX;
            int relY = mouseY - centerY;

            if (compartment.getIcon() == null) {
                float distance = (float) Math.sqrt(relX * relX + relY * relY);
                float hitboxRadius = 8 + (2 * (float) Math.sqrt(scaleFactor));
                return distance <= hitboxRadius;
            }

            float unscaledRelX = relX / scaleFactor;
            float unscaledRelY = relY / scaleFactor;

            int texX = Math.round(unscaledRelX);
            int texY = Math.round(unscaledRelY);

            if (texX >= 0 && texX < visualData.width && texY >= 0 && texY < visualData.height) {
                return getAlphaAtPixel(compartment.getIcon(), texX, texY) >= 1;
            }
        }
        return false;
    }

    private float getAlphaAtPixel(ResourceLocation resourceLocation, int x, int y) {
        NativeImage image;
        try {
            image = NativeImage.read(Minecraft.getInstance().getResourceManager().getResource(resourceLocation).get().open());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (x >= 0 && x < image.getWidth() && y >= 0 && y < image.getHeight()) {
            int rgba = image.getPixelRGBA(x, y);
            int alpha = (rgba >> 24) & 0xFF;
//            System.out.println("Pixel at " + x + "," + y + " - RGBA: " + Integer.toHexString(rgba) + ", Alpha: " + alpha);
            image.close();
            return alpha / 255.0f;
        }

        return 0.5f;
    }

    public CompartmentInstance getCompartment() {
        return compartment;
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {

    }
}
