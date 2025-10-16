package com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client.instanceprivilegeeditor;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client.ListWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
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
        private int left = 0;
        private int top = 0;
        private int entryWidth = 0;

        public Entry(String privilege, boolean value) {
            this.privilege = privilege;
            this.value = value;
        }

        @Override
        public @NotNull net.minecraft.network.chat.Component getNarration() {
            return net.minecraft.network.chat.Component.literal(privilege);
        }

        private int getToggleButtonX() {
            return left + entryWidth - 44;
        }

        private int getDeleteButtonX() {
            return left + entryWidth - 22;
        }

        private int getButtonY() {
            return top - 2;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button != 0) return false;

            int toggleButtonX = getToggleButtonX();
            int deleteButtonX = getDeleteButtonX();
            int buttonY = getButtonY();

            if (mouseX >= toggleButtonX && mouseX <= toggleButtonX + BUTTON_SIZE &&
                    mouseY >= buttonY && mouseY <= buttonY + BUTTON_SIZE) {
                value = !value;
                parent.setPrivilege(privilege, value);
                return true;
            }

            if (mouseX >= deleteButtonX && mouseX <= deleteButtonX + BUTTON_SIZE &&
                    mouseY >= buttonY && mouseY <= buttonY + BUTTON_SIZE) {
                parent.removePrivilege(privilege);
                removeEntry(this);
                return true;
            }

            return true;
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

            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, getIcon(), left + 4, top, 16, 16);

            int toggleButtonX = getToggleButtonX();
            int deleteButtonX = getDeleteButtonX();
            int buttonY = getButtonY();

            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, getToggleButtonIcon(mouseX, mouseY), toggleButtonX, buttonY, BUTTON_SIZE, BUTTON_SIZE);
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, getDeleteButtonIcon(mouseX, mouseY), deleteButtonX, buttonY, BUTTON_SIZE, BUTTON_SIZE);

            int textColor = isFocused() ? 0xFFFFFFFF : 0xFF000000;
            guiGraphics.drawString(Minecraft.getInstance().font, privilege, left + 22, top + 3, textColor, false);
        }

        public ResourceLocation getIcon() {
            if (privilege.contains("group")) return ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/users");

            return ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/keys");
        }

        public ResourceLocation getToggleButtonIcon(int mouseX, int mouseY) {
            int toggleButtonX = getToggleButtonX();
            int buttonY = getButtonY();

            if (mouseX >= toggleButtonX && mouseX <= toggleButtonX + BUTTON_SIZE &&
                    mouseY >= buttonY && mouseY <= buttonY + BUTTON_SIZE) {
                return value ? ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/true_button_highlighted")
                        : ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/false_button_highlighted");
            } else {
                return value ? ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/true_button")
                        : ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/false_button");
            }
        }

        public ResourceLocation getDeleteButtonIcon(int mouseX, int mouseY) {
            int deleteButtonX = getDeleteButtonX();
            int buttonY = getButtonY();

            if (mouseX >= deleteButtonX && mouseX <= deleteButtonX + BUTTON_SIZE &&
                    mouseY >= buttonY && mouseY <= buttonY + BUTTON_SIZE) {
                return ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/trash_button_highlighted");
            } else {
                return ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/trash_button");
            }
        }
    }
}
