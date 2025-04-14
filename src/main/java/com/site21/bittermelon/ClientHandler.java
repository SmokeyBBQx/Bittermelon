package com.site21.bittermelon;

import com.site21.bittermelon.content.blocks.devices.IElectronic;
import com.site21.bittermelon.content.blocks.devices.implementations.containmentpanel.ContainmentPanelBlockEntity;
import com.site21.bittermelon.content.blocks.devices.implementations.intercom.IntercomBlockEntity;
import com.site21.bittermelon.content.blocks.devices.implementations.intercom.client.IntercomScreen;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.containment.client.ContainmentPanelScreen;
import com.site21.bittermelon.content.economy.client.ATMScreen;
import com.site21.bittermelon.content.items.wires.wire.client.WiringScreen;
import com.site21.bittermelon.content.medical.client.screen.HealthScreen;
import com.site21.bittermelon.content.personnel.PersonnelEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ClientHandler {
    public static void displayATMScreen(PersonnelEntry entry) {
        Minecraft.getInstance().setScreen(new ATMScreen(entry));
    }

    public static void displayContainmentPanelScreen(ContainmentPanelBlockEntity blockEntity, boolean hasAccess) {
        Minecraft.getInstance().setScreen(new ContainmentPanelScreen(blockEntity, hasAccess));
    }

    public static void displayIntercomScreen(IntercomBlockEntity blockEntity, boolean canEdit) {
        Minecraft.getInstance().setScreen(new IntercomScreen(blockEntity, canEdit));
    }

    public static void displayWiringScreen(IElectronic electronic, ItemStack wiringStack) {
        Minecraft.getInstance().setScreen(new WiringScreen(electronic, wiringStack));
    }

    public static void displayHealthScreen(Character character, Player player, ItemStack heldItem) {
        Minecraft.getInstance().setScreen(new HealthScreen(character, player, heldItem));
    }
}
