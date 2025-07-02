package com.site21.bittermelon.content.medical.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.content.medical.client.screen.widget.CompartmentSpaceWidget;
import com.site21.bittermelon.content.medical.compartments.CompartmentData;
import com.site21.bittermelon.content.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.content.medical.compartments.VisualData;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HealthScreenV2 extends Screen {
    private final Character character;
    private MedicalStats medicalStats;
    private List<CompartmentSpaceWidget> renderedCompartmentSpaces;
    private CompartmentSpaceWidget activeWidget = null;
    private ItemStack heldItem = null;

    public HealthScreenV2(@NotNull Character character) {
        super(Component.literal(character.getName()));
        this.character = character;
        this.medicalStats = character.getMedicalStats();
        this.renderedCompartmentSpaces = new ArrayList<>();
        // TODO: Implement client listener for medical stats
    }

    @Override
    protected void init() {
        renderedCompartmentSpaces.add(new CompartmentSpaceWidget(
                        10,
                        10,
                        160,
                        150,
                        Component.literal(character.getName()),
                        medicalStats.getMainCompartment(),
                this
                )
        );
    }

    public void addCompartmentSpace(Component name, CompartmentInstance instance) {
        renderedCompartmentSpaces.add(new CompartmentSpaceWidget(
                20,
                20,
                100,
                100,
                name,
                instance,
                this
        ));
    }

    public void removeCompartmentSpace(CompartmentSpaceWidget compartmentSpace) {
        if (compartmentSpace == renderedCompartmentSpaces.getFirst()) {
            onClose();
            return;
        }
        renderedCompartmentSpaces.remove(compartmentSpace);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        for (CompartmentSpaceWidget widget : renderedCompartmentSpaces) {
            widget.render(guiGraphics, mouseX, mouseY, partialTick);
        }

        renderHeldItem(guiGraphics, mouseX, mouseY);
    }

    private void renderHeldItem(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (heldItem != null) {
            CompartmentData instance = heldItem.get(BitterDataComponents.COMPARTMENT);
            if (instance == null || instance.getVisualData().getIcon() == null) {
                guiGraphics.renderFakeItem(heldItem, mouseX, mouseY);
            } else {
                VisualData visualData = instance.getVisualData();
                float scaleFactor = visualData.scale;
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(mouseX - (float) visualData.getWidth() * 2, mouseY - (float) visualData.getHeight() * 2, visualData.getZ());
                guiGraphics.pose().scale(scaleFactor, scaleFactor, 0);

                int width = visualData.width;
                int height = visualData.height;
                RenderSystem.enableBlend();
                guiGraphics.blit(visualData.icon, 0, 0, 0, 0, width, height, width, height);
                RenderSystem.disableBlend();

                guiGraphics.pose().popPose();
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (CompartmentSpaceWidget widget : renderedCompartmentSpaces.reversed()) {
            if (widget.mouseClicked(mouseX, mouseY, button)) {
                activeWidget = widget;
                if (heldItem != null) {
                    activeWidget.handleCompartmentPlacement(mouseX, mouseY, button);
                    for (CompartmentSpaceWidget widget1 : renderedCompartmentSpaces) {
                        widget1.refreshCompartmentNodes();
                    }
                }
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (activeWidget != null) {
            return activeWidget.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }

        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (activeWidget != null) {
            boolean result = activeWidget.mouseReleased(mouseX, mouseY, button);
            activeWidget = null;
            return result;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    public Character getCharacter() {
        return character;
    }

    public MedicalStats getMedicalStats() {
        return medicalStats;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public ItemStack getHeldItem() {
        return heldItem;
    }

    public void setHeldItem(ItemStack heldItem) {
        this.heldItem = heldItem;
    }
}

