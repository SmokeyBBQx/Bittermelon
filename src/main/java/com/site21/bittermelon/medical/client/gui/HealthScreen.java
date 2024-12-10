package com.site21.bittermelon.medical.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.items.medical.MedicalItem;
import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.compartments.Condition;
import com.site21.bittermelon.medical.compartments.firstaid.FirstAid;
import com.site21.bittermelon.medical.compartments.conditions.Bleed;
import com.site21.bittermelon.medical.medicalstats.MedicalStats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

public class HealthScreen extends Screen {
    private final Character character;
    private final Player player;
    private ItemStack heldItem;
    private final MedicalStats medicalStats;
    private CompartmentList compartmentList;

    private final List<InstrumentSlot> instrumentSlots = new ArrayList<>();
    private static final int SLOT_SIZE = 18;
    private static final int SLOTS_PER_ROW = 4;
    private InstrumentSlot selectedSlot = null;

    private boolean showOnlyInjured = false;
    private Button filterButton;

    public HealthScreen(Character character, Player player, ItemStack heldItem) {
        super(Component.literal(character.getName()));
        this.character = character;
        this.player = player;
        this.medicalStats = character.getMedicalStats();
        if (heldItem.getItem() instanceof MedicalItem) {
            this.heldItem = heldItem;
        }
    }

    @Override
    protected void init() {
        this.filterButton = Button.builder(
                        Component.literal(showOnlyInjured ? "Show All" : "Show Injured Only"),
                        (button) -> {
                            showOnlyInjured = !showOnlyInjured;
                            button.setMessage(Component.literal(showOnlyInjured ? "Show All" : "Show Injured Only"));
                            refreshCompartmentList();
                        })
                .pos(5, 5)
                .size(100, 20)
                .build();

        this.addWidget(this.filterButton);
        refreshCompartmentList();
    }

    @Override
    public void tick() {
        super.tick();
        for (CompartmentEntry entry : this.compartmentList.children()) {
            entry.obscure();
        }
    }


    public void refreshCompartmentList() {
        if (this.compartmentList == null) {
            this.compartmentList = new CompartmentList(this.minecraft, 200, height - 50, 30, 25);
            this.addWidget(compartmentList);
        }

        double savedScroll = this.compartmentList.getScrollAmount();
        Set<String> savedNodes = PersistentScreen.getExpandedNodes(player.getUUID(), character.getUUID());
        this.compartmentList.clearEntries();

        for (Compartment compartment : medicalStats.getCompartments()) {
            if (compartment.getOwner() != null && compartment.getOwner().getOwner() == null) {
                if (!showOnlyInjured || hasInjuredChild(compartment)) {
                    CompartmentEntry entry = new CompartmentEntry(compartment, 0, this);
                    this.compartmentList.addEntry(entry);
                }
            }
        }

        for (int i = 0; i < this.compartmentList.children().size(); i++) {
            CompartmentEntry entry = this.compartmentList.children().get(i);
            if (savedNodes.contains(entry.getNodePath())) {
                entry.updateExpansion();
            }
        }

        this.compartmentList.setScrollAmount(savedScroll);
    }

