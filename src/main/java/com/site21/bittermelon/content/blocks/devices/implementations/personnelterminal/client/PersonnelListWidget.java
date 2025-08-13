package com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.client;

import com.site21.bittermelon.content.personnel.registry.PersonnelEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class PersonnelListWidget extends ListWidget<PersonnelListWidget.Entry> {
    private final PersonnelTerminalScreen screen;

    public PersonnelListWidget(Minecraft minecraft, int width, int height, int y, int itemHeight, PersonnelTerminalScreen screen) {
        super(minecraft, width, height, y, itemHeight);
        this.screen = screen;
    }

    void refreshList(@NotNull Collection<PersonnelEntry> entries) {
        clearEntries();
        for (PersonnelEntry entry : entries) {
            addEntry(new Entry(entry));
        }
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
                ((PersonnelListWidget) list).screen.onEntrySelected(this);
                return true;
            }
            return false;
        }

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, int entryIdx, int top, int left, int entryWidth, int entryHeight,
                           int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
            Minecraft mc = Minecraft.getInstance();

            if (isMouseOver && !isFocused()) {
                guiGraphics.fill(left, top - 2, left + entryWidth, top + entryHeight + 2, 0xFFD3E3FD);
            }

            Component name = Component.literal(entry.getName());
            Component occupation = Component.literal(entry.getOccupation());
            int nameColor = isFocused() ? 0xFFFFFF : 0;
            int occupationColor = 8421504;

            int leftX = left + 5;
            int y = top + 2;
            guiGraphics.drawString(mc.font, name, leftX, y, nameColor, false);
            guiGraphics.drawString(mc.font, occupation, leftX, y + 1 + mc.font.lineHeight, occupationColor, false);

            String idString = "ID: " + entry.getId();
            int idWidth = mc.font.width(idString);
            int idColor = 11184810;

            int idX = left + entryWidth - idWidth - entryWidth / 24 - 12;
            guiGraphics.drawString(mc.font, idString, idX, y, idColor, false);
        }
    }
}
