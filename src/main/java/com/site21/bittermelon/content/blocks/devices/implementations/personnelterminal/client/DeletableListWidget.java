package com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.client;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DeletableListWidget<T extends DeletableListWidget.DeletableEntry> extends ListWidget<DeletableListWidget.DeletableEntry> {
    public DeletableListWidget(Minecraft minecraft, int width, int height, int y, int itemHeight) {
        super(minecraft, width, height, y, itemHeight);
    }

    public abstract static class DeletableEntry extends ObjectSelectionList.Entry<DeletableEntry> {
        private static final int BUTTON_SIZE = 20;
        private static final int ICON_SIZE = 16;
        private static final int TEXT_OFFSET = 24;
        private static final int ICON_OFFSET = 6;

        protected int left = 0;
        protected int top = 0;
        protected int entryWidth = 0;

        protected abstract String getDisplayName();
        protected abstract String getIconPath();
        protected abstract void onDeleteClicked();
        protected abstract boolean hasDeleteButton();

        private int getDeleteButtonX() {
            return left + entryWidth - ICON_SIZE - ICON_OFFSET;
        }

        private int getButtonY() {
            return top - 2;
        }

        private @Nullable ResourceLocation getDeleteButtonIcon(int mouseX, int mouseY) {
            if (!hasDeleteButton()) return null;

            int deleteButtonX = getDeleteButtonX();
            int buttonY = getButtonY();

            if (mouseX >= deleteButtonX && mouseX <= deleteButtonX + BUTTON_SIZE &&
                    mouseY >= buttonY && mouseY <= buttonY + BUTTON_SIZE) {
                return ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/trash_button_highlighted");
            } else {
                return ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/trash_button");
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button != 0) return false;

            if (hasDeleteButton()) {
                int deleteButtonX = getDeleteButtonX();
                int buttonY = getButtonY();

                if (mouseX >= deleteButtonX && mouseX <= deleteButtonX + BUTTON_SIZE &&
                        mouseY >= buttonY && mouseY <= buttonY + BUTTON_SIZE) {
                    onDeleteClicked();
                    return true;
                }
            }

            return onNonDeleteClick(mouseX, mouseY, button);
        }

        protected boolean onNonDeleteClick(double mouseX, double mouseY, int button) {
            return true;
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.literal(getDisplayName());
        }

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, int entryIdx, int top, int left, int entryWidth, int entryHeight,
                           int mouseX, int mouseY, boolean isMouseOver, float partialTick) {

            this.left = left;
            this.top = top;
            this.entryWidth = entryWidth;

            if (isMouseOver && !isFocused()) {
                guiGraphics.fill(left, top - 2, left + entryWidth, top + entryHeight + 2, 0xFFD3E3FD);
            }

            ResourceLocation icon = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, getIconPath());
            guiGraphics.blitSprite(icon, left + 4, top, ICON_SIZE, ICON_SIZE);

            if (hasDeleteButton()) {
                ResourceLocation deleteIcon = getDeleteButtonIcon(mouseX, mouseY);
                if (deleteIcon != null) {
                    guiGraphics.blitSprite(deleteIcon, getDeleteButtonX(), getButtonY(), BUTTON_SIZE, BUTTON_SIZE);
                }
            }

            int textColor = isFocused() ? 0xFFFFFFFF : 0xFF000000;
            guiGraphics.drawString(Minecraft.getInstance().font, getDisplayName(), left + TEXT_OFFSET, top + 2, textColor, false);
        }
    }
}
