package com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client.privilegeeditor;

import com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client.BaseTerminalScreen;
import com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client.BitterButton;
import com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client.PersonnelTerminalScreen;
import com.site21.bittermelon.common.systems.personnel.privilege.PrivilegeGroup;
import com.site21.bittermelon.common.systems.personnel.privilege.PrivilegeManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class PrivilegeEditorScreen extends BaseTerminalScreen {
    private final Screen previousScreen;
    private final PrivilegeManager privilegeManager;

    private PrivilegeGroupListWidget groupList;

    public PrivilegeEditorScreen(Screen previousScreen) {
        super(Component.literal("Privilege Editor"));
        this.previousScreen = previousScreen;
        privilegeManager = PrivilegeManager.get(Minecraft.getInstance().player.level());
    }

    @Override
    protected Component getSearchHint() {
        return Component.literal("Search groups...");
    }

    @Override
    protected void initializeWidgets() {
        int leftX = x + MARGIN;
        int topY = y + 20;
        int bottomY = screenHeight - 25;
        int listHeight = screenHeight - 80 - COMPONENT_SPACE;
        int listWidth = (int) (screenWidth / 2.75);

        int listY = topY + COMPONENT_SPACE + COMPONENT_HEIGHT;
        groupList = new PrivilegeGroupListWidget(Minecraft.getInstance(), listWidth, listHeight, listY, COMPONENT_HEIGHT, this);
        groupList.setX(leftX);
        addRenderableWidget(groupList);

        int backButtonWidth = 40;
        BitterButton backButton = BitterButton.builder(Component.literal("Back"), this::onBackButtonPressed, PersonnelTerminalScreen.BUTTON_SPRITES)
                .bounds(leftX, bottomY, backButtonWidth, COMPONENT_HEIGHT)
                .build();
        addRenderableWidget(backButton);

        int addGroupButtonX = leftX + backButtonWidth + COMPONENT_SPACE;
        int addGroupButtonWidth = 65;
        BitterButton addPrivilegeGroupButton = BitterButton.builder(Component.literal("Add Group"), this::onAddPrivilegeGroupButton, PersonnelTerminalScreen.BUTTON_SPRITES)
                .bounds(addGroupButtonX, bottomY, addGroupButtonWidth, COMPONENT_HEIGHT)
                .build();
        addRenderableWidget(addPrivilegeGroupButton);

        int editPrivilegesX = addGroupButtonX + addGroupButtonWidth + COMPONENT_SPACE;
        int editPrivilegesWidth = 100;
        BitterButton editPrivilegesButton = BitterButton.builder(Component.literal("Manage Privileges"), this::onEditPrivilegesButtonPressed, PersonnelTerminalScreen.BUTTON_SPRITES)
                .bounds(editPrivilegesX, bottomY, editPrivilegesWidth, COMPONENT_HEIGHT)
                .build();
        addRenderableWidget(editPrivilegesButton);
    }

    @Override
    protected void refreshContent() {
        Collection<PrivilegeGroup> privilegeGroups = privilegeManager.getPrivilegeGroups().values();

        if (currentSearchTerm.isEmpty()) {
            privilegeGroups = privilegeGroups.stream()
                    .sorted(Comparator.comparing(PrivilegeGroup::getName))
                    .collect(Collectors.toList());
        } else {
            privilegeGroups = privilegeGroups.stream()
                    .filter(this::matchesSearchTerm)
                    .sorted(Comparator.comparing(PrivilegeGroup::getName))
                    .collect(Collectors.toList());
        }

        groupList.refreshList(privilegeGroups);
    }
    public void onBackButtonPressed(Button button) {
        Minecraft.getInstance().setScreen(previousScreen);
    }

    public void onEditPrivilegesButtonPressed(Button button) {
        if (!(activeWidget instanceof PrivilegeWidget)) {
            PrivilegeWidget widget = new PrivilegeWidget(widgetX, widgetY, widgetWidth, widgetHeight);
            setActiveWidget(widget);
        } else {
            setActiveWidget(null);
        }
    }

    public void onAddPrivilegeGroupButton(Button button) {
        if (!(activeWidget instanceof AddPrivilegeGroupWidget)) {
            AddPrivilegeGroupWidget widget = new AddPrivilegeGroupWidget(widgetX, widgetY, width / 3, 40, this);
            setActiveWidget(widget);
        } else {
            setActiveWidget(null);
        }
    }

    public void addPrivilegeGroup(String name) {
        PrivilegeGroup group = new PrivilegeGroup(name);
        privilegeManager.addPrivilegeGroup(group);

        refreshContent();
    }

    private boolean matchesSearchTerm(@NotNull PrivilegeGroup entry) {
        String searchTerm = currentSearchTerm.toLowerCase();
        return (entry.getName() != null && entry.getName().toLowerCase().contains(searchTerm));
    }

    public void onGroupSelected(@NotNull PrivilegeGroup group) {
        PrivilegeGroupEditorWidget groupWidget = new PrivilegeGroupEditorWidget(
                widgetX,
                widgetY,
                widgetWidth,
                widgetHeight,
                group);
        setActiveWidget(groupWidget);
    }
}
