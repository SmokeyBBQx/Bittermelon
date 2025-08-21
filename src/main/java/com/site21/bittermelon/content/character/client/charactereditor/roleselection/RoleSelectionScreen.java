package com.site21.bittermelon.content.character.client.charactereditor.roleselection;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.roles.Role;
import com.site21.bittermelon.content.roles.networking.AddRole;
import com.site21.bittermelon.init.custom.Roles;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.RegistryManager;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.site21.bittermelon.init.custom.Roles.ROLES;

public class RoleSelectionScreen extends Screen {
    private final Screen previousScreen;
    private final Character character;

    private RoleListWidget listWidget;
    private RoleListWidget.Entry selectedRole;
    private Button confirmButton;

    public RoleSelectionScreen(Screen previousScreen, Character character) {
        super(Component.literal("Role Selection"));
        this.previousScreen = previousScreen;
        this.character = character;
    }

    @Override
    protected void init() {
        int listWidth = width / 3;
        int margin = 20;

        listWidget = new RoleListWidget(minecraft, listWidth, height - margin * 2, margin, 40, this);
        listWidget.setX(width / 2 - listWidth * 2 / 2);

        Collection<DeferredHolder<Role, ? extends Role>> roles = ROLES.getEntries();
        listWidget.refreshList(roles);

        confirmButton = Button.builder(Component.literal("Confirm"), this::onConfirm)
                .bounds(listWidget.getRight() + 5, 0, 50, 20)
                .build();
        confirmButton.visible = false;

        addRenderableWidget(listWidget);
        addRenderableWidget(confirmButton);
    }

    public void onConfirm(Button button) {
        PacketDistributor.sendToServer(new AddRole(character.getUUID(), selectedRole.getRoleHolder()));
        minecraft.setScreen(previousScreen);
    }

    public void setSelectedRole(RoleListWidget.@NotNull Entry role) {
        int descriptionHeight = minecraft.font.wordWrapHeight(role.getRole().description, listWidget.getWidth());
        confirmButton.setY(role.y + font.lineHeight + descriptionHeight + 15);
        confirmButton.visible = true;
        selectedRole = role;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        if (selectedRole != null) {
            renderSelectedRole(guiGraphics);
        }
    }

    public void renderSelectedRole(@NotNull GuiGraphics guiGraphics) {
        int x = listWidget.getRight() + 5;
        int y = selectedRole.y;
        Role role = selectedRole.getRole();
        int descriptionHeight = minecraft.font.wordWrapHeight(role.description, listWidget.getWidth());

        guiGraphics.fill(x- 2, y - 2, x + listWidget.getWidth(), y + descriptionHeight + font.lineHeight + 12, 0x44000000);

        guiGraphics.drawString(font, role.name, x, y, 0xFFFFFF);
        guiGraphics.drawWordWrap(font, Component.literal(role.description), x, y + 15, listWidget.getWidth(), 0xFFFFFF);
    }
}
