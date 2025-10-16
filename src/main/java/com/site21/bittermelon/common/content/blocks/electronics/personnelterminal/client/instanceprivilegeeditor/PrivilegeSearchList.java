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

public class PrivilegeSearchList extends ListWidget<PrivilegeSearchList.Entry> {
    private final PrivilegeEditorWidget parent;

    public PrivilegeSearchList(Minecraft minecraft, int width, int height, int y, int itemHeight, PrivilegeEditorWidget parent) {
        super(minecraft, width, height, y, itemHeight);
        this.parent = parent;
    }

    void refreshList(@NotNull Map<String, Boolean> privileges) {
        clearEntries();
        for (Map.Entry<String, Boolean> privilege : privileges.entrySet()) {
            addEntry(new PrivilegeSearchList.Entry(privilege.getKey(), privilege.getValue()));
        }
    }

    public static class Entry extends ObjectSelectionList.Entry<PrivilegeSearchList.Entry> {
        private final String privilege;
        private final boolean group;

        public Entry(String privilege, boolean group) {
            this.privilege = privilege;
            this.group = group;
        }

        @Override
        public @NotNull net.minecraft.network.chat.Component getNarration() {
            return net.minecraft.network.chat.Component.literal(privilege);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0) {
                ((PrivilegeSearchList) list).parent.setInput(privilege);
                return true;
            }
            return false;
        }

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, int entryIdx, int top, int left, int entryWidth, int entryHeight,
                           int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
            if (isMouseOver && !isFocused()) {
                guiGraphics.fill(left, top - 2, left + entryWidth, top + entryHeight + 2, 0xFFD3E3FD);
            }

            ResourceLocation icon = group ? ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/users")
                    : ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/keys");

            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, icon, left + 4, top, 16, 16);

            int textColor = isFocused() ? 0xFFFFFFFF : 0xFF000000;
            guiGraphics.drawString(Minecraft.getInstance().font, privilege, left + 22, top + 2, textColor, false);
        }
    }
}
