package com.site21.bittermelon.content.blocks.devices.privilege.client;

import com.site21.bittermelon.content.blocks.devices.privilege.networking.RemovePrivilegeForBE;
import com.site21.bittermelon.content.blocks.devices.privilege.networking.SetPrivilegeForBE;
import com.site21.bittermelon.content.personnel.privilege.PrivilegeOwner;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;

@OnlyIn(Dist.CLIENT)
public class PrivilegeEditorScreen extends Screen {
    private final BlockEntity blockEntity;
    private PrivilegeOwner privilegeOwner;
    PrivilegeListWidget listWidget;

    public PrivilegeEditorScreen(BlockEntity blockEntity) {
        super(Component.literal("Privilege Editor"));
        this.blockEntity = blockEntity;
        if (blockEntity instanceof PrivilegeOwner owner) {
            privilegeOwner = owner;
        }
    }

    @Override
    protected void init() {
        listWidget = new PrivilegeListWidget(width / 2, height / 3, 200, 200,
                Component.literal("Privileges"), privilegeOwner, this);
        addRenderableWidget(listWidget);
    }

    public void setPrivilege(String privilege, boolean value) {
        privilegeOwner.getPrivileges().put(privilege, value);
        PacketDistributor.sendToServer(new SetPrivilegeForBE(blockEntity.getBlockPos(), privilege, value));
    }

    public void removePrivilege(String privilege) {
        privilegeOwner.getPrivileges().remove(privilege);
        PacketDistributor.sendToServer(new RemovePrivilegeForBE(blockEntity.getBlockPos(), privilege));
    }
}
