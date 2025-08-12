package com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.site21.bittermelon.content.personnel.PrivilegeGroup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class PrivilegeGroupListWidget extends ObjectSelectionList<PrivilegeGroupListWidget.Entry> {

    public PrivilegeGroupListWidget(Minecraft minecraft, int width, int height, int y, int itemHeight) {
        super(minecraft, width, height, y, itemHeight);
    }

    void refreshList(@NotNull Collection<PrivilegeGroup> groups) {
        clearEntries();
        for (PrivilegeGroup group : groups) {
            addEntry(new PrivilegeGroupListWidget.Entry(group));
        }
    }

    @Override
    protected int getScrollbarPosition() {
        return getX() + width;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);

        if (this.scrollbarVisible()) {
            int l = this.getScrollbarPosition();
            int i1 = (int) ((float) (this.height * this.height) / (float) this.getMaxPosition());
            i1 = Mth.clamp(i1, 32, this.height - 8);
            int k = (int) this.getScrollAmount() * (this.height - i1) / this.getMaxScroll() + this.getY();
            if (k < this.getY()) {
                k = this.getY();
            }

            RenderSystem.enableBlend();
            guiGraphics.blitSprite(PersonnelTerminalScreen.SCROLLER_BACKGROUND_SPRITE, l, this.getY(), 12, this.getHeight());
            guiGraphics.blitSprite(PersonnelTerminalScreen.SCROLLER_SPRITE, l, k, 12, i1);
            RenderSystem.disableBlend();
        }
    }

    @Override
    protected void renderListBackground(@NotNull GuiGraphics guiGraphics) {
        guiGraphics.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0xFFFFFFFF);
    }

    @Override
    protected void renderItem(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, int index, int left, int top, int width, int height) {
        PrivilegeGroupListWidget.Entry e = this.getEntry(index);
        e.renderBack(guiGraphics, index, top, left, width, height, mouseX, mouseY, Objects.equals(this.getHovered(), e), partialTick);
        if (this.isSelectedItem(index)) {
            int borderColor = e.isMouseOver(mouseX, mouseY) ? 0xFF938DD7 : 0xFF000000;
            int i = this.isFocused() ? borderColor : -8355712;
            this.renderSelection(guiGraphics, top, width, height, i, 0xFF0100AC);
        }

        e.render(guiGraphics, index, top, left, width, height, mouseX, mouseY, Objects.equals(this.getHovered(), e), partialTick);

    }

    public static class Entry extends ObjectSelectionList.Entry<PrivilegeGroupListWidget.Entry> {
        private final PrivilegeGroup user;

        public Entry(PrivilegeGroup user) {
            this.user = user;
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.literal(user.getName());
        }

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, int entryIdx, int top, int left, int entryWidth, int entryHeight,
                           int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
            if (isMouseOver && !isFocused()) {
                guiGraphics.fill(left, top - 2, entryWidth, top + entryHeight + 2, 0xFFD3E3FD);
            }

            String name = user.getName();
            int nameColor = isFocused() ? 0xFFFFFFFF : 0xFF000000 ;

            guiGraphics.drawString(Minecraft.getInstance().font, name, left, top + 2, nameColor, false);
        }
    }
}
