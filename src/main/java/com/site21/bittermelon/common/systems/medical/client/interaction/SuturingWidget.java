package com.site21.bittermelon.common.systems.medical.client.interaction;

import com.site21.bittermelon.common.systems.character.networking.SetCharactersChanged;
import com.site21.bittermelon.common.systems.medical.client.CompartmentWidget;
import com.site21.bittermelon.common.systems.medical.client.HealthScreen;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.compartment.layer.Point;
import com.site21.bittermelon.common.systems.medical.networking.ExtractCompartment;
import com.site21.bittermelon.init.custom.Compartments;
import com.site21.bittermelon.init.neoforge.BitterSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SuturingWidget extends InteractionWidget {
    private final HealthScreen healthScreen;
    private final CompartmentWidget compartmentWidget;
    private final List<Point> suturePoints;
    private Point sutureStart = null;

    public SuturingWidget(int x, int y, HealthScreen healthScreen, CompartmentWidget compartmentWidget) {
        super(x, y, 1, 1, Component.literal("Suture"));
        this.healthScreen = healthScreen;
        this.compartmentWidget = compartmentWidget;
        this.suturePoints = new ArrayList<>();
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int size = compartmentWidget.getSlotSize();

        for (Point point : suturePoints) {
            guiGraphics.blitSprite(
                    RenderPipelines.GUI_TEXTURED,
                    ResourceLocation.withDefaultNamespace("pending_invite/reject"),
                    point.x(),
                    point.y(),
                    size,
                    size
            );
        }

        if (sutureStart != null) {
            float angle = (float) Math.atan2(mouseX - sutureStart.x(), mouseY - sutureStart.y());
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().rotateAbout(angle, sutureStart.x(), sutureStart.y());
            guiGraphics.fill(
                    sutureStart.x(),
                    sutureStart.y(),
                    (int) (sutureStart.x() + getDistance(sutureStart, new Point(mouseX, mouseY))),
                    sutureStart.y() + 1,
                    0xFFFFFFFF
            );
            guiGraphics.pose().popMatrix();
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Point hoveredSlot = compartmentWidget.getHoveredSlot((int) mouseX, (int) mouseY);
        if (hoveredSlot == null) return false;

        sutureStart = new Point(hoveredSlot.x() * compartmentWidget.getSlotSize() + compartmentWidget.getContentX(),
                hoveredSlot.y() * compartmentWidget.getSlotSize() + compartmentWidget.getContentY());

        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (sutureStart == null) return false;

        Point newPoint = new Point((int) mouseX, (int) mouseY);
        if (getDistance(sutureStart, newPoint) > compartmentWidget.getSlotSize()) {
            if (makeSuture(mouseX, mouseY)) {
                suturePoints.add(sutureStart);
                sutureStart = null;
                return true;
            }
        }

        return false;
    }

    private boolean makeSuture(double mouseX, double mouseY) {
        UUID hoveredCompartmentId = compartmentWidget.getHoveredCompartment((int) mouseX, (int) mouseY);
        if (hoveredCompartmentId == null) return false;

        CompartmentInstance hoveredCompartment = healthScreen.getMedicalStats().getCompartment(hoveredCompartmentId);
        if (!(hoveredCompartment.getCompartment().equals(Compartments.CUT.get()))) return false;

        ClientPacketDistributor.sendToServer(new ExtractCompartment(
                healthScreen.getCharacter().getId(),
                compartmentWidget.getCompartment().getId(),
                hoveredCompartmentId,
                compartmentWidget.getLayerIndex()));
        ClientPacketDistributor.sendToServer(new SetCharactersChanged());

        makeSound(BitterSounds.SCALPEL.value());

        return true;
    }

    private void makeSound(SoundEvent soundEvent) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(soundEvent, 1.0f));
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
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
