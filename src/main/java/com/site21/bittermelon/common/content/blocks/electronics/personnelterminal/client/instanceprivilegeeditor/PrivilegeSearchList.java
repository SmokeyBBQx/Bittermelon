package com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client.instanceprivilegeeditor;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client.ListWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
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
        public void render(@NotNull GuiGraphicsExtractor GuiGraphicsExtractor, int entryIdx, int top, int left, int entryWidth, int entryHeight,
                           int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
            if (isMouseOver && !isFocused()) {
                GuiGraphicsExtractor.fill(left, top - 2, left + entryWidth, top + entryHeight + 2, 0xFFD3E3FD);
            }

            Identifier icon = group ? Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/users")
                    : Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/keys");

            GuiGraphicsExtractor.blitSprite(RenderPipelines.GUI_TEXTURED, icon, left + 4, top, 16, 16);

            int textColor = isFocused() ? 0xFFFFFFFF : 0xFF000000;
            GuiGraphicsExtractor.drawString(Minecraft.getInstance().font, privilege, left + 22, top + 2, textColor, false);
        }
    }
}
