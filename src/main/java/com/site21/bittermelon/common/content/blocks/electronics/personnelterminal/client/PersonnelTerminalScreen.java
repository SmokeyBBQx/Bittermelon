package com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.PersonnelTerminalBlockEntity;
import com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client.entry.PersonnelEntryInfoWidget;
import com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client.privilegeeditor.PrivilegeEditorScreen;
import com.site21.bittermelon.common.systems.personnel.registry.PersonnelEntry;
import com.site21.bittermelon.common.systems.personnel.registry.PersonnelRegistry;
import com.site21.bittermelon.init.neoforge.BitterSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class PersonnelTerminalScreen extends BaseTerminalScreen {
    public static final WidgetSprites BUTTON_SPRITES = new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/button"),
            ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/button_disabled"),
            ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/button_highlighted")
    );
    public static final ResourceLocation SCROLLER_SPRITE = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/scroller");
    public static final ResourceLocation SCROLLER_BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("widget/scroller_background");

    private final PersonnelRegistry registry;
    private final PersonnelTerminalBlockEntity terminal;

    private PersonnelListWidget personnelList;
    private PersonnelListWidget.Entry selectedEntry;

    public PersonnelTerminalScreen(PersonnelTerminalBlockEntity terminal) {
        super(Component.literal("Personnel Terminal"));
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(BitterSounds.COMPUTER_START, 1f));
        assert Minecraft.getInstance().player != null;
        registry = PersonnelRegistry.get(Minecraft.getInstance().player.level());
        this.terminal = terminal;
    }

    @Override
    protected Component getSearchHint() {
        return Component.literal("Search personnel...");
    }

    @Override
    protected void initializeWidgets() {
        int leftX = x + MARGIN;
        int topY = y + 20;
        int bottomY = screenHeight - 25;
        int listHeight = screenHeight - 80 - COMPONENT_SPACE;
        int listWidth = (int) (screenWidth / 2.75);

        int privilegeButtonWidth = 80;
        Button privilegeEditorButton = BitterButton.builder(Component.literal("Edit Privileges"), this::onPrivilegeEditorPressed, BUTTON_SPRITES)
                .bounds(leftX, bottomY, privilegeButtonWidth, COMPONENT_HEIGHT)
                .build();
        addRenderableWidget(privilegeEditorButton);

        int listY = topY + COMPONENT_SPACE + COMPONENT_HEIGHT;
        int itemHeight = 25;
        personnelList = new PersonnelListWidget(Minecraft.getInstance(), listWidth, listHeight, listY, itemHeight, this);
        personnelList.setX(leftX);
        addRenderableWidget(personnelList);

        widgetHeight = listHeight;
        widgetY = listY;
    }

    @Override
    public void refreshContent() {
        Map<Integer, PersonnelEntry> entries = registry.getPersonnelEntries();

        Collection<PersonnelEntry> filteredEntries;
        if (currentSearchTerm.isEmpty()) {
            filteredEntries = entries.values().stream()
                    .sorted(Comparator.comparing(PersonnelEntry::getName))
                    .collect(Collectors.toList());
        } else {
            filteredEntries = entries.values().stream()
                    .filter(this::matchesSearchTerm)
                    .sorted(Comparator.comparing(PersonnelEntry::getName))
                    .collect(Collectors.toList());
        }

        personnelList.refreshList(filteredEntries);

        if (selectedEntry != null) {
            PersonnelEntry currentEntry = selectedEntry.getPersonnelEntry();
            boolean entryStillExists = filteredEntries.stream()
                    .anyMatch(entry -> entry.getId() == currentEntry.getId());

            if (!entryStillExists) {
                onEntrySelected(null);
            }
        }
    }

    private void onPrivilegeEditorPressed(Button button) {
        PrivilegeEditorScreen editorScreen = new PrivilegeEditorScreen(this);
        Minecraft.getInstance().setScreen(editorScreen);
    }

    private boolean matchesSearchTerm(@NotNull PersonnelEntry entry) {
        String searchTerm = currentSearchTerm.toLowerCase();

        return (entry.getName() != null && entry.getName().toLowerCase().contains(searchTerm)) ||
                (entry.getOccupation() != null && entry.getOccupation().toLowerCase().contains(searchTerm)) ||
                (entry.getDepartment() != null && entry.getDepartment().toLowerCase().contains(searchTerm)) ||
                (entry.getNotes() != null && entry.getNotes().toLowerCase().contains(searchTerm)) ||
                String.valueOf(entry.getId()).contains(searchTerm);
    }

    public void onEntrySelected(PersonnelListWidget.Entry entry) {
        selectedEntry = entry;

        if (entry != null) {
            PersonnelEntryInfoWidget entryInfoWidget = new PersonnelEntryInfoWidget(
                    widgetX,
                    widgetY,
                    widgetWidth,
                    widgetHeight,
                    entry.getPersonnelEntry(),
                    this
            );

            entryInfoWidget.setCanEdit(terminal.canEdit());
            setActiveWidget(entryInfoWidget);
        } else {
            setActiveWidget(null);
        }
    }

    @Override
    public void onClose() {
        minecraft.getSoundManager().play(SimpleSoundInstance.forUI(BitterSounds.COMPUTER_END, 1f));
        super.onClose();
    }
}
