package com.site21.bittermelon.containment.client;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FacilityConsoleScreen extends Screen {
    protected FacilityConsoleScreen(Component title) {
        super(title);
    }
}
