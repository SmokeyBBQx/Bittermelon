package com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.site21.bittermelon.content.personnel.PersonnelEntry;
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
public class PersonnelListWidget extends ObjectSelectionList<PersonnelListWidget.Entry> {
    private final PersonnelTerminalScreen parentScreen;

    public PersonnelListWidget(Minecraft minecraft, int width, int height, int y, int itemHeight, PersonnelTerminalScreen parentScreen) {
        super(minecraft, width, height, y, itemHeight);
        this.parentScreen = parentScreen;
    }

    void refreshList(@NotNull Collection<PersonnelEntry> entries) {
        clearEntries();
        for (PersonnelEntry entry : entries) {
            addEntry(new Entry(entry));
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
        guiGraphics.fill(getX(), getY(), getWidth() + 20, getHeight() + 50, 0xFFFFFFFF);
    }

    @Override
    protected void renderItem(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, int index, int left, int top, int width, int height) {
        Entry e = this.getEntry(index);
        e.renderBack(guiGraphics, index, top, left, width, height, mouseX, mouseY, Objects.equals(this.getHovered(), e), partialTick);
        if (this.isSelectedItem(index)) {
            int borderColor = e.isMouseOver(mouseX, mouseY) ? 0xFF938DD7 : 0xFF000000;
            int i = this.isFocused() ? borderColor : -8355712;
            this.renderSelection(guiGraphics, top, width, height, i, 0xFF0100AC);
        }

        e.render(guiGraphics, index, top, left, width, height, mouseX, mouseY, Objects.equals(this.getHovered(), e), partialTick);
    }

    public static class Entry extends ObjectSelectionList.Entry<PersonnelListWidget.Entry> {
        private final PersonnelEntry entry;

        Entry(PersonnelEntry entry) {
            this.entry = entry;
        }

        public PersonnelEntry getPersonnelEntry() {
            return entry;
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.translatable("narrator.select", entry.getName());
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0) {
                ((PersonnelListWidget) list).parentScreen.onEntrySelected(this);
                return true;
            }
            return false;
        }

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, int entryIdx, int top, int left, int entryWidth, int entryHeight,
                           int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
            Minecraft mc = Minecraft.getInstance();

            if (isMouseOver && !isFocused()) {
                guiGraphics.fill(left, top - 2, entryWidth, top + entryHeight + 2, 0xFFD3E3FD);
            }

            Component name = Component.literal(entry.getName());
            Component occupation = Component.literal(entry.getOccupation());
            int nameColor = isFocused() ? 0xFFFFFF : 0;
            int occupationColor = 8421504;

            guiGraphics.drawString(mc.font, name, left + 15, top + 2, nameColor, false);
            guiGraphics.drawString(mc.font, occupation, left + 15, top + 2 + mc.font.lineHeight, occupationColor, false);

            String idString = "ID: " + entry.getId();
            int idWidth = mc.font.width(idString);
            int idColor = 11184810;

            guiGraphics.drawString(mc.font, idString, left + entryWidth - idWidth - 15, top + 2, idColor, false);
        }
    }
}
