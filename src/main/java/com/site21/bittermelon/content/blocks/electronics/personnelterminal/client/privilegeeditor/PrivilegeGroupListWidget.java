package com.site21.bittermelon.content.blocks.electronics.personnelterminal.client.privilegeeditor;

import com.site21.bittermelon.content.blocks.electronics.personnelterminal.client.DeletableListWidget;
import com.site21.bittermelon.systems.personnel.privilege.PrivilegeGroup;
import com.site21.bittermelon.systems.personnel.privilege.networking.RemovePrivilegeGroup;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

@OnlyIn(Dist.CLIENT)
public class PrivilegeGroupListWidget extends DeletableListWidget<PrivilegeGroupListWidget.Entry> {
    private final PrivilegeEditorScreen screen;

    public PrivilegeGroupListWidget(Minecraft minecraft, int width, int height, int y, int itemHeight, PrivilegeEditorScreen screen) {
        super(minecraft, width, height, y, itemHeight);
        this.screen = screen;
    }

    void refreshList(@NotNull Collection<PrivilegeGroup> privilegeGroups) {
        clearEntries();
        for (PrivilegeGroup group : privilegeGroups) {
            addEntry(new Entry(group));
        }
    }

    public class Entry extends DeletableEntry {
        private final PrivilegeGroup group;

        public Entry(PrivilegeGroup group) {
            this.group = group;
        }

        @Override
        protected String getDisplayName() {
            return group.getName();
        }

        @Override
        protected String getIconPath() {
            return "retro/users";
        }

        @Override
        protected void onDeleteClicked() {
            ClientPacketDistributor.sendToServer(new RemovePrivilegeGroup(group.getName()));
            removeEntry(this);
        }

        @Override
        protected boolean hasDeleteButton() {
            return true;
        }

        @Override
        protected boolean onNonDeleteClick(double mouseX, double mouseY, int button) {
            screen.onGroupSelected(group);
            return true;
        }
    }
}
