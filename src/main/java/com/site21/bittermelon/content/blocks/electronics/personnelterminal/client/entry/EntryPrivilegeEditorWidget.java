package com.site21.bittermelon.content.blocks.electronics.personnelterminal.client.entry;

import com.site21.bittermelon.content.blocks.electronics.personnelterminal.client.instanceprivilegeeditor.PrivilegeEditorWidget;
import com.site21.bittermelon.systems.personnel.privilege.networking.RemovePrivilegeForEntry;
import com.site21.bittermelon.systems.personnel.privilege.networking.SetPrivilegeForEntry;
import com.site21.bittermelon.systems.personnel.registry.PersonnelEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jetbrains.annotations.NotNull;

public class EntryPrivilegeEditorWidget extends PrivilegeEditorWidget {
    private final PersonnelEntry entry;

    public EntryPrivilegeEditorWidget(int x, int y, int width, int height, @NotNull PersonnelEntry entry) {
        super(x, y, width, height, Component.literal(entry.getName()), entry);
        this.entry = entry;
    }

    @Override
    public void setPrivilege(String privilege, boolean value) {
        ClientPacketDistributor.sendToServer(new SetPrivilegeForEntry(entry.getId(), privilege, value));
    }

    @Override
    public void removePrivilege(String privilege) {
        ClientPacketDistributor.sendToServer(new RemovePrivilegeForEntry(entry.getId(), privilege));
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, Component.literal(entry.getName()));
    }
}
