package com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.client.privilegeeditor;

import com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.client.instanceprivilegeeditor.PrivilegeEditorWidget;
import com.site21.bittermelon.content.personnel.privilege.PrivilegeGroup;
import com.site21.bittermelon.content.personnel.privilege.PrivilegeManager;
import com.site21.bittermelon.content.personnel.privilege.networking.RemovePrivilegeForGroup;
import com.site21.bittermelon.content.personnel.privilege.networking.SetPrivilegeForGroup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class PrivilegeGroupEditorWidget extends PrivilegeEditorWidget {
    private final PrivilegeGroup group;

    public PrivilegeGroupEditorWidget(int x, int y, int width, int height, @NotNull PrivilegeGroup group) {
        super(x, y, width, height, Component.literal(group.getName()), group);
        this.group = group;
    }

    @Override
    public void setPrivilege(String privilege, boolean value) {
        ClientPacketDistributor.sendToServer(new SetPrivilegeForGroup(group.getName(), privilege, value));
    }

    @Override
    public void removePrivilege(String privilege) {
        ClientPacketDistributor.sendToServer(new RemovePrivilegeForGroup(group.getName(), privilege));
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, Component.literal(group.getName()));
    }
}
