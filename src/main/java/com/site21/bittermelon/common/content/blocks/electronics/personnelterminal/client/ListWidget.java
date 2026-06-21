package com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ListWidget<T extends ObjectSelectionList.Entry<T>> extends ObjectSelectionList<T> {
    public ListWidget(Minecraft minecraft, int width, int height, int y, int itemHeight) {
        super(minecraft, width, height, y, itemHeight);
        centerListVertically = false;
    }

//    @Override
//    protected int getScrollbarPosition() {
//        return getX() + width - 12;
//    }

    @Override
    public int getRowLeft() {
        return x;
    }

    public int getRowWidth() {
        return width - 12;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(GuiGraphicsExtractor, mouseX, mouseY, partialTick);

//        if (this.scrollbarVisible()) {
//            int l = this.getScrollbarPosition();
//            int i1 = (int) ((float) (this.height * this.height) / (float) this.getMaxPosition());
//            i1 = Mth.clamp(i1, 32, this.height - 8);
//            int k = (int) this.getScrollbarPosition() * (this.height - i1) / this.getMaxScroll() + this.getY();
//            if (k < this.getY()) {
//                k = this.getY();
//            }
//
//            RenderSystem.enableBlend();
//            GuiGraphicsExtractor.blitSprite(BitterRenderPipelines.GUI_TEXTURED, PersonnelTerminalScreen.SCROLLER_BACKGROUND_SPRITE, l, this.getY(), 12, this.getHeight());
//            GuiGraphicsExtractor.blitSprite(BitterRenderPipelines.GUI_TEXTURED, PersonnelTerminalScreen.SCROLLER_SPRITE, l, k, 12, i1);
//            RenderSystem.disableBlend();
//        }
    }

    @Override
    protected void renderListBackground(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor) {
        GuiGraphicsExtractor.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0xFFFFFFFF);
    }

    @Override
    protected void renderItem(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor, int mouseX, int mouseY, float partialTick, int index, int left, int top, int width, int height) {
        T e = this.getEntry(index);
        e.renderBack(GuiGraphicsExtractor, index, top, left, width, height, mouseX, mouseY, Objects.equals(this.getHovered(), e), partialTick);
        if (this.isSelectedItem(index)) {
            int borderColor = e.isMouseOver(mouseX, mouseY) ? 0xFF938DD7 : 0xFF000000;
            int i = this.isFocused() ? borderColor : -8355712;
            this.renderSelection(GuiGraphicsExtractor, top, width, height, i, 0xFF0100AC);
        }

        e.render(GuiGraphicsExtractor, index, top, left, width, height, mouseX, mouseY, Objects.equals(this.getHovered(), e), partialTick);
    }

    @Override
    protected void renderSelection(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor, int top, int width, int height, int outerColor, int innerColor) {
        int minX = getRowLeft();
        int maxX = x + getRowWidth();
        GuiGraphicsExtractor.fill(minX, top - 2, maxX, top + height + 2, outerColor);
        GuiGraphicsExtractor.fill(minX + 1, top - 1, maxX - 1, top + height + 1, innerColor);
    }
}
