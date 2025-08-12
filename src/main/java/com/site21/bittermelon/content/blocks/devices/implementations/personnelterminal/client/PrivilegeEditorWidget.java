package com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.personnel.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class PrivilegeEditorWidget extends AbstractWidget {
    private final PersonnelEntryInfoWidget previousWidget;
    private final PrivilegeManager privilegeManager;
    private final PersonnelRegistry personnelRegistry;
    private final PrivilegeGroupListWidget groupList;
    private final EditBox searchField;
    private String currentSearchTerm = "";

    public PrivilegeEditorWidget(int x, int y, int width, int height, Component message, PersonnelEntryInfoWidget previousWidget) {
        super(x, y, width, height, message);
        this.previousWidget = previousWidget;
        privilegeManager = PrivilegeManager.get(Minecraft.getInstance().player.level());
        personnelRegistry = PersonnelRegistry.get(Minecraft.getInstance().player.level());
        groupList = new PrivilegeGroupListWidget(Minecraft.getInstance(), 100, height - 30, y + 25, 20);
        groupList.setX(x + 5);

        searchField = new EditBox(Minecraft.getInstance().font, x + 5, y + 5, 100, 20, Component.literal("Search"));
        searchField.setHint(Component.literal("Search..."));
        searchField.setResponder(this::onSearchChanged);
        searchField.setFGColor(0xFFFFFFFF);

        refreshGroups();
    }

    private void onSearchChanged(@NotNull String searchTerm) {
        currentSearchTerm = searchTerm;
        refreshGroups();
    }

    private void refreshGroups() {
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

    private boolean matchesSearchTerm(@NotNull PrivilegeGroup entry) {
        String searchTerm = currentSearchTerm.toLowerCase();

        return (entry.getName() != null && entry.getName().toLowerCase().contains(searchTerm));
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blitSprite(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/generic_background"), getX(), getY(), getWidth() + 5, getBottom() + 80);

        groupList.render(guiGraphics, mouseX, mouseY, partialTick);
        searchField.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (searchField.mouseClicked(mouseX, mouseY, button)) {
            searchField.setFocused(true);
            return true;
        }
        if (groupList.mouseClicked(mouseX, mouseY, button)) {
            groupList.setFocused(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (searchField.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        if (groupList.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (searchField.charTyped(codePoint, modifiers)) {
            return true;
        }
        if (groupList.charTyped(codePoint, modifiers)) {
            return true;
        }
        return super.charTyped(codePoint, modifiers);
    }
}
