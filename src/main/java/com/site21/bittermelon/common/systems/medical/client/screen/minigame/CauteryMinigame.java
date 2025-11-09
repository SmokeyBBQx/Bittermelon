package com.site21.bittermelon.common.systems.medical.client.screen.minigame;

import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.init.neoforge.BitterSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class CauteryMinigame extends MedicalMinigame {
    private final List<BleedPoint> bleedPoints = new ArrayList<>();
    private final List<BleedPoint> clampedPoints = new ArrayList<>();
    private int bleedTime;

    private static class BleedPoint {
        int x, y;
        boolean isClamped = false;
        float bleedRate = 1.0f;
    }

    public CauteryMinigame(ItemStack item, CompartmentInstance compartment, MedicalStats medicalStats, Character character) {
        super(Component.literal("Clamp Blood Vessels"), item, compartment, medicalStats, character);
    }

    @Override
    protected void init() {
        int areaLeft = width / 4;
        int areaRight = (width * 3) / 4;
        int areaTop = height / 3;
        int areaBottom = (height * 2) / 3;

        Random random = new Random();
        int numPoints = 3 + random.nextInt(3);
        for (int i = 0; i < numPoints; i++) {
            BleedPoint point = new BleedPoint();
            point.x = areaLeft + random.nextInt(areaRight - areaLeft);
            point.y = areaTop + random.nextInt(areaBottom - areaTop);

            if (isValidPointLocation(point)) {
                bleedPoints.add(point);
            } else {
                i--;
            }
        }
    }

    private boolean isValidPointLocation(BleedPoint newPoint) {
        for (BleedPoint existing : bleedPoints) {
            double distance = Math.sqrt(Math.pow(newPoint.x - existing.x, 2) + Math.pow(newPoint.y - existing.y, 2));
            if (distance < 30) return false;
        }
        return true;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        bleedTime++;

        for (BleedPoint point : bleedPoints) {
            int color = point.isClamped ? 0xFF00FF00 : 0xFFFF0000;
            guiGraphics.fill(point.x - 5, point.y - 5, point.x + 5 + bleedTime / 200,
                    point.y + 5 + bleedTime / 200, color);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (BleedPoint point : bleedPoints) {
            if (isNear(mouseX, mouseY, point)) {
                if (!point.isClamped) {
                    point.isClamped = true;
                    clampedPoints.add(point);

                    Minecraft.getInstance().player.playSound(
                            BitterSounds.CAUTERY.get(),
                            1f,
                            1.0f
                    );

                    if (clampedPoints.size() == bleedPoints.size()) {
                        complete();
                    }

                    return true;
                }
            }
        }
        return false;
    }

    private boolean isNear(double x, double y, BleedPoint point) {
        return Math.abs(x - point.x) < 10 && Math.abs(y - point.y) < 10;
    }
}
