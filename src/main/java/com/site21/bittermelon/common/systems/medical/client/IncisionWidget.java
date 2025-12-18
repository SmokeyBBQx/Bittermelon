package com.site21.bittermelon.common.systems.medical.client;

import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.compartment.layer.Point;
import com.site21.bittermelon.init.custom.Compartments;
import com.site21.bittermelon.init.neoforge.BitterSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IncisionWidget extends AbstractWidget {
    protected static long SOUND_DELAY = 2780;
    private final CompartmentWidget compartment;
    private final HealthScreen healthScreen;
    private final List<Point> drawnPoints = new ArrayList<>();
    private long lastSoundTime = 0;

    public IncisionWidget(int x, int y, CompartmentWidget compartment, HealthScreen healthScreen) {
        super(x, y, 1, 1, Component.literal("Incision"));
        this.compartment = compartment;
        this.healthScreen = healthScreen;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        for (Point point : drawnPoints) {
            guiGraphics.fill(point.x() - 1, point.y() - 1, point.x() + 1, point.y() + 1, 0xFFFF0000);
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button == 0 && (dragX > 0 || dragY > 0 || dragX <= -1 || dragY <= -1)) {
            Point newPoint = new Point((int) mouseX, (int) mouseY);
            if (isPointFarEnough(newPoint)) {
                drawnPoints.add(newPoint);
                makeSound(BitterSounds.SCALPEL.value());
                return true;
            }
        }
        return false;
    }

    private boolean isPointFarEnough(Point newPoint) {
        if (drawnPoints.isEmpty()) {
            return true;
        }

        Point lastPoint = drawnPoints.getLast();
        double distance = Math.sqrt(
                Math.pow(newPoint.x() - lastPoint.x(), 2) +
                        Math.pow(newPoint.y() - lastPoint.y(), 2)
        );

        return distance >= 3.0;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            finishIncision();
            healthScreen.removeWidget(this);
            return true;
        }
        return false;
    }

    private void finishIncision() {
        List<Point> touchedSlots = new ArrayList<>();
        Map<Point, List<Point>> relatedPoints = new HashMap<>();

        for (Point point : drawnPoints) {
            Point slot = compartment.getHoveredSlot(point.x(), point.y());
            relatedPoints.computeIfAbsent(slot, k -> new ArrayList<>()).add(point);
            if (touchedSlots.contains(slot)) continue;

            if (slot != null) {
                touchedSlots.add(slot);
            }
        }

        for (Point point : touchedSlots) {
            CompartmentInstance cut = Compartments.CUT.get().toInstance();
            // TODO: Accuracy calculation is weird and inconsistent
            float accuracy = calculateAccuracy(relatedPoints.get(point));
            int color = ARGB.setBrightness(0xFFFF0000, 2 - accuracy);
            cut.getVisualData().color(color);
            healthScreen.getMedicalStats().addCompartment(cut);
            compartment.getLayer().tryToPlace(point.x(), point.y(), cut);
        }
    }

    private float calculateAccuracy(@NotNull List<Point> points) {
        return points.isEmpty() ? 0.0f : (float) points.size() / compartment.getSlotSize() * 2;
    }

    private void makeSound(SoundEvent soundEvent) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSoundTime > SOUND_DELAY) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(soundEvent, 1.0f));
            lastSoundTime = currentTime;
        }
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrationElementOutput) {

    }
}
