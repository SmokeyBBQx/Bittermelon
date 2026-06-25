package com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
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

        protected abstract String getDisplayName();
        protected abstract String getIconPath();
        protected abstract void onDeleteClicked();
        protected abstract boolean hasDeleteButton();

        private int getDeleteButtonX() {
            return getContentRight() - ICON_SIZE - ICON_OFFSET;
        }

        private @Nullable Identifier getDeleteButtonIcon(int mouseX, int mouseY) {
            if (!hasDeleteButton()) return null;

            int deleteButtonX = getDeleteButtonX();

            if (mouseX >= deleteButtonX && mouseX <= deleteButtonX + BUTTON_SIZE &&
                    mouseY >= getY() && mouseY <= getY() + BUTTON_SIZE) {
                return Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/trash_button_highlighted");
            } else {
                return Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/trash_button");
            }
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
            if (event.button() != 0) return false;

            if (hasDeleteButton()) {
                int deleteButtonX = getDeleteButtonX();
                int buttonY = getY();

                if (event.x() >= deleteButtonX && event.x() <= deleteButtonX + BUTTON_SIZE &&
                        event.y() >= buttonY && event.y() <= buttonY + BUTTON_SIZE) {
                    onDeleteClicked();
                    return true;
                }
            }

            return onNonDeleteClick(event, doubleClick);
        }

        protected boolean onNonDeleteClick(MouseButtonEvent event, boolean doubleClick) {
            return true;
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.literal(getDisplayName());
        }

        @Override
        public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
            if (isMouseOver(mouseX, mouseY) && !isFocused()) {
                graphics.fill(getX(), getY() - 2, getContentRight(), getContentBottom() + 2, 0xFFD3E3FD);
            }

            Identifier icon = Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, getIconPath());
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, icon, getX() + 4, getY(), ICON_SIZE, ICON_SIZE);

            if (hasDeleteButton()) {
                Identifier deleteIcon = getDeleteButtonIcon(mouseX, mouseY);
                if (deleteIcon != null) {
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, deleteIcon, getDeleteButtonX(), getY(), BUTTON_SIZE, BUTTON_SIZE);
                }
            }

            int textColor = isFocused() ? 0xFFFFFFFF : 0xFF000000;
            graphics.text(Minecraft.getInstance().font, getDisplayName(), getX() + TEXT_OFFSET, getY() + 2, textColor, false);
        }
    }
}
