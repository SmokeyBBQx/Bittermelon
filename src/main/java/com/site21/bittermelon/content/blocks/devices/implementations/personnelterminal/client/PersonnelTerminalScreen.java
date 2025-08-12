package com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.PersonnelTerminalBlockEntity;
import com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.networking.AddPersonnelEntry;
import com.site21.bittermelon.content.personnel.PersonnelEntry;
import com.site21.bittermelon.content.personnel.PersonnelRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class PersonnelTerminalScreen extends Screen {
    public static final WidgetSprites BUTTON_SPRITES = new WidgetSprites(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/button"), ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/button_disabled"), ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/button_highlighted"));
    public static final ResourceLocation SCROLLER_SPRITE = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "retro/scroller");
    public static final ResourceLocation SCROLLER_BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("widget/scroller_background");

    private final PersonnelRegistry registry;
    private final PersonnelTerminalBlockEntity terminal;
    private final PersonnelListWidget personnelList;
    private AbstractWidget activeEntryWidget;
    private PersonnelListWidget.Entry selectedEntry;
    private EditBox searchField;
    private Button addButton;
    private String currentSearchTerm = "";

    public PersonnelTerminalScreen(PersonnelTerminalBlockEntity terminal) {
        super(Component.literal("Personnel Terminal"));
        assert Minecraft.getInstance().player != null;
        registry = PersonnelRegistry.get(Minecraft.getInstance().player.level());
        this.terminal = terminal;
        personnelList = new PersonnelListWidget(Minecraft.getInstance(), 200, 200, 10, 25, this);
    }

    @Override
    protected void init() {
        super.init();

        searchField = new EditBox(font, 20, 25, 200, 20, Component.literal("Search"));
        searchField.setHint(Component.literal("Search personnel..."));
        searchField.setResponder(this::onSearchChanged);
        searchField.setFGColor(0xFFFFFFFF);
        addRenderableWidget(searchField);

        addButton = BitterButton.builder(Component.literal("Add Entry"), this::onAddButtonPressed, PersonnelTerminalScreen.BUTTON_SPRITES)
                .bounds(20, height - 40, 80, 20)
                .build();
        addRenderableWidget(addButton);

        personnelList.setPosition(20, 50);
        personnelList.setSize(200, height - 100);
        addRenderableWidget(personnelList);

        updatePersonnelList();
    }

    private void onSearchChanged(@NotNull String searchTerm) {
        currentSearchTerm = searchTerm.toLowerCase();
        updatePersonnelList();
    }

    private void onAddButtonPressed(Button button) {
        PacketDistributor.sendToServer(new AddPersonnelEntry(new PersonnelEntry(UUID.fromString("64f95c69-6eb8-4c70-8e3d-a9916b55aa79"), "joe", "test", "test")));
        updatePersonnelList();
    }

    public void updatePersonnelList() {
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

        if (activeEntryWidget != null) {
            removeWidget(activeEntryWidget);
            activeEntryWidget = null;
        }

        if (entry != null) {
            PersonnelEntryInfoWidget entryInfoWidget = new PersonnelEntryInfoWidget(
                    250, 50,
                    width - 350, height - 100,
                    entry.getPersonnelEntry(),
                    this
            );

            entryInfoWidget.setCanEdit(terminal.canEdit());
            setActiveWidget(entryInfoWidget);
        }
    }

    public void setActiveWidget(AbstractWidget widget) {
        removeWidget(activeEntryWidget);
        activeEntryWidget = widget;
        addRenderableWidget(widget);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.fill(10, 3, width - 90, height - 10, 0xFFF9FDFF);
        guiGraphics.fill(10 + 1, 3 + 1, width - 90 - 1, height - 10 - 1, 0xFFD6D6CE);

        for (Renderable renderable : this.renderables) {
            renderable.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        guiGraphics.fillGradient(12, 5, width - 92, 20, 0xFF2C02AC, 0xFF1084D0);

        guiGraphics.drawString(font, "Personnel Database", 20, 8, 0xFFFFFF);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
