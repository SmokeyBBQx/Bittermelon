package com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.client.privilegeeditor;

import com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.client.DeletableListWidget;
import com.site21.bittermelon.content.personnel.privilege.PrivilegeManager;
import com.site21.bittermelon.content.personnel.privilege.networking.RemovePrivilege;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@OnlyIn(Dist.CLIENT)
public class PrivilegeListWidget extends DeletableListWidget<PrivilegeListWidget.Entry> {

    public PrivilegeListWidget(Minecraft minecraft, int width, int height, int y, int itemHeight) {
        super(minecraft, width, height, y, itemHeight);
    }

    void refreshList(@NotNull Collection<String> privileges) {
        clearEntries();
        for (String privilege : privileges) {
            addEntry(new PrivilegeListWidget.Entry(privilege));
        }
    }

    public class Entry extends DeletableEntry {
        private final String privilege;

        public Entry(String privilege) {
            this.privilege = privilege;
        }

        @Override
        protected String getDisplayName() {
            return privilege;
        }

        @Override
        protected String getIconPath() {
            return "retro/keys";
        }

        @Override
        protected void onDeleteClicked() {
            removeEntry(this);
            PacketDistributor.sendToServer(new RemovePrivilege(privilege));
            PrivilegeManager.get(minecraft.level).removePrivilege(privilege);
        }

        @Override
        protected boolean hasDeleteButton() {
            return true;
        }
    }
}
