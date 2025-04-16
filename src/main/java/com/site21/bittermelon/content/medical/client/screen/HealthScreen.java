package com.site21.bittermelon.content.medical.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.items.medical.MedicalItem;
import com.site21.bittermelon.content.medical.client.screen.networking.ExtractCompartment;
import com.site21.bittermelon.content.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.content.medical.compartments.FunctionType;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

import static com.site21.bittermelon.content.medical.compartments.CompartmentTag.*;

@OnlyIn(Dist.CLIENT)
public class HealthScreen extends Screen {
    private final Character character;
    private final Player player;
    private ItemStack heldItem;
    private final MedicalStats medicalStats;
    private CompartmentList compartmentList;
    private boolean showOnlyInjured = false;
    private Button filterButton;
    private static final ResourceLocation BACKGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/health_screen.png");

    public HealthScreen(@NotNull Character character, Player player, @NotNull ItemStack heldItem) {
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
    }

    public void refreshCompartmentList() {
        if (this.compartmentList == null) {
            this.compartmentList = new CompartmentList(this.minecraft, 200, height - 50, 30, 25);
            this.addWidget(compartmentList);
        }

        double savedScroll = this.compartmentList.getScrollAmount();
        Set<String> savedNodes = PersistentScreen.getExpandedNodes(player.getUUID(), character.getUUID());
        this.compartmentList.clearEntries();

        for (CompartmentInstance compartment : medicalStats.getCompartments().values()) {
            CompartmentInstance parent = medicalStats.getCompartment(compartment.getParentID());
            boolean isRootLevel = parent != null && medicalStats.getCompartment(parent.getParentID()) == null;

            if (isRootLevel && (!showOnlyInjured || hasInjuredChild(compartment))) {
                compartmentList.addEntry(new CompartmentEntry(compartment, 0, this));
            }
        }

        for (CompartmentEntry entry : compartmentList.children()) {
            if (savedNodes.contains(entry.getNodePath())) {
                entry.updateExpansion();
            }
        }

        this.compartmentList.setScrollAmount(savedScroll);
    }

