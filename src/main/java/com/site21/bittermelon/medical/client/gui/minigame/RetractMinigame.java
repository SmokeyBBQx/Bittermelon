package com.site21.bittermelon.medical.client.gui.minigame;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.init.BitterSounds;
import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.medicalstats.MedicalStats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

public class RetractMinigame extends MedicalMinigame {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/gui/retract_sprites.png");
    private boolean retracting = false;
    private int retractDown = height / 2 + 130;
    private int retractUp = height / 2 + 130;
    private boolean spaceWasPressed = false;

    public RetractMinigame(ItemStack item, Compartment compartment, MedicalStats medicalStats, Character character) {
        super(Component.literal("Retract"), item, compartment, medicalStats, character);
    }

    @Override
    protected void init() {
        COMPLETION_DELAY = 200;
        SOUND_DELAY = 1690;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        int centerX = width / 2 - 192;
        int centerY = height / 2 - 40;
        float scale = 4.0f;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scale, scale, 1);

        if (!retracting) {
            guiGraphics.blit(
                    TEXTURE,
                    (int) (centerX / scale),
                    (int) (centerY / scale) - 11,
                    0,
                    0,
                    95,
                    20,
                    192,
                    40
            );

            guiGraphics.blit(
                    TEXTURE,
                    (int) (centerX / scale),
                    (int) (centerY / scale) + 11,
                    96,
                    0,
                    95,
                    20,
                    192,
                    40
            );
        } else {
            guiGraphics.blit(
                    TEXTURE,
                    (int) (centerX / scale),
                    (int) (retractUp / scale) - 11,
                    0,
                    20,
                    95,
                    20,
                    192,
                    40
            );

            guiGraphics.blit(
                    TEXTURE,
                    (int) (centerX / scale),
                    (int) (retractDown / scale) + 11,
                    96,
                    20,
                    95,
                    20,
                    192,
                    40
            );
        }

        guiGraphics.pose().popPose();

        if (retractUp < height / 6) {
            isCompleting = true;
        }

        String text = "PRESS SPACE TO RETRACT";
        int textWidth = Minecraft.getInstance().font.width(text);
        if (!retracting) {
            guiGraphics.drawString(
                    Minecraft.getInstance().font,
                    Component.literal(text),
                    width / 2 - textWidth / 2,
                    height / 2 + 30,
                    0x808080
            );
        }
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_SPACE) {
            if (!isCompleting && !spaceWasPressed) {
                retracting = true;
                int retractIncrement = 4;
                retractUp -= retractIncrement;
                retractDown += retractIncrement;
                makeSound(BitterSounds.RETRACT.get());
                spaceWasPressed = true;
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_SPACE) {
            if (!isCompleting) {
                spaceWasPressed = false;
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}
