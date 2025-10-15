package com.site21.bittermelon.common.systems.medical.client.screen.minigame;

import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.medical.client.screen.networking.CompleteMinigame;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.common.systems.medical.client.screen.HealthScreenKeyBind.openHealthScreen;

@OnlyIn(Dist.CLIENT)
public abstract class MedicalMinigame extends Screen {
    protected final ItemStack item;
    protected final CompartmentInstance compartment;
    protected final MedicalStats medicalStats;
    protected final Character character;
    protected int completionTime;
    protected int COMPLETION_DELAY = 300;
    protected boolean isCompleting = false;
    protected long lastSoundTime = 0;
    protected static long SOUND_DELAY;


    protected MedicalMinigame(Component title, ItemStack item, CompartmentInstance compartment, MedicalStats medicalStats, Character character) {
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
        if (getMinecraft().player == null) return;
        PacketDistributor.sendToServer(new CompleteMinigame(item,
                compartment.getUUID(),
                character.getUUID(),
                getMinecraft().player.getUUID(),
                1));

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
