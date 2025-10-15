package com.site21.bittermelon.content.items.handheldprogrammer.client;

import com.site21.bittermelon.systems.electronics.wiring.PLC;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ProgrammingScreen extends Screen {
    private final PLC plc;

    public ProgrammingScreen(PLC plc) {
        super(Component.literal("PLC PROGRAMMING"));
        this.plc = plc;
    }

    @Override
    protected void init() {

    }


}
