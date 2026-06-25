package com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client;

import com.site21.bittermelon.common.systems.personnel.registry.PersonnelEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

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
        private final Font font;

        Entry(PersonnelEntry entry) {
            this.entry = entry;
            this.font = Minecraft.getInstance().font;
        }

        public PersonnelEntry getPersonnelEntry() {
            return entry;
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.translatable("narrator.select", entry.getName());
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
            if (event.button() == 0) {
                ((PersonnelListWidget) list).screen.onEntrySelected(this);
                return true;
            }
            return false;
        }

        @Override
        public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
            if (isMouseOver(mouseX, mouseY) && !isFocused()) {
                graphics.fill(getX(), getY() - 2, getContentRight(), getContentBottom() + 2, 0xFFD3E3FD);
            }

            Component name = Component.literal(entry.getName());
            Component occupation = Component.literal(entry.getOccupation());
            int nameColor = isFocused() ? 0xFFFFFFFF : 0xFF000000;
            int occupationColor = 0xFF808080;

            int leftX = getX() + 5;
            int y = getY() + 2;
            graphics.text(font, name, leftX, y, nameColor, false);
            graphics.text(font, occupation, leftX, y + 1 + font.lineHeight, occupationColor, false);

            String idString = "ID: " + entry.getId();
            int idWidth = font.width(idString);
            int idColor = 0xFFAAAAAA;

            int idX = getContentRight() - idWidth - 5;
            graphics.text(font, idString, idX, y, idColor, false);
        }
    }
}
