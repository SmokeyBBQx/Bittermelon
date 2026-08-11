package com.site21.bittermelon.common.systems.medical.legacy.client.interaction;

import com.site21.bittermelon.common.systems.medical.legacy.client.CompartmentWidget;
import com.site21.bittermelon.common.systems.medical.legacy.client.HealthScreen;
import com.site21.bittermelon.common.systems.medical.legacy.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.legacy.compartment.layer.Point;
import com.site21.bittermelon.common.systems.medical.legacy.networking.RemoveCompartment;
import com.site21.bittermelon.init.custom.Compartments;
import com.site21.bittermelon.init.neoforge.BitterSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SuturingWidget extends InteractionWidget {
    private final HealthScreen screen;
    private final CompartmentWidget compartmentWidget;
    private final List<Point> suturePoints;
    private Point sutureStart = null;

    public SuturingWidget(int x, int y, HealthScreen screen, CompartmentWidget compartmentWidget) {
        super(x, y, 1, 1, Component.literal("Suture"));
        this.screen = screen;
        this.compartmentWidget = compartmentWidget;
        this.suturePoints = new ArrayList<>();
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        int size = compartmentWidget.getSlotSize();

        for (Point point : suturePoints) {
            graphics.blitSprite(
                    RenderPipelines.GUI_TEXTURED,
                    Identifier.withDefaultNamespace("pending_invite/reject"),
                    point.x(),
                    point.y(),
                    size,
                    size
            );
        }

        if (sutureStart != null) {
            float angle = (float) Math.atan2(mouseY - sutureStart.y(), mouseX - sutureStart.x());
            graphics.pose().pushMatrix();
            graphics.pose().rotateAbout(angle, sutureStart.x(), sutureStart.y());
            graphics.fill(
                    sutureStart.x(),
                    sutureStart.y(),
                    (int) (sutureStart.x() + getDistance(sutureStart, new Point(mouseX, mouseY))),
                    sutureStart.y() + 1,
                    0xFFFFFFFF
            );
            graphics.pose().popMatrix();
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        Point hoveredSlot = compartmentWidget.getHoveredSlot((int) event.x(), (int) event.y());
        if (hoveredSlot == null) return false;

        UUID hoveredCompartmentId = compartmentWidget.getHoveredCompartment((int) event.x(), (int) event.y());
        if (hoveredCompartmentId == null) return true;

        sutureStart = new Point((int) event.x(), (int) event.y());

        return true;
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        if (sutureStart == null) return false;

        int slotSize = compartmentWidget.getSlotSize();

        Point newPoint = new Point((int) event.x(), (int) event.y());
        if (getDistance(sutureStart, newPoint) > slotSize) {
            if (makeSuture(event.x(), event.y())) {
                Point hoveredSlot = compartmentWidget.getHoveredSlot(sutureStart.x(), sutureStart.y());
                if (hoveredSlot != null) {
                    suturePoints.add(new Point(compartmentWidget.getContentX() + hoveredSlot.x() * slotSize,
                            compartmentWidget.getContentY() + hoveredSlot.y() * slotSize));
                }

                sutureStart = null;
                return true;
            }
        }

        return false;
    }

    private boolean makeSuture(double mouseX, double mouseY) {
        UUID hoveredCompartmentId = compartmentWidget.getHoveredCompartment((int) mouseX, (int) mouseY);
        if (hoveredCompartmentId == null) return false;

        CompartmentInstance hoveredCompartment = screen.getMedicalStats().getCompartment(hoveredCompartmentId);
        if (!(hoveredCompartment.getCompartment().equals(Compartments.CUT.get()))) return false;

        ClientPacketDistributor.sendToServer(new RemoveCompartment(
                screen.getEntity().getUUID(),
                compartmentWidget.getCompartment().getId(),
                hoveredCompartmentId
        ));

        makeSound(BitterSounds.SCALPEL.value());

        screen.sendMessage("*" + screen.getPlayerName() + " sutures " +
                screen.getTargetName() + "'s " + compartmentWidget.getCompartment().getName() + ".*");

        return true;
    }

    private void makeSound(SoundEvent soundEvent) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(soundEvent, 1.0f));
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        sutureStart = null;
        return true;
    }

    public CompartmentWidget getCompartmentWidget() {
        return compartmentWidget;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
