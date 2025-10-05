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
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HealthScreenV2 extends Screen {
    private final UUID characterUUID;
    private Character character;
    private MedicalStats medicalStats;
    private List<CompartmentSpaceWidget> renderedCompartmentSpaces;
    private CompartmentSpaceWidget activeWidget = null;
    private HeldItemData heldItemData = null;

    public HealthScreenV2(@NotNull Character character) {
        super(Component.literal(character.getName()));
        this.characterUUID = character.getUUID();
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
                        medicalStats.getMainCompartment(),
                this
                )
        );
    }

    public void addCompartmentSpace(CompartmentInstance instance) {
        renderedCompartmentSpaces.add(new CompartmentSpaceWidget(
                20,
                20,
                100,
                100,
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
        if (heldItemData != null) {
            CompartmentData instance = heldItemData.heldItem().get(BitterDataComponents.COMPARTMENT);
            if (instance == null || instance.getVisualData().getIcon() == null) {
                guiGraphics.renderFakeItem(heldItemData.heldItem(), mouseX, mouseY);
            } else {
                VisualData visualData = instance.getVisualData();
                float scaleFactor = visualData.scale;
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(
                        mouseX - (float) visualData.getWidth() * 2,
                        mouseY - (float) visualData.getHeight() * 2,
                        visualData.getZ()
                );
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
                if (heldItemData != null) {
                    widget.handleCompartmentInteraction(mouseX, mouseY, button);
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
        return CharacterManager.get(minecraft.level).getCharacter(characterUUID);
    }

    public MedicalStats getMedicalStats() {
        return medicalStats;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public HeldItemData getHeldItemData() {
        return heldItemData;
    }

    public void setHeldItemData(HeldItemData heldItemData) {
        this.heldItemData = heldItemData;
    }

    public void refresh() {
        for (CompartmentSpaceWidget widget : renderedCompartmentSpaces) {
            CompartmentInstance updated = medicalStats.getCompartment(widget.getCompartment().getUUID());
            widget.setCompartment(updated);
            widget.refreshCompartmentNodes();

            // TODO: Refresh specific widgets by mapping widgets to compartment UUIDs?
        }
    }
}

