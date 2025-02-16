package com.site21.bittermelon.content.character.client;

import com.site21.bittermelon.content.character.Character;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class CharacterScreen extends Screen {
    private final Character character;
    private final boolean isModifiable;

    public CharacterScreen(@NotNull Character character, boolean isModifiable) {
        super(Component.literal(character.getName()));
        this.character = character;
        this.isModifiable = isModifiable;
    }


    @Override
    protected void init() {
        super.init();


    }

        @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int textX = width / 2;
        int textY = width / 2;
        int textColor = 0xFFFFFF;

        guiGraphics.drawString(
                Minecraft.getInstance().font,
                character.getName(),
                textX,
                textY,
                textColor
        );

        guiGraphics.drawWordWrap(
                Minecraft.getInstance().font,
                Component.literal(character.getDescription()),
                textX,
                textY,
                10,
                textColor
        );
    }

}
