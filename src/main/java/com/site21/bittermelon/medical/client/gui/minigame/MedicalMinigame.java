package com.site21.bittermelon.medical.client.gui.minigame;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.items.medical.MedicalItem;
import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.medicalstats.MedicalStats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.keybinds.HealthScreenKeyBind.openHealthScreen;

public abstract class MedicalMinigame extends Screen {
    protected final ItemStack item;
    protected final Compartment compartment;
    protected final MedicalStats medicalStats;
    protected final Character character;
    protected int completionTime;
    protected int COMPLETION_DELAY = 300;
    protected boolean isCompleting = false;
    protected long lastSoundTime = 0;
    protected static long SOUND_DELAY;


    protected MedicalMinigame(Component title, ItemStack item, Compartment compartment, MedicalStats medicalStats, Character character) {
        super(title);
        this.item = item;
        this.compartment = compartment;
        this.medicalStats = medicalStats;
        this.character = character;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    protected void complete() {
        if (item.getItem() instanceof MedicalItem medicalItem) {
            medicalItem.finishAction(compartment, medicalStats, character, 1, item);
            medicalItem.consumeItem(item, getMinecraft().player);
        }
        this.onClose();
        openHealthScreen();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        if (isCompleting) {
            completionTime += 1;
            if (completionTime >= COMPLETION_DELAY) {
                complete();
            }
        }
    }

    protected void makeSound(SoundEvent sound) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSoundTime >= SOUND_DELAY) {
            Minecraft.getInstance().player.playSound(
                    sound,
                    1f,
                    1.0f
            );
            lastSoundTime = currentTime;
        }
    }
}
