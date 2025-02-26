package com.site21.bittermelon.client.mainmenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class MainMenuScreen extends Screen {
    private final Player player;

    public MainMenuScreen(Player player) {
        super(Component.literal("Main Menu"));
        this.player = player;
    }

    @Override
    protected void init() {

    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {

    }
}
