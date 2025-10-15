package com.site21.bittermelon.common.systems.medical.client.screen.minigame;

import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.medical.client.screen.networking.CompleteMinigame;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.init.neoforge.BitterSounds;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.site21.bittermelon.common.systems.medical.client.screen.HealthScreenV2.openHealthScreen;

@OnlyIn(Dist.CLIENT)
public class IncisionMinigame extends MedicalMinigame {
    private final List<Point> drawnPoints = new ArrayList<>();
    private int lineX;
    private int startY, endY;
    private static final int DOT_LENGTH = 5;
    private static final int DOT_GAP = 5;
    private static final int MIN_POINTS_FOR_COMPLETION = 350;
    private final Random random = new Random();
    private int shakeTimer = 0;
    private int shakeX = 0;
    private int shakeY = 0;
    private int shakeXDraw = 0;
    private int shakeYDraw = 0;
    private static final int SHAKE_TIMER_THRESHOLD = 40;
    private float tremor = 0;

    private record Point(int x, int y) {
    }

    public IncisionMinigame(ItemStack item, CompartmentInstance compartment, MedicalStats medicalStats, Character character) {
        super(Component.literal("Make Incision"), item, compartment, medicalStats, character);
    }

    @Override
    protected void init() {
        lineX = width / 2;
        startY = height / 3;
        endY = (2 * height) / 3 + 5;
        SOUND_DELAY = 2780;
    }

    @Override
    public void tick() {
        super.tick();

        shakeTimer++;
        int shakeIntensity = (int) Math.min(tremor / 10, 10);

        if (shakeIntensity > 1) {
            if (shakeTimer >= SHAKE_TIMER_THRESHOLD / shakeIntensity) {
                shakeTimer = 0;
                shakeX = random.nextInt(shakeIntensity * 2) - shakeIntensity;
                shakeY = random.nextInt(shakeIntensity * 2) - shakeIntensity;
                shakeXDraw = random.nextInt(shakeIntensity * 2) - shakeIntensity;
                shakeYDraw = random.nextInt(shakeIntensity * 2) - shakeIntensity;
            }
        }

        if (drawnPoints.size() > MIN_POINTS_FOR_COMPLETION) {
            isCompleting = true;
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        drawDottedLine(guiGraphics, lineX + shakeX, startY + shakeY, endY + shakeY);

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(shakeXDraw, shakeYDraw);
        if (drawnPoints.size() > 1) {
            for (int i = 1; i < drawnPoints.size(); i++) {
                Point prev = drawnPoints.get(i - 1);
                Point curr = drawnPoints.get(i);
                guiGraphics.fill(prev.x - 1, prev.y - 1, curr.x + 1, curr.y + 1, isCompleting ? 0xFF1cba39 : 0xFFFF0000);

                if (drawnPoints.get(i).y > endY + 5) {
                    isCompleting = true;
                }
            }
        }
        guiGraphics.pose().popMatrix();

        guiGraphics.drawString(
                this.minecraft.font,
                String.format("Accuracy: %.1f%%", calculateAccuracy() * 100),
                10,
                10,
                0xFFFFFFFF
        );


        guiGraphics.renderFakeItem(item, mouseX - 8, mouseY - 8);
    }

    private void drawDottedLine(GuiGraphics guiGraphics, int x, int startY, int endY) {
        int y = startY;
        while (y < endY) {
            int segmentEnd = Math.min(y + DOT_LENGTH, endY);
            guiGraphics.vLine(x, y, segmentEnd, -1);
            y += DOT_LENGTH + DOT_GAP;
        }
    }

    private float calculateAccuracy() {
        if (drawnPoints.isEmpty()) {
            return 0;
        }

        double totalDistance = 0;
        int points = 0;

        for (Point p : drawnPoints) {
            if (p.y >= startY && p.y <= endY) {
                int distance = Math.abs(p.x - lineX);
                totalDistance += distance;
                points++;
            }
        }

        double avgDistance = totalDistance / points;
        return Math.max(0, 1.0f - (float) (avgDistance / 20.0f));
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button == 0 && !isCompleting) {
            drawnPoints.add(new Point((int) mouseX, (int) mouseY));
            makeSound(BitterSounds.SCALPEL.get());
            return true;
        }
        return false;
    }

    @Override
    protected void complete() {
        if (getMinecraft().player == null) return;
        ClientPacketDistributor.sendToServer(new CompleteMinigame(item,
                compartment.getUUID(),
                character.getUUID(),
                getMinecraft().player.getUUID(),
                calculateAccuracy()));
        this.onClose();
        openHealthScreen();
    }

    public void setTremor(float tremor) {
        this.tremor = tremor;
    }
}
