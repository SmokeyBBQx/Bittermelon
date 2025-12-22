package com.site21.bittermelon.common.systems.medical.client.interaction;

import com.site21.bittermelon.common.systems.medical.client.CompartmentWidget;
import com.site21.bittermelon.common.systems.medical.client.HealthScreen;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.compartment.layer.Point;
import com.site21.bittermelon.common.systems.medical.networking.ExtractCompartment;
import com.site21.bittermelon.init.custom.Compartments;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SutureWidget extends InteractionWidget {
    private final CompartmentWidget compartment;
    private final HealthScreen healthScreen;
    private final List<Point> suturePoints;

    public SutureWidget(int x, int y, CompartmentWidget compartment, HealthScreen healthScreen) {
        super(x, y, 1, 1, Component.literal("Suture"));
        this.compartment = compartment;
        this.healthScreen = healthScreen;
        this.suturePoints = new ArrayList<>();
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        for (Point point : suturePoints) {
            guiGraphics.fill(point.x() - 1, point.y() - 1, point.x() + 1, point.y() + 1, 0xFF00FF00);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        UUID hoveredCompartmentId = compartment.getHoveredCompartment((int) mouseX, (int) mouseY);
        if (hoveredCompartmentId == null) return false;

        CompartmentInstance hoveredCompartment = healthScreen.getMedicalStats().getCompartment(hoveredCompartmentId);
        if (hoveredCompartment == null) return false;

        if (!(hoveredCompartment.getCompartment().equals(Compartments.CUT.get()))) return false;

        ClientPacketDistributor.sendToServer(new ExtractCompartment(
                healthScreen.getCharacter().getId(),
                compartment.getCompartment().getId(),
                hoveredCompartmentId,
                compartment.getLayerIndex()));

        Point hoveredSlot = compartment.getHoveredSlot((int) mouseX, (int) mouseY);
        if (hoveredSlot != null) {
            suturePoints.add(hoveredSlot);
        }

        return true;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