    private boolean hasInjuredChild(Compartment compartment) {
        if (compartment instanceof Condition) {
            return true;
        }

        for (Compartment child : compartment.getChildren()) {
            if (hasInjuredChild(child)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.filterButton.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.compartmentList.render(guiGraphics, mouseX, mouseY, partialTicks);

        CompartmentEntry hoveredEntry = this.compartmentList.getHoveredEntry(mouseX, mouseY);
        if (hoveredEntry != null) {
            hoveredEntry.getTooltip().ifPresent(tooltip ->
                    guiGraphics.renderTooltip(Minecraft.getInstance().font, tooltip, mouseX, mouseY)
            );

            // TODO: Manual interactions (apply pressure, pull, etc.)
        }

        if (heldItem != null) {
            guiGraphics.renderItem(heldItem, mouseX - 8, mouseY - 8);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void initializeInstruments() {
        instrumentSlots.clear();
        List<ItemStack> instruments = new ArrayList<>();

        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof MedicalItem) {
                instruments.add(stack);
            }
        }

        int startX = width - 200;
        int startY = 40;

        for (int i = 0; i < instruments.size(); i++) {
            int row = i / SLOTS_PER_ROW;
            int col = i % SLOTS_PER_ROW;
            int x = startX + col * SLOT_SIZE;
            int y = startY + row * SLOT_SIZE;

            instrumentSlots.add(new InstrumentSlot(
                    instruments.get(i),
                    x,
                    y,
                    i
            ));
        }

    }

    private void renderInstruments(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(
                Minecraft.getInstance().font,
                Component.literal("Available Instruments"),
                width - 200,
                30,
                0xFFFFFF
        );

        for (InstrumentSlot slot : instrumentSlots) {
            if (slot == selectedSlot) {
                guiGraphics.fill(
                        slot.x,
                        slot.y,
                        slot.x + SLOT_SIZE,
                        slot.y + SLOT_SIZE,
                        0xFFFFFF00
                );
            } else {
                guiGraphics.fill(
                        slot.x,
                        slot.y,
                        slot.x + SLOT_SIZE,
                        slot.y + SLOT_SIZE,
                        0xFF404040
                );
            }

            guiGraphics.renderItem(slot.stack, slot.x + 1, slot.y + 1);
            guiGraphics.renderItemDecorations(Minecraft.getInstance().font, slot.stack, slot.x + 1, slot.y + 1);

            if (mouseX >= slot.x && mouseX < slot.x + SLOT_SIZE &&
                    mouseY >= slot.y && mouseY < slot.y + SLOT_SIZE) {
                guiGraphics.renderTooltip(
                        Minecraft.getInstance().font,
                        slot.stack.getHoverName(),
                        mouseX,
                        mouseY
                );
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.filterButton.isMouseOver(mouseX, mouseY)) {
            return this.filterButton.mouseClicked(mouseX, mouseY, button);
        }

        boolean clickedOnCompartment = compartmentList.mouseClicked(mouseX, mouseY, button);
        if (!clickedOnCompartment) {
            this.onClose();
            return super.mouseClicked(mouseX, mouseY, button);
        }
        return clickedOnCompartment;
    }

    private void handleInstrumentSelection(InstrumentSlot slot) {
        if (selectedSlot == slot) {
            selectedSlot = null;
        } else {
            selectedSlot = slot;
        }
    }

    private static class InstrumentSlot {
        final ItemStack stack;
        final int x;
        final int y;
        final int index;

        InstrumentSlot(ItemStack stack, int x, int y, int index) {
            this.stack = stack;
            this.x = x;
            this.y = y;
            this.index = index;
        }
    }

    private static class CompartmentList extends ObjectSelectionList<CompartmentEntry> {
        public CompartmentList(Minecraft p_94442_, int p_94443_, int p_94444_, int p_94445_, int p_94446_) {
            super(p_94442_, p_94443_, p_94444_, p_94445_, p_94446_);
        }

        public int addEntry(@NotNull CompartmentEntry entry) {
            return super.addEntry(entry);
        }

        public CompartmentEntry getHoveredEntry(double mouseX, double mouseY) {
            if (this.isMouseOver(mouseX, mouseY)) {
                int i1 = Mth.floor(mouseY - (double) this.getY()) - this.headerHeight + (int) this.getScrollAmount() - 4;
                int j1 = i1 / this.itemHeight;
                if (j1 >= 0 && i1 >= 0 && j1 < this.children().size()) {
                    return this.children().get(j1);
                }
            }
            return null;
        }

        @Override
        public void clearEntries() {
            super.clearEntries();
        }

        @Override
        public int getRowWidth() {
            return 200;
        }

        @Override
        public void setFocused(@Nullable GuiEventListener focused) {

        }

        @Override
        protected void renderListBackground(GuiGraphics guiGraphics) {
//            RenderSystem.enableBlend();
//            ResourceLocation resourcelocation = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/inworld_menu_list_background.png");
//            guiGraphics.blit(
//                    resourcelocation,
//                    this.getX(),
//                    this.getY(),
//                    (float)this.getRight(),
//                    (float)(this.getBottom() + (int)this.getScrollAmount()),
//                    this.getWidth(),
//                    this.getHeight(),
//                    32,
//                    32
//            );
//            RenderSystem.disableBlend();
            super.renderListBackground(guiGraphics);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            this.updateScrollingState(mouseX, mouseY, button);
            if (!this.isMouseOver(mouseX, mouseY)) {
                return false;
            } else {
                CompartmentEntry e = this.getEntryAtPosition(mouseX, mouseY);
                if (e != null) {
                    if (e.mouseClicked(mouseX, mouseY, button)) {
                        CompartmentEntry e1 = this.getFocused();
                        if (e1 != e && e1 instanceof ContainerEventHandler containereventhandler) {
                            containereventhandler.setFocused(null);
                        }

                        this.setFocused(e);
                        this.setDragging(true);
                        return true;
                    }
                } else if (this.clickedHeader(
                        (int) (mouseX - (double) (this.getX() + this.width / 2 - this.getRowWidth() / 2)),
                        (int) (mouseY - (double) this.getY()) + (int) this.getScrollAmount() - 4
                )) {
                    return true;
                }

                return super.mouseClicked(mouseX, mouseY, button);
            }
        }

        @Override
        protected int getScrollbarPosition() {
            return this.width;
        }
    }

    private static class CompartmentEntry extends ObjectSelectionList.Entry<CompartmentEntry> {
        private final Compartment compartment;
        private final int depth;
        private final HealthScreen screen;
        private boolean isExpanded;
        private static final int REMOVE_BUTTON_WIDTH = 16;
        private int removeButtonX;
        private int removeButtonY;

        public CompartmentEntry(Compartment compartment, int depth, HealthScreen screen) {
            this.compartment = compartment;
            this.depth = depth;
            this.screen = screen;
            this.isExpanded = PersistentScreen.getExpandedNodes(screen.player.getUUID(), screen.character.getUUID()).contains(getNodePath());
        }

        private String getNodePath() {
            return compartment.getName();
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0) {
                if (!compartment.areChildrenEmpty() &&
                        mouseX < (double) screen.width / 2 + 100) {
                    toggleExpanded();
                }
            }

            if (button == 1) {
                if (screen.heldItem != null) {
                    if (screen.heldItem.getItem() instanceof MedicalItem medicalItem) {
                        if (medicalItem.canInteract(compartment)) {
                            medicalItem.use(compartment, screen.medicalStats, screen.character, screen.heldItem);
                            screen.refreshCompartmentList();
                        }
                    }
                }
            }

            if (button == 2) {
                if (compartment.canExtract() && compartment.getItem() != null) {
                    screen.character.getMedicalStats().extractCompartment(compartment);
                    screen.refreshCompartmentList();
                    screen.player.getInventory().add(compartment.getItem());
                }
            }

            return true;
        }

        private int lastRenderedTop;

        private void toggleExpanded() {
            isExpanded = !isExpanded;
            Set<String> nodes = PersistentScreen.getExpandedNodes(screen.player.getUUID(), screen.character.getUUID());
            if (isExpanded) {
                nodes.add(getNodePath());
                int index = screen.compartmentList.children().indexOf(this) + 1;
                for (Compartment child : compartment.getChildren()) {
                    if (!child.isHidden()) {
                        screen.compartmentList.children().add(index++,
                                new CompartmentEntry(child, depth + 1, screen));
                    }
                }
            } else {
                nodes.remove(getNodePath());
                removeChildren();
            }
        }

        private void updateExpansion() {
            int index = screen.compartmentList.children().indexOf(this) + 1;
            for (Compartment child : compartment.getChildren()) {
                if (!child.isHidden() && (!screen.showOnlyInjured || screen.hasInjuredChild(child))) {
                    screen.compartmentList.children().add(index++,
                            new CompartmentEntry(child, depth + 1, screen));
                }
            }
        }

        private void removeChildren() {
            int index = screen.compartmentList.children().indexOf(this) + 1;
            while (index < screen.compartmentList.children().size()) {
                CompartmentEntry entry = screen.compartmentList.children().get(index);
                if (entry.depth <= this.depth) break;
                PersistentScreen.getExpandedNodes(screen.player.getUUID(), screen.character.getUUID()).remove(entry.getNodePath());
                screen.compartmentList.children().remove(index);
            }
        }

        @Override
        public @NotNull Component getNarration() {
            return Component.literal(compartment.getName());
        }

        public Optional<Component> getTooltip() {
            List<Component> tooltipLines = new ArrayList<>();

            if (screen.heldItem != null && screen.heldItem.getItem() instanceof MedicalItem medicalItem) {
                if (medicalItem.canInteract(compartment)) {
                    tooltipLines.add(Component.literal("\uE002 " + medicalItem.getActionDescription())
                            .withStyle(style -> style.withFont(
                                    ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "default"))));
                }
            }

            if (!compartment.isObscured()) {
                if (compartment.canExtract() && compartment.getItem() != null) {
                    tooltipLines.add(Component.literal("\uE001 Extract")
                            .withStyle(style -> style.withFont(
                                    ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "default"))));
                }
            }

            if (tooltipLines.isEmpty()) {
                return Optional.empty();
            } else {
                Component result = tooltipLines.get(0);
                if (tooltipLines.size() > 1) {
                    result = Component.empty()
                            .append(result)
                            .append(Component.literal(" "))
                            .append(tooltipLines.get(1));
                }
                return Optional.of(result);
            }
        }

        @Override
        public void render(@NotNull GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            if (hovering) {
                guiGraphics.fill(left - 1, top - 1, left + width + 1, top + 22, 0x22FFFFFF);
            }

            lastRenderedTop = top;
            int color = 0xFFFFFF;
            Component name;
            if (compartment.isObscured()) {
                name = Component.literal(compartment.getName()).withStyle(style -> style.withObfuscated(true).withColor(0xFF7D1010));
            } else {
                name = Component.literal(compartment.getName());
            }
            int nameWidth = Minecraft.getInstance().font.width(name);
            ResourceLocation REMOVE_ICON = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/sprites/widget/cross.png");
            int ICON_SIZE = 8;

            if (compartment instanceof FirstAid) {
                color = 0xFF3CC9C5;
            } else if (compartment instanceof Condition) {
                color = 0xFFCF1515;
            }

            int indent = depth * 12;
            int currentX = left + indent + 2;

            if (!compartment.areChildrenEmpty() && !compartment.isObscured()) {
                ResourceLocation UNEXPANDED_ICON = ResourceLocation.fromNamespaceAndPath("bittermelon", "textures/gui/sprites/icon/arrow_right.png");
                ResourceLocation EXPANDED_ICON = ResourceLocation.fromNamespaceAndPath("bittermelon", "textures/gui/sprites/icon/arrow_down.png");
                guiGraphics.blit(
                        isExpanded ? EXPANDED_ICON : UNEXPANDED_ICON,
                        currentX - 3,
                        top,
                        0,
                        0,
                        16,
                        16,
                        16,
                        16
                );
            } else {
                ResourceLocation SQUARE = ResourceLocation.fromNamespaceAndPath("bittermelon", "textures/gui/sprites/icon/square.png");
                guiGraphics.blit(
                        SQUARE,
                        currentX - 3,
                        top,
                        0,
                        0,
                        16,
                        16,
                        16,
                        16
                );
            }
            currentX += 13;

            if (compartment.isObscured()) {

            } else if (compartment.getIcon() != null) {
                guiGraphics.blit(
                        compartment.getIcon(),
                        currentX,
                        top + 2,
                        0,
                        0,
                        16,
                        16,
                        16,
                        16
                );
                currentX += 20;
            } else if (compartment.getItem() != null) {
                if (compartment.canExtract()) {
                    float pulse = (float) (Math.sin(System.currentTimeMillis() / 500.0) * 0.4f + 0.8f);
                    RenderSystem.enableBlend();
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, pulse);
                }

                guiGraphics.renderFakeItem(compartment.getItem(), currentX, top + 3);

                if (compartment.canExtract()) {
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.disableBlend();
                }
                currentX += 20;

            }

            guiGraphics.drawString(Minecraft.getInstance().font, name,
                    currentX, top + 2, color);

            if (compartment.getMaxHealth() >= 0 && !compartment.isObscured()) {
                Component health = Component.literal(
                        String.format("%.1f/%.0f", compartment.getHealth(),
                                compartment.getMaxHealth()));
                guiGraphics.drawString(Minecraft.getInstance().font, health,
                        currentX, top + 12, 0x808080);
            } else {
                guiGraphics.drawString(Minecraft.getInstance().font, "------------------",
                        currentX, top + 12, 0x808080);
            }
        }

