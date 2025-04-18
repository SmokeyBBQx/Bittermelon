package com.site21.bittermelon.content.medical.client.screen;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class HealthScreenV2 extends Screen {
    private final Character character;
    private MedicalStats medicalStats;
    private Set<CompartmentSpaceWidget> compartmentSpaces;

    public HealthScreenV2(@NotNull Character character) {
        super(Component.literal(character.getName()));
        this.character = character;
        this.medicalStats = character.getMedicalStats();

        // TODO: Implement client listener for medical stats
    }

    @Override
    protected void init() {

    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

    }
}
