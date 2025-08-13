package com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.client.privilegeeditor;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.client.ListWidget;
import com.site21.bittermelon.content.personnel.privilege.PrivilegeGroup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@OnlyIn(Dist.CLIENT)
public class PrivilegeGroupListWidget extends ListWidget<PrivilegeGroupListWidget.Entry> {
    private final PrivilegeEditorScreen screen;

    public PrivilegeGroupListWidget(Minecraft minecraft, int width, int height, int y, int itemHeight, PrivilegeEditorScreen screen) {
        super(minecraft, width, height, y, itemHeight);
        this.screen = screen;
    }

    void refreshList(@NotNull Collection<PrivilegeGroup> privilegeGroups) {
        clearEntries();
        for (PrivilegeGroup group : privilegeGroups) {
            addEntry(new PrivilegeGroupListWidget.Entry(group));
        }
    }

    public class Entry extends ObjectSelectionList.Entry<PrivilegeGroupListWidget.Entry> {
        private final PrivilegeGroup group;

        public Entry(PrivilegeGroup group) {
            this.group = group;
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.literal(group.getName());
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0) {
                screen.onGroupSelected(group);
                return true;
            }
            return false;
        }

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, int entryIdx, int top, int left, int entryWidth, int entryHeight,
                           int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
            if (isMouseOver && !isFocused()) {
                guiGraphics.fill(left - 20, top - 2, left + entryWidth, top + entryHeight + 2, 0xFFD3E3FD);
            }

            ResourceLocation icon = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/users");

            guiGraphics.blitSprite(icon, left + 6, top, 16, 16);

            int textColor = isFocused() ? 0xFFFFFFFF : 0xFF000000;
            guiGraphics.drawString(Minecraft.getInstance().font, group.getName(), left + 24, top + 2, textColor, false);
        }
    }
}
