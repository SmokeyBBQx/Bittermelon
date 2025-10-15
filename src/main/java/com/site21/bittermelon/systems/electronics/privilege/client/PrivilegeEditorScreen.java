package com.site21.bittermelon.systems.electronics.privilege.client;

import com.site21.bittermelon.systems.electronics.privilege.networking.RemovePrivilegeForBE;
import com.site21.bittermelon.systems.electronics.privilege.networking.SetPrivilegeForBE;
import com.site21.bittermelon.systems.personnel.privilege.PrivilegeOwner;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

@OnlyIn(Dist.CLIENT)
public class PrivilegeEditorScreen extends Screen {
    private static final int WIDGET_WIDTH = 300;
    private static final int WIDGET_HEIGHT = 200;

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
        int x = width / 2 - WIDGET_WIDTH / 2;
        int y = height / 2 - WIDGET_HEIGHT / 2;

        listWidget = new PrivilegeListWidget(x, y, WIDGET_WIDTH, WIDGET_HEIGHT,
                Component.literal("Privileges"), privilegeOwner, this);
        addRenderableWidget(listWidget);
    }

    public void setPrivilege(String privilege, boolean value) {
        privilegeOwner.getPrivileges().put(privilege, value);
        ClientPacketDistributor.sendToServer(new SetPrivilegeForBE(blockEntity.getBlockPos(), privilege, value));
    }

    public void removePrivilege(String privilege) {
        privilegeOwner.getPrivileges().remove(privilege);
        ClientPacketDistributor.sendToServer(new RemovePrivilegeForBE(blockEntity.getBlockPos(), privilege));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
