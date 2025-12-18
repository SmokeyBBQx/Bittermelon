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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
        if (button == 0) {
            drawnPoints.add(new Point((int) mouseX, (int) mouseY));
            makeSound(BitterSounds.SCALPEL.value());
            return true;
        }
        return false;
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
        for (Point point : drawnPoints) {
            Point slot = compartment.getHoveredSlot(point.x(), point.y());
            if (touchedSlots.contains(slot)) continue;

            if (slot != null) {
                touchedSlots.add(slot);
            }
        }

        for (Point point : touchedSlots) {
            CompartmentInstance cut = Compartments.CUT.get().toInstance();
            healthScreen.getMedicalStats().addCompartment(cut);
            compartment.getLayer().tryToPlace(point.x(), point.y(), cut);
        }
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
