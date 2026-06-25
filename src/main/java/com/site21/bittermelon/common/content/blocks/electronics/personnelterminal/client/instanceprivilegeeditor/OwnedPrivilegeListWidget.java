package com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client.instanceprivilegeeditor;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client.ListWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class OwnedPrivilegeListWidget extends ListWidget<OwnedPrivilegeListWidget.Entry> {
    private final PrivilegeEditorWidget parent;

    public OwnedPrivilegeListWidget(Minecraft minecraft, int width, int height, int y, int itemHeight, PrivilegeEditorWidget parent) {
        super(minecraft, width, height, y, itemHeight);
        this.parent = parent;
    }

    void refreshList(@NotNull Map<String, Boolean> privileges) {
        clearEntries();
        for (Map.Entry<String, Boolean> entry : privileges.entrySet()) {
            addEntry(new Entry(entry.getKey(), entry.getValue()));
        }
    }

    public class Entry extends ObjectSelectionList.Entry<OwnedPrivilegeListWidget.Entry> {
        private static final int BUTTON_SIZE = 20;

        private final String privilege;
        private boolean value;

        public Entry(String privilege, boolean value) {
            this.privilege = privilege;
            this.value = value;
        }

        @Override
        public @NotNull net.minecraft.network.chat.Component getNarration() {
            return net.minecraft.network.chat.Component.literal(privilege);
        }

        private int getToggleButtonX() {
            return getRight()- 44;
        }

        private int getDeleteButtonX() {
            return getRight() - 22;
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
            if (event.button() != 0) return false;

            int toggleButtonX = getToggleButtonX();
            int deleteButtonX = getDeleteButtonX();

            if (event.x() >= toggleButtonX && event.x() <= toggleButtonX + BUTTON_SIZE &&
                    event.y() >= getY() && event.y() <= getY() + BUTTON_SIZE) {
                value = !value;
                parent.setPrivilege(privilege, value);
                return true;
            }

            if (event.x() >= deleteButtonX && event.x() <= deleteButtonX + BUTTON_SIZE &&
                    event.y() >= getY() && event.y() <= getY() + BUTTON_SIZE) {
                parent.removePrivilege(privilege);
                removeEntry(this);
                return true;
            }

            return true;
        }

        @Override
        public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
            if (isMouseOver(mouseX, mouseY) && !isFocused()) {
                graphics.fill(getX(),getY() - 2, getRight(), getBottom(),0xFFD3E3FD);
            }

            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, getIcon(), getX() + 4, getY(), 16, 16);

            int toggleButtonX = getToggleButtonX();
            int deleteButtonX = getDeleteButtonX();

            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, getToggleButtonIcon(mouseX, mouseY), toggleButtonX, getY(), BUTTON_SIZE, BUTTON_SIZE);
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, getDeleteButtonIcon(mouseX, mouseY), deleteButtonX, getY(), BUTTON_SIZE, BUTTON_SIZE);

            int textColor = isFocused() ? 0xFFFFFFFF : 0xFF000000;
            graphics.text(Minecraft.getInstance().font, privilege, getX() + 22, getY() + 3, textColor, false);
        }

        public Identifier getIcon() {
            if (privilege.contains("group")) return Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/users");

            return Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/keys");
        }

        public Identifier getToggleButtonIcon(int mouseX, int mouseY) {
            int toggleButtonX = getToggleButtonX();

            if (mouseX >= toggleButtonX && mouseX <= toggleButtonX + BUTTON_SIZE &&
                    mouseY >= getY() && mouseY <= getY() + BUTTON_SIZE) {
                return value ? Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/true_button_highlighted")
                        : Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/false_button_highlighted");
            } else {
                return value ? Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/true_button")
                        : Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/false_button");
            }
        }

        public Identifier getDeleteButtonIcon(int mouseX, int mouseY) {
            int deleteButtonX = getDeleteButtonX();

            if (mouseX >= deleteButtonX && mouseX <= deleteButtonX + BUTTON_SIZE &&
                    mouseY >= getY() && mouseY <= getY() + BUTTON_SIZE) {
                return Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/trash_button_highlighted");
            } else {
                return Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/trash_button");
            }
        }
    }
}
