//package com.site21.bittermelon.common.systems.medical.client.widget;
//
//import com.mojang.blaze3d.platform.NativeImage;
//import com.site21.bittermelon.Bittermelon;
//import com.site21.bittermelon.common.systems.medical.client.HealthScreen;
//import com.site21.bittermelon.common.systems.medical.client.HeldItemData;
//import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
//import com.site21.bittermelon.common.systems.medical.compartment.VisualData;
//import com.site21.bittermelon.init.neoforge.BitterSounds;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.GuiGraphicsExtractor;
//import net.minecraft.client.gui.components.AbstractWidget;
//import net.minecraft.client.gui.narration.NarrationElementOutput;
//import net.minecraft.client.renderer.RenderPipelines;
//import net.minecraft.client.resources.sounds.SimpleSoundInstance;
//import net.minecraft.client.sounds.SoundManager;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.Identifier;
//import net.minecraft.util.ARGB;
//import net.minecraft.util.Mth;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import org.jetbrains.annotations.NotNull;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class CompartmentNodeWidget extends AbstractWidget {
//    private static final Identifier TITLE_BOX_SPRITE = Identifier.withDefaultNamespace("advancements/title_box");
//    private static final Identifier BOX_OBTAINED = Identifier.withDefaultNamespace("advancements/box_obtained");
//    private static final Identifier BOX_UNOBTAINED = Identifier.withDefaultNamespace("advancements/box_unobtained");
//    private static final Identifier FRAME_TASK = Identifier.withDefaultNamespace("advancements/task_frame_unobtained");
//    private static final Identifier FRAME_CHALLENGE = Identifier.withDefaultNamespace("advancements/challenge_frame_unobtained");
//    private static final Identifier FRAME_GOAL = Identifier.withDefaultNamespace("advancements/goal_frame_unobtained");
//
//    private final HealthScreen healthScreen;
//    private final CompartmentInstance compartment;
//
//    private int relativeX;
//    private int relativeY;
//    private NativeImage cachedImage = null;
//    private Identifier cachedImageLocation = null;
//
//    public CompartmentNodeWidget(int x, int y, int width, int height, Component message, HealthScreen healthScreen, CompartmentInstance compartment) {
//        super(x, y, width, height, message);
//        this.relativeX = x;
//        this.relativeY = y;
//        this.healthScreen = healthScreen;
//        this.compartment = compartment;
//    }
//
//    public int getRelativeX() {
//        return relativeX;
//    }
//
//    public int getRelativeY() {
//        return relativeY;
//    }
//
//    public void setRelativeX(int relativeX) {
//        this.relativeX = relativeX;
//    }
//
//    public void setRelativeY(int relativeY) {
//        this.relativeY = relativeY;
//    }
//
//    @Override
//    protected void renderWidget(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor, int mouseX, int mouseY, float partialTick) {
////        VisualData visualData = compartment.getVisualData();
////        if (visualData.isHidden) return;
////
////        GuiGraphicsExtractor.pose().pushMatrix();
////
////        GuiGraphicsExtractor.pose().translate(getX(), getY());
////        GuiGraphicsExtractor.pose().rotate(visualData.rotation);
////        GuiGraphicsExtractor.pose().scale(visualData.scale, visualData.scale);
////
////        if (visualData.icon != null) {
////            int width = visualData.width;
////            int height = visualData.height;
////            int color = visualData.color;
////
////            float pulse = (float) (Math.sin(System.currentTimeMillis() / 500.0) * 0.4f + 0.95f);
////            color = isHoveredOrFocused() ? ARGB.color(pulse, color) : color;
////
////            // Apparently the positions need to be offset by 1 for proper collision detection?
////            GuiGraphicsExtractor.blit(RenderPipelines.GUI_TEXTURED, visualData.icon, 1, 1, 0, 0, width, height, width, height, color);
////        } else {
////            GuiGraphicsExtractor.renderFakeItem(new ItemStack(compartment.getItem()), 0, 0);
////        }
////
////        GuiGraphicsExtractor.pose().popMatrix();
//    }
//
//    public void drawHover(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor, int mouseX, int mouseY, float partialTick, int screenWidth, int screenHeight) {
//        boolean isRightSide = screenWidth + mouseX + getX() + 200 >= healthScreen.width;
//
//        // Calculate tooltip dimensions
//        List<Component> tooltipLines = buildTooltipLines();
//        int tooltipWidth = calculateTooltipWidth(tooltipLines);
//
//        // Calculate positions
//        int tooltipX = isRightSide ? getX() - tooltipWidth + 36 : getX() + 29;
//        int tooltipY = getY() + 5;
//        int boxX = isRightSide ? getX() - tooltipWidth + 32 : getX();
//
//        // Render background box
//        int boxHeight = 32 + tooltipLines.size() * 9;
//        GuiGraphicsExtractor.blitSprite(RenderPipelines.GUI_TEXTURED, TITLE_BOX_SPRITE, boxX, tooltipY, tooltipWidth, boxHeight);
//
//        // Render health progress bar
//        renderHealthBar(GuiGraphicsExtractor, boxX, getY(), tooltipWidth);
//
//        // Render text
//        renderTooltipText(GuiGraphicsExtractor, tooltipX, tooltipY, tooltipLines);
//
//        // Render icon
//        if (compartment.getItem() != Items.AIR) {
//            Identifier frameSprite = getFrameSpriteForHealth();
//            GuiGraphicsExtractor.blitSprite(RenderPipelines.GUI_TEXTURED, frameSprite, getX() + 3, getY(), 26, 26);
//            GuiGraphicsExtractor.renderFakeItem(new ItemStack(compartment.getItem()), getX() + 8, getY() + 5);
//        }
//    }
//
//    private @NotNull List<Component> buildTooltipLines() {
//        List<Component> lines = new ArrayList<>();
//        lines.add(Component.literal(Math.round(compartment.getHealth()) + "/" + Math.round(compartment.getMaxHealth())));
//        lines.add(Component.literal("\uE002 Inspect")
//                .withStyle(style -> style.withFont(Bittermelon.resource("default"))));
//        return lines;
//    }
//
//    private int calculateTooltipWidth(@NotNull List<Component> tooltipLines) {
//        int tooltipWidth = 0;
//        for (Component line : tooltipLines) {
//            int lineWidth = Minecraft.getInstance().font.width(line);
//            tooltipWidth = Math.max(tooltipWidth, lineWidth);
//        }
//
//        int nameLength = Minecraft.getInstance().font.width(compartment.getName());
//        return Math.max(tooltipWidth, nameLength) + 40;
//    }
//
//    private void renderHealthBar(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor, int x, int y, int width) {
//        float healthRatio = compartment.getHealth() / compartment.getMaxHealth();
//        int progressWidth = Mth.floor(healthRatio * width);
//        int remainingWidth = width - progressWidth;
//
//        GuiGraphicsExtractor.blitSprite(RenderPipelines.GUI_TEXTURED, BOX_OBTAINED, 200, 26, 0, 0, x, y, progressWidth - 2, 26);
//        GuiGraphicsExtractor.blitSprite(RenderPipelines.GUI_TEXTURED, BOX_UNOBTAINED, 200, 26, 200 - remainingWidth - 2, 0, x + progressWidth - 2, y, remainingWidth + 2, 26);
//
//        if (healthRatio == 1) {
//            GuiGraphicsExtractor.blitSprite(RenderPipelines.GUI_TEXTURED, BOX_OBTAINED, 200, 26, 198, 0, x + progressWidth - 2, y, 2, 26);
//        }
//    }
//
//    private void renderTooltipText(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor, int x, int y, @NotNull List<Component> tooltipLines) {
//        GuiGraphicsExtractor.drawString(Minecraft.getInstance().font, compartment.getName(), x + 5, y + 3, -1);
//
//        for (int i = 0; i < tooltipLines.size(); i++) {
//            GuiGraphicsExtractor.drawString(Minecraft.getInstance().font, tooltipLines.get(i), x + 5, y + 20 + (i * 12), -1);
//        }
//    }
//
//    private Identifier getFrameSpriteForHealth() {
//        float healthPercent = compartment.getHealth() / compartment.getMaxHealth();
//        if (healthPercent > 0.66f) {
//            return FRAME_TASK;
//        } else if (healthPercent > 0.33f) {
//            return FRAME_GOAL;
//        } else {
//            return FRAME_CHALLENGE;
//        }
//    }
//
//    public boolean mouseClicked(double mouseX, double mouseY, int button, CompartmentSpaceWidget parent) {
//        if (button == 1) {
//            healthScreen.addCompartmentSpace(compartment);
//            return true;
//        } else if (healthScreen.getHeldItemData() == null) {
//            playDownSound(Minecraft.getInstance().getSoundManager());
//            visible = false;
//            compartment.getVisualData().isHidden(true);
//            healthScreen.setHeldItemData(new HeldItemData(compartment.getCompartment().createItemStack(compartment), parent));
//        }
//        return super.mouseClicked(mouseX, mouseY, button);
//    }
//
//    @Override
//    public void playDownSound(@NotNull SoundManager soundManager) {
//        soundManager.play(SimpleSoundInstance.forUI(BitterSounds.SPLATTER, 1.0F));
//    }
//
//    @Override
//    public boolean isMouseOver(double mouseX, double mouseY) {
//        if (compartment.getVisualData().isHidden) return false;
//
//        VisualData visualData = compartment.getVisualData();
//        float scaleFactor = visualData.scale;
//        float rotation = visualData.rotation;
//
//        double localX = mouseX - getX();
//        double localY = mouseY - getY();
//
//        double inverseRotation = -rotation;
//        double cosTheta = Math.cos(inverseRotation);
//        double sinTheta = Math.sin(inverseRotation);
//
//        double rotatedX = localX * cosTheta - localY * sinTheta;
//        double rotatedY = localX * sinTheta + localY * cosTheta;
//
//        double unscaledX = rotatedX / scaleFactor;
//        double unscaledY = rotatedY / scaleFactor;
//
//        if (compartment.getVisualData().icon == null) {
//            return unscaledX >= 0 && unscaledX <= 16 && unscaledY >= 0 && unscaledY <= 16;
//        }
//
//        int texX = (int) Math.floor(unscaledX - 1);
//        int texY = (int) Math.floor(unscaledY - 1);
//
//        if (texX >= 0 && texX < visualData.width && texY >= 0 && texY < visualData.height) {
//            return getAlphaAtPixel(compartment.getVisualData().icon, texX, texY) >= 1;
//        }
//
//        return false;
//    }
//
//    private float getAlphaAtPixel(Identifier Identifier, int x, int y) {
//        if (cachedImage == null || !Identifier.equals(cachedImageLocation)) {
//            if (cachedImage != null) {
//                cachedImage.close();
//            }
//
//            try {
//                cachedImage = NativeImage.read(Minecraft.getInstance().getResourceManager().getResource(Identifier).get().open());
//                cachedImageLocation = Identifier;
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        if (x >= 0 && x < cachedImage.getWidth() && y >= 0 && y < cachedImage.getHeight()) {
//            int rgba = cachedImage.getPixel(x, y);
//            // System.out.println("Alpha at (" + x + ", " + y + "): " + ARGB.alphaFloat(rgba) + "from compartment " + compartment.getName());
//            return ARGB.alphaFloat(rgba);
//        }
//
//        return 0;
//    }
//
//    public void cleanup() {
//        if (cachedImage != null) {
//            cachedImage.close();
//            cachedImage = null;
//            cachedImageLocation = null;
//        }
//    }
//
//    public CompartmentInstance getCompartment() {
//        return compartment;
//    }
//
//    @Override
//    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {
//
//    }
//}
