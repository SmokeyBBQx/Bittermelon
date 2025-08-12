package com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class PrivilegeListWidget extends ObjectSelectionList<PrivilegeListWidget.Entry> {

    public PrivilegeListWidget(Minecraft p_94442_, int p_94443_, int p_94444_, int p_94445_, int p_94446_) {
        super(p_94442_, p_94443_, p_94444_, p_94445_, p_94446_);
    }

    public static class Entry extends ObjectSelectionList.Entry<PrivilegeListWidget.Entry> {

        @Override
        public Component getNarration() {
            return null;
        }

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, int i, int i1, int i2, int i3, int i4, int i5, int i6, boolean b, float v) {

        }
    }
}
