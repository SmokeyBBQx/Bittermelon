package com.site21.bittermelon.content.items.handheldprogrammer.client;

import com.site21.bittermelon.content.blocks.devices.connection.PLC;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ProgrammingScreen extends Screen {
    private final PLC plc;

    public ProgrammingScreen(PLC plc) {
        super(Component.literal("Handheld Programmer"));
        this.plc = plc;
    }

    protected void init() {

    }



}
