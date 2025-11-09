package com.site21.bittermelon.common.systems.electronics.privilege.client;

import com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client.instanceprivilegeeditor.PrivilegeEditorWidget;
import com.site21.bittermelon.common.systems.personnel.privilege.PrivilegeOwner;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PrivilegeListWidget extends PrivilegeEditorWidget {
    private final PrivilegeEditorScreen parentScreen;

    public PrivilegeListWidget(int x, int y, int width, int height, Component message, PrivilegeOwner privilegeOwner, PrivilegeEditorScreen parentScreen) {
        super(x, y, width, height, message, privilegeOwner);
        this.parentScreen = parentScreen;
    }

    @Override
    public void setPrivilege(String privilege, boolean value) {
        parentScreen.setPrivilege(privilege, value);
    }

    @Override
    public void removePrivilege(String privilege) {
        parentScreen.removePrivilege(privilege);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