        private void obscure() {
            List<Compartment> children = compartment.getChildren();
            List<Compartment> ownerChildren = compartment.getOwner().getChildren();
            int bleedCount = 0;

            for (Compartment child : children) {
                if (child instanceof Bleed bleed) {
                    bleedCount += (int) (bleed.getHealth() * bleed.getBleedRate());
                }
            }

            if (bleedCount > 0) {
                RandomSource random = screen.player.getRandom();
                float obscureChance = Math.min(0.01f + (0.05f * bleedCount), 0.05f);

                if (random.nextFloat() < obscureChance) {
                    List<Compartment> eligibleChildren = ownerChildren.stream()
                            .filter(child -> !(child instanceof Condition) && !child.isObscured())
                            .toList();

                    if (!eligibleChildren.isEmpty()) {
                        Compartment selectedChild = eligibleChildren.get(
                                random.nextInt(eligibleChildren.size())
                        );
                        selectedChild.setObscured(true);
                    }
                }
            }
        }
    }

    private static class PersistentScreen {
        private record CharacterKey(UUID playerId, UUID characterId) {
        }

        private static final Map<CharacterKey, Set<String>> expandedHealthNodes = new HashMap<>();

        public static Set<String> getExpandedNodes(UUID playerId, UUID characterId) {
            return expandedHealthNodes.computeIfAbsent(
                    new CharacterKey(playerId, characterId),
                    k -> new HashSet<>()
            );
        }
    }
}

