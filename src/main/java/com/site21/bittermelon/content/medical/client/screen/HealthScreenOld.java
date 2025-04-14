//package com.site21.bittermelon.medical.client.gui;
//
//import com.site21.bittermelon.character.Character;
//import com.site21.bittermelon.items.medical.MedicalItem;
//import com.site21.bittermelon.medical.compartments.CompartmentOld;
//import com.site21.bittermelon.medical.compartments.Condition;
//import com.site21.bittermelon.medical.compartments.firstaid.FirstAid;
//import com.site21.bittermelon.medical.medicalstats.MedicalStatsOld;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.GuiGraphics;
//import net.minecraft.client.gui.components.ObjectSelectionList;
//import net.minecraft.client.gui.screens.Screen;
//import net.minecraft.client.gui.screens.inventory.InventoryScreen;
//import net.minecraft.network.chat.Component;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//public class HealthScreenOld extends Screen {
//    private final Character character;
//    private final Player player;
//    private final MedicalStatsOld medicalStats;
//    private CompartmentList compartmentList;
//    private final Set<String> expandedNodes = new HashSet<>();
//    private final List<InstrumentSlot> instrumentSlots = new ArrayList<>();
//    private static final int SLOT_SIZE = 18;
//    private static final int SLOTS_PER_ROW = 4;
//    private InstrumentSlot selectedSlot = null;
//
//
//    public HealthScreenOld(Character character, Player player) {
//        super(Component.literal(character.getName()));
//        this.character = new Character(player.getUUID(), "Test");
//        this.player = player;
//        this.medicalStats = character.getMedicalStats();
//    }
//
//    @Override
//    protected void init() {
//        this.compartmentList = new CompartmentList(this.minecraft, 200, height - 50, 30, 25);
//        for (CompartmentOld compartment : medicalStats.getCompartments()) {
//            if (compartment.getOwner() != null) {
//                if (compartment.getOwner().getOwner() == null) {
//                    this.compartmentList.addEntry(new CompartmentEntry(compartment, 0, this));
//                }
//            }
//        }
//        this.addWidget(compartmentList);
//
//        initializeInstruments();
//    }
//
//    @Override
//    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
//        super.render(guiGraphics, mouseX, mouseY, partialTicks);
//        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
//
//
//        int entityX = this.width / 3;
//        int entityY = 30;
//        int entityWidth = 200;
//        int entityHeight = this.height - 60;
//
//        InventoryScreen.renderEntityInInventoryFollowsAngle(
//                guiGraphics,
//                entityX,
//                entityY,
//                entityX + entityWidth,
//                entityY + entityHeight,
//                100,
//                0.0f,
//                0.0f,
//                0.0f,
//                this.player
//        );
//
//        this.compartmentList.render(guiGraphics, mouseX, mouseY, partialTicks);
//        renderInstruments(guiGraphics, mouseX, mouseY);
//
//        if (selectedSlot != null) {
//            guiGraphics.renderItem(selectedSlot.stack, mouseX - 8, mouseY - 8);
//        }
//    }
//
//    private void initializeInstruments() {
//        instrumentSlots.clear();
//        List<ItemStack> instruments = new ArrayList<>();
//
//        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
//            ItemStack stack = player.getInventory().getItem(i);
//            if (stack.getItem() instanceof MedicalItem) {
//                instruments.add(stack);
//            }
//        }
//
//        int startX = width - 200;
//        int startY = 40;
//
//        for (int i = 0; i < instruments.size(); i++) {
//            int row = i / SLOTS_PER_ROW;
//            int col = i % SLOTS_PER_ROW;
//            int x = startX + col * SLOT_SIZE;
//            int y = startY + row * SLOT_SIZE;
//
//            instrumentSlots.add(new InstrumentSlot(
//                    instruments.get(i),
//                    x,
//                    y,
//                    i
//            ));
//        }
//
//    }
//
//    private void renderInstruments(GuiGraphics guiGraphics, int mouseX, int mouseY) {
//        guiGraphics.drawString(
//                Minecraft.getInstance().font,
//                Component.literal("Available Instruments"),
//                width - 200,
//                30,
//                0xFFFFFF
//        );
//
//        for (InstrumentSlot slot : instrumentSlots) {
//            if (slot == selectedSlot) {
//                guiGraphics.fill(
//                        slot.x,
//                        slot.y,
//                        slot.x + SLOT_SIZE,
//                        slot.y + SLOT_SIZE,
//                        0xFFFFFF00
//                );
//            } else {
//                guiGraphics.fill(
//                        slot.x,
//                        slot.y,
//                        slot.x + SLOT_SIZE,
//                        slot.y + SLOT_SIZE,
//                        0xFF404040
//                );
//            }
//
//            guiGraphics.renderItem(slot.stack, slot.x + 1, slot.y + 1);
//            guiGraphics.renderItemDecorations(Minecraft.getInstance().font, slot.stack, slot.x + 1, slot.y + 1);
//
//            if (mouseX >= slot.x && mouseX < slot.x + SLOT_SIZE &&
//                    mouseY >= slot.y && mouseY < slot.y + SLOT_SIZE) {
//                guiGraphics.renderTooltip(
//                        Minecraft.getInstance().font,
//                        slot.stack.getHoverName(),
//                        mouseX,
//                        mouseY
//                );
//            }
//        }
//    }
//
//
//    @Override
//    public boolean mouseClicked(double mouseX, double mouseY, int button) {
//        if (button == 0) {
//            for (InstrumentSlot slot : instrumentSlots) {
//                if (mouseX >= slot.x && mouseX < slot.x + SLOT_SIZE &&
//                        mouseY >= slot.y && mouseY < slot.y + SLOT_SIZE) {
//                    handleInstrumentSelection(slot);
//                    return true;
//                }
//            }
//
//            if (selectedSlot != null) {
//                return compartmentList.mouseClicked(mouseX, mouseY, button);
//            }
//        } else if (selectedSlot != null) {
//            selectedSlot = null;
//        }
//
//        return super.mouseClicked(mouseX, mouseY, button);
//    }
//
//    private void handleInstrumentSelection(InstrumentSlot slot) {
//        if (selectedSlot == slot) {
//            selectedSlot = null;
//        } else {
//            selectedSlot = slot;
//        }
//    }
//
//    private static class InstrumentSlot {
//        final ItemStack stack;
//        final int x;
//        final int y;
//        final int index;
//
//        InstrumentSlot(ItemStack stack, int x, int y, int index) {
//            this.stack = stack;
//            this.x = x;
//            this.y = y;
//            this.index = index;
//        }
//    }
//
//    private static class CompartmentList extends ObjectSelectionList<CompartmentEntry> {
//        public CompartmentList(Minecraft p_94442_, int p_94443_, int p_94444_, int p_94445_, int p_94446_) {
//            super(p_94442_, p_94443_, p_94444_, p_94445_, p_94446_);
//        }
//
//        public int addEntry(@NotNull CompartmentEntry entry) {
//            return super.addEntry(entry);
//        }
//
//        @Override
//        public int getRowWidth() {
//            return 200;
//        }
//
//        @Override
//        protected int getScrollbarPosition() {
//            return this.width;
//        }
//    }
//
//    private static class CompartmentEntry extends ObjectSelectionList.Entry<CompartmentEntry> {
//        private final CompartmentOld compartment;
//        private final int depth;
//        private final HealthScreen screen;
//        private boolean isExpanded;
//        private static final int REMOVE_BUTTON_WIDTH = 16;
//
//        public CompartmentEntry(CompartmentOld compartment, int depth, HealthScreen screen) {
//            this.compartment = compartment;
//            this.depth = depth;
//            this.screen = screen;
//            this.isExpanded = screen.expandedNodes.contains(getNodePath());
//        }
//
//        private String getNodePath() {
//            return compartment.getName();
//        }
//
//        @Override
//        public boolean mouseClicked(double mouseX, double mouseY, int button) {
//            if (button == 0) {
//                int removeButtonX = screen.width / 2 + 80;
//                if (compartment.canExtract() &&
//                        mouseX >= removeButtonX && mouseX <= removeButtonX + REMOVE_BUTTON_WIDTH &&
//                        mouseY >= lastRenderedTop && mouseY <= lastRenderedTop + 20) {
////                    compartment.remove();
//                    screen.compartmentList.children().remove(this);
//                    return true;
//                }
//
//                if (screen.selectedSlot != null) {
//                    ItemStack stack = screen.selectedSlot.stack;
//                    if (stack.getItem() instanceof MedicalItem medicalItem) {
//                        medicalItem.use(compartment, this.screen.medicalStats, this.screen.character);
//                        screen.selectedSlot = null;
//                        return true;
//                    }
//                } else if (!compartment.getChildren().isEmpty() &&
//                        mouseX < (double) screen.width / 2 + 100) {
//                    toggleExpanded();
//                    return true;
//                }
//            }
//            return false;
//        }
//
//        private int lastRenderedTop;
//
//        private void toggleExpanded() {
//            isExpanded = !isExpanded;
//            if (isExpanded) {
//                screen.expandedNodes.add(getNodePath());
//                int index = screen.compartmentList.children().indexOf(this) + 1;
//                for (CompartmentOld child : compartment.getChildren()) {
//                    if (!child.isHidden()) {
//                        screen.compartmentList.children().add(index++,
//                                new CompartmentEntry(child, depth + 1, screen));
//                    }
//                }
//            } else {
//                screen.expandedNodes.remove(getNodePath());
//                removeChildren();
//            }
//        }
//
//        private void removeChildren() {
//            int index = screen.compartmentList.children().indexOf(this) + 1;
//            while (index < screen.compartmentList.children().size()) {
//                CompartmentEntry entry = screen.compartmentList.children().get(index);
//                if (entry.depth <= this.depth) break;
//                screen.expandedNodes.remove(entry.getNodePath());
//                screen.compartmentList.children().remove(index);
//            }
//        }
//
//        @Override
//        public @NotNull Component getNarration() {
//            return Component.literal(compartment.getName());
//        }
//
//        @Override
//        public void render(@NotNull GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
//            lastRenderedTop = top;
//            int color = 0xFFFFFF;
//            Component name = Component.literal(compartment.getName());
//            int nameWidth = Minecraft.getInstance().font.width(name);
//            ResourceLocation REMOVE_ICON = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/sprites/widget/cross.png");
//            int ICON_SIZE = 8;
//
//            if (compartment instanceof FirstAid) {
//                color = 0xFF3CC9C5;
//            } else if (compartment instanceof Condition) {
//                color = 0xFFCF1515;
//            }
//
//            int indent = depth * 12;
//            if (!compartment.areChildrenEmpty()) {
//                Component icon = Component.literal(isExpanded ? "⮟" : "⮞");
//                guiGraphics.drawString(Minecraft.getInstance().font, icon,
//                        left + indent + 2, top + 2, color);
//            }
//
//            guiGraphics.drawString(Minecraft.getInstance().font, name,
//                    left + indent + 15, top + 2, color);
//
//            if (compartment.getMaxHealth() > 0) {
//                Component health = Component.literal(
//                        String.format("%.1f/%.0f", compartment.getHealth(),
//                                (float) compartment.getMaxHealth()));
//                guiGraphics.drawString(Minecraft.getInstance().font, health,
//                        left + indent + 15, top + 12, 0x808080);
//            } else {
//                guiGraphics.drawString(Minecraft.getInstance().font, "------------------",
//                        left + indent + 15, top + 12, 0x808080);
//            }
//
//            if (compartment.getItem() != null) {
//                guiGraphics.renderFakeItem(new ItemStack(compartment.getItem()), left + indent - 5, top + 3);
//            }
//
//            if (compartment.canExtract()) {
////                int removeButtonX = left + indent + 15 + nameWidth + 5;
////                Component removeIcon = Component.literal("✕");
////                boolean isHovered = mouseX >= removeButtonX && mouseX <= removeButtonX + REMOVE_BUTTON_WIDTH &&
////                        mouseY >= top && mouseY <= top + 20;
//                int iconX = left + indent + 15 + nameWidth + 5;
//                boolean isHovered = mouseX >= iconX && mouseX <= iconX + ICON_SIZE &&
//                        mouseY >= top && mouseY <= top + ICON_SIZE;
//
////                guiGraphics.drawString(
////                        Minecraft.getInstance().font,
////                        removeIcon,
////                        removeButtonX,
////                        top + 7,
////                        isHovered ? 0xFF0000 : 0x808080
////                );
//
//                guiGraphics.blit(REMOVE_ICON, iconX, top + 4, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
//            }
//        }
//    }
//}
