package com.site21.bittermelon.common.systems.character.client.charactereditor.roleselection;

import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.client.characterselection.CharacterSelectionScreen;
import com.site21.bittermelon.common.systems.roles.FoundationRole;
import com.site21.bittermelon.common.systems.roles.Role;
import com.site21.bittermelon.common.systems.roles.networking.AddRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.site21.bittermelon.init.custom.Roles.ROLES;

@OnlyIn(Dist.CLIENT)
public class RoleSelectionScreen extends Screen {
    private final Screen previousScreen;
    private final Character character;

    private RoleListWidget listWidget;
    private RoleListWidget.Entry selectedRole;
    private Button confirmButton;
    private EditBox searchField;
    private String currentSearchTerm = "";

    public RoleSelectionScreen(Screen previousScreen, Character character) {
        super(Component.literal("Role Selection"));
        this.previousScreen = previousScreen;
        this.character = character;
    }

    @Override
    protected void init() {
        int margin = 20;
        int listWidth = width / 3;
        int listHeight = height - margin - 40;
        int listX = width / 2 - listWidth * 2 / 2;

        listWidget = new RoleListWidget(minecraft, listWidth, listHeight, margin, 40, this);
        listWidget.setX(listX);

        confirmButton = Button.builder(Component.literal("Confirm"), this::onConfirm)
                .bounds(listWidget.getRight() + 5, 0, 50, 20)
                .build();
        confirmButton.visible = false;

        searchField = new EditBox(font, listX, margin + listHeight, listWidth, 20, Component.literal("Search"));
        searchField.setResponder(this::onSearchChanged);
        searchField.setHint(Component.literal("Search..."));

        addRenderableWidget(listWidget);
        addRenderableWidget(confirmButton);
        addRenderableWidget(searchField);

        refreshRoles();
    }

    private void onConfirm(Button button) {
        PacketDistributor.sendToServer(new AddRole(character.getUUID(), selectedRole.getRoleHolder()));
        if (previousScreen instanceof CharacterSelectionScreen screen) {
            screen.switchCharacter(character);
            onClose();
        }
    }

    private void onSearchChanged(@NotNull String search) {
        currentSearchTerm = search.toLowerCase();
        refreshRoles();
    }

    private void refreshRoles() {
        Collection<DeferredHolder<Role, ? extends Role>> roles = ROLES.getEntries();

        if (!currentSearchTerm.isEmpty()) {
            roles = roles.stream().filter(holder -> matchesSearchTerm(holder.value())).toList();
        }

        listWidget.refreshList(roles);
    }

    private boolean matchesSearchTerm(Role role) {
        if (role instanceof FoundationRole foundationRole) {
            return role.name.toLowerCase().contains(currentSearchTerm)
                    || foundationRole.department.toLowerCase().contains(currentSearchTerm)
                    || foundationRole.position.toLowerCase().contains(currentSearchTerm);
        }

        return role.name.toLowerCase().contains(currentSearchTerm);
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

        guiGraphics.fill(x - 2, y - 2, x + listWidget.getWidth(), y + descriptionHeight + font.lineHeight + 12, 0x44000000);

        guiGraphics.drawString(font, role.name, x, y, 0xFFFFFF);
        guiGraphics.drawWordWrap(font, Component.literal(role.description), x, y + 15, listWidget.getWidth(), 0xFFFFFF);
    }

    public Character getCharacter() {
        return character;
    }
}
