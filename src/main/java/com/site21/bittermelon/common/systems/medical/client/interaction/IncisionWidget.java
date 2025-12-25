package com.site21.bittermelon.common.systems.medical.client.interaction;

import com.site21.bittermelon.common.systems.character.networking.SetCharactersChanged;
import com.site21.bittermelon.common.systems.medical.client.CompartmentWidget;
import com.site21.bittermelon.common.systems.medical.client.HealthScreen;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentUtil;
import com.site21.bittermelon.common.systems.medical.compartment.MedicalAttribute;
import com.site21.bittermelon.common.systems.medical.compartment.VisualData;
import com.site21.bittermelon.common.systems.medical.compartment.layer.Point;
import com.site21.bittermelon.common.systems.medical.networking.AddAndInsertCompartment;
import com.site21.bittermelon.init.custom.Compartments;
import com.site21.bittermelon.init.neoforge.BitterSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ARGB;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.VISUAL_DATA;

public class IncisionWidget extends InteractionWidget {
    private static final long SOUND_DELAY = 2780;
    private static final int DISTANCE_COMPARISON_COUNT = 3;

    private final float efficiency;
    private final CompartmentWidget compartment;
    private final HealthScreen healthScreen;
    private final List<Point> drawnPoints = new ArrayList<>();
    private long lastSoundTime = 0;

    public IncisionWidget(int x, int y, float efficiency, CompartmentWidget compartment, HealthScreen healthScreen) {
        super(x, y, 1, 1, Component.literal("Incision"));
        this.efficiency = efficiency;
        this.compartment = compartment;
        this.healthScreen = healthScreen;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        for (Point point : drawnPoints) {
            guiGraphics.fill(point.x() - 1, point.y() - 1, point.x() + 1, point.y() + 1, 0xFFFF0000);
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button == 0) {
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
        double distance = Math.sqrt(Math.pow(newPoint.x() - lastPoint.x(), 2) + Math.pow(newPoint.y() - lastPoint.y(), 2)
        );

        return distance >= 2.0;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            finishIncision();
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

            float accuracy = calculateAccuracyFromDistance(relatedPoints.get(point)) * efficiency;
            int color = ARGB.setBrightness(0xFFFF0000, accuracy);

            VisualData visualData = cut.getOrDefault(VISUAL_DATA, VisualData.empty()).withColor(color);
            cut.set(VISUAL_DATA, visualData);
            CompartmentUtil.setAttribute(cut, MedicalAttribute.BLEED, 1.0f - accuracy);

            ClientPacketDistributor.sendToServer(new AddAndInsertCompartment(
                    healthScreen.getCharacter().getId(),
                    compartment.getCompartment().getId(),
                    cut,
                    compartment.getLayerIndex(),
                    point.x(),
                    point.y()
            ));
        }

        ClientPacketDistributor.sendToServer(new SetCharactersChanged());
    }

    private float calculateAccuracyFromDistance(@NotNull List<Point> points) {
        if (points.isEmpty()) return 0.0f;

        int compareCount = Math.min(points.size(), DISTANCE_COMPARISON_COUNT);
        double totalDistance = 0.0f;

        for (int i = 0; i < points.size() - 1; ++i) {
            Point pointA = points.get(i);
            for (int j = i + 1; j < i + compareCount; ++j) {
                if (j >= points.size()) break;
                totalDistance += getDistance(pointA, points.get(j));
            }

            for (int j = i - 1; j > i - compareCount; --j) {
                if (j <= 0) break;
                totalDistance += getDistance(pointA, points.get(j));
            }
        }

        float averageDistance = (float) (totalDistance / points.size());
        return Math.max(0.0f, 1.0f - (averageDistance / 10.0f));
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
