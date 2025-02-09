package com.site21.bittermelon.economy;

import com.site21.bittermelon.database.PersonnelEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ATMScreen extends Screen {
    private final PersonnelEntry user;

    protected ATMScreen(Component title, PersonnelEntry personnel) {
        super(title);
        this.user = personnel;
    }


}
