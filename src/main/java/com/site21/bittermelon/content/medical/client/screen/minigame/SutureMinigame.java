package com.site21.bittermelon.content.medical.client.screen.minigame;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class SutureMinigame extends MedicalMinigame {
    public SutureMinigame(ItemStack item, CompartmentInstance compartment, MedicalStats medicalStats, Character character) {
        super(Component.literal("Suture"), item, compartment, medicalStats, character);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

    }
}
