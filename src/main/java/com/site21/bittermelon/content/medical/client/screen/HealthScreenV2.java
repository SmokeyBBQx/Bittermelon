package com.site21.bittermelon.content.medical.client.screen;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.content.medical.compartments.CompartmentSpace;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HealthScreenV2 extends Screen {
    private final Character character;
    private MedicalStats medicalStats;
    private List<CompartmentSpaceWidget> renderedCompartmentSpaces;
    private CompartmentSpaceWidget activeWidget = null;

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
                        medicalStats.getMainCompartment().getCompartmentSpace(),
                this
                )
        );
    }

    public void addCompartmentSpace(Component name, CompartmentSpace compartmentSpace) {
        renderedCompartmentSpaces.add(new CompartmentSpaceWidget(
                20,
                20,
                100,
                100,
                name,
                compartmentSpace,
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
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (CompartmentSpaceWidget widget : renderedCompartmentSpaces.reversed()) {
            if (widget.mouseClicked(mouseX, mouseY, button)) {
                activeWidget = widget;
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

}

