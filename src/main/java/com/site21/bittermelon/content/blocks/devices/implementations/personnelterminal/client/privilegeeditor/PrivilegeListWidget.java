package com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.client.privilegeeditor;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.client.ListWidget;
import com.site21.bittermelon.content.personnel.privilege.PrivilegeManager;
import com.site21.bittermelon.content.personnel.privilege.networking.RemovePrivilege;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@OnlyIn(Dist.CLIENT)
public class PrivilegeListWidget extends ListWidget<PrivilegeListWidget.Entry> {

    public PrivilegeListWidget(Minecraft minecraft, int width, int height, int y, int itemHeight) {
        super(minecraft, width, height, y, itemHeight);
    }

    void refreshList(@NotNull Collection<String> privileges) {
        clearEntries();
        for (String privilege : privileges) {
            addEntry(new PrivilegeListWidget.Entry(privilege));
        }
    }

    public class Entry extends ObjectSelectionList.Entry<PrivilegeListWidget.Entry> {
        private static final int BUTTON_SIZE = 20;

        private final String privilege;
        private int left = 0;
        private int top = 0;
        private int entryWidth = 0;

        public Entry(String privilege) {
            this.privilege = privilege;
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

            int deleteButtonX = getDeleteButtonX();
            int buttonY = getButtonY();

            if (mouseX >= deleteButtonX && mouseX <= deleteButtonX + BUTTON_SIZE &&
                    mouseY >= buttonY && mouseY <= buttonY + BUTTON_SIZE) {
                removeEntry(this);
                PacketDistributor.sendToServer(new RemovePrivilege(privilege));
                PrivilegeManager.get(Minecraft.getInstance().level).removePrivilege(privilege);
                return true;
            }

            return true;
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.literal(privilege);
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

            ResourceLocation icon = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/keys");
            guiGraphics.blitSprite(icon, left - 22, top, 16, 16);

            guiGraphics.blitSprite(getDeleteButtonIcon(mouseX, mouseY), getDeleteButtonX(), getButtonY(), BUTTON_SIZE, BUTTON_SIZE);

            int textColor = isFocused() ? 0xFFFFFFFF : 0xFF000000;
            guiGraphics.drawString(Minecraft.getInstance().font, privilege, left, top + 2, textColor, false);
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