    private boolean hasInjuredChild(@NotNull CompartmentInstance compartment) {
        if (compartment.hasTag(INJURY)) {
            return true;
        }

        for (UUID childID : compartment.getChildren()) {
            if (hasInjuredChild(medicalStats.getCompartment(childID))) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        guiGraphics.blit(BACKGROUND_TEXTURE,
                0,
                30,
                0,
                0,
                200,
                287,
                200,
                400
        );
        RenderSystem.disableBlend();

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

    public Character getCharacter() {
        return character;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
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
        return true;
    }

    private static class CompartmentList extends ObjectSelectionList<CompartmentEntry> {
        public CompartmentList(Minecraft p_94442_, int p_94443_, int p_94444_, int p_94445_, int p_94446_) {
            super(p_94442_, p_94443_, p_94444_, p_94445_, p_94446_);
        }

        public int addEntry(@NotNull CompartmentEntry entry) {
            return super.addEntry(entry);
        }

        public @org.jetbrains.annotations.Nullable CompartmentEntry getHoveredEntry(double mouseX, double mouseY) {
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
        protected void renderListBackground(@NotNull GuiGraphics guiGraphics) {
//            RenderSystem.enableBlend();
//            ResourceLocation resourceLocation = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/inworld_menu_list_background.png");
//            guiGraphics.blit(
//                    resourceLocation,
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
//            super.renderListBackground(guiGraphics);
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
        private final CompartmentInstance compartment;
        private final int depth;
        private final HealthScreen screen;
        private boolean isExpanded;

        public CompartmentEntry(CompartmentInstance compartment, int depth, @NotNull HealthScreen screen) {
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
                if (!compartment.areChildrenEmpty(screen.medicalStats) &&
                        mouseX < (double) screen.width / 2 + 100) {
                    toggleExpanded();
                }
            }

            if (button == 1) {
                if (screen.heldItem != null) {
                    if (screen.heldItem.getItem() instanceof MedicalItem medicalItem) {
                        if (medicalItem.canInteract(compartment, screen.medicalStats)) {
                            medicalItem.use(compartment, screen.medicalStats, screen.character, screen.heldItem);
                            screen.refreshCompartmentList();
                        }
                    }
                }
            }

            if (button == 2) {
                if (compartment.getCompartment().canExtract(compartment, screen.medicalStats) && !compartment.getItem().isEmpty()) {
                    PacketDistributor.sendToServer(new ExtractCompartment(
                            compartment.getUUID(),
                            screen.character.getUUID(),
                            screen.player.getUUID()));
                    screen.medicalStats.removeCompartment(compartment);
                    screen.refreshCompartmentList();
                }
            }

            return true;
        }

        private void toggleExpanded() {
            isExpanded = !isExpanded;
            Set<String> nodes = PersistentScreen.getExpandedNodes(screen.player.getUUID(), screen.character.getUUID());
            if (isExpanded) {
                nodes.add(getNodePath());
                int index = screen.compartmentList.children().indexOf(this) + 1;
                for (UUID childID : compartment.getChildren()) {
                    CompartmentInstance child = screen.medicalStats.getCompartment(childID);
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
            for (UUID childID : compartment.getChildren()) {
                CompartmentInstance child = screen.medicalStats.getCompartment(childID);
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
                if (medicalItem.canInteract(compartment, screen.medicalStats)) {
                    tooltipLines.add(Component.literal("\uE002 " + medicalItem.getActionDescription())
                            .withStyle(style -> style.withFont(
                                    ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "default"))));
                }
            }

            if (!compartment.isObscured()) {
                if (compartment.getCompartment().canExtract(compartment, screen.medicalStats) && !compartment.getItem().isEmpty()) {
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

            int textColor = 0xFFFFFF;
            Component name;

            if (compartment.isObscured()) {
                name = Component.literal(compartment.getName()).withStyle(style -> style.withObfuscated(true).withColor(0xFF7D1010));
            } else {
                name = Component.literal(compartment.getName());
            }

            if (compartment.hasTag(FIRST_AID)) {
                textColor = 0xFF3CC9C5;
            } else if (compartment.hasTag(CONDITION)) {
                textColor = 0xFFCF1515;
            }

            int indent = depth * 12;
            int xPosition = left + indent + 2;

            renderExpandIcon(guiGraphics, xPosition, top);
            xPosition += 13;

            xPosition = renderIconOrItem(guiGraphics, xPosition, top);

            guiGraphics.drawString(Minecraft.getInstance().font, name, xPosition, top + 2, textColor);

            renderHealthOrDivider(guiGraphics, xPosition, top);
        }

        private void renderExpandIcon(GuiGraphics guiGraphics, int x, int top) {
            if (!compartment.areChildrenEmpty(screen.medicalStats) && !compartment.isObscured()) {
                ResourceLocation iconTexture = isExpanded ?
                        ResourceLocation.fromNamespaceAndPath("bittermelon", "textures/gui/sprites/icon/arrow_down.png") :
                        ResourceLocation.fromNamespaceAndPath("bittermelon", "textures/gui/sprites/icon/arrow_right.png");

                guiGraphics.blit(iconTexture, x - 3, top, 0, 0, 16, 16, 16, 16);
            } else {
                ResourceLocation squareTexture = ResourceLocation.fromNamespaceAndPath("bittermelon", "textures/gui/sprites/icon/square.png");
                guiGraphics.blit(squareTexture, x - 3, top, 0, 0, 16, 16, 16, 16);
            }
        }

        private int renderIconOrItem(GuiGraphics guiGraphics, int x, int top) {
            if (compartment.isObscured()) {
                return x;
            }

            if (compartment.getIcon() != null) {
                guiGraphics.blit(compartment.getIcon(), x, top + 2, 0, 0, 16, 16, 16, 16);
                return x + 20;
            }

            if (!compartment.getItem().isEmpty()) {
                boolean canExtract = compartment.getCompartment().canExtract(compartment, screen.medicalStats);

                if (canExtract) {
                    float pulse = (float) (Math.sin(System.currentTimeMillis() / 500.0) * 0.4f + 0.8f);
                    RenderSystem.enableBlend();
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, pulse);
                }

                guiGraphics.renderFakeItem(compartment.getItem(), x, top + 3);

                if (canExtract) {
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.disableBlend();
                }

                return x + 20;
            }

            return x;
        }

        private void renderHealthOrDivider(GuiGraphics guiGraphics, int x, int top) {
            if (compartment.getMaxHealth() > 0 && !compartment.isObscured()) {
                Component health = Component.literal(
                        String.format("%.1f/%.0f", compartment.getHealth(), compartment.getMaxHealth()));
                guiGraphics.drawString(Minecraft.getInstance().font, health, x, top + 12, 0x808080);
            } else {
                guiGraphics.drawString(Minecraft.getInstance().font, "------------------", x, top + 12, 0x808080);
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

