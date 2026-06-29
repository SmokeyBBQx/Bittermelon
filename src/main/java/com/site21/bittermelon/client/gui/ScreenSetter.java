package com.site21.bittermelon.client.gui;

import com.site21.bittermelon.common.content.blocks.electronics.containmentpanel.ContainmentPanelBlockEntity;
import com.site21.bittermelon.common.content.blocks.electronics.intercom.IntercomBlockEntity;
import com.site21.bittermelon.common.content.blocks.electronics.intercom.client.IntercomScreen;
import com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.PersonnelTerminalBlockEntity;
import com.site21.bittermelon.common.content.blocks.electronics.personnelterminal.client.PersonnelTerminalScreen;
import com.site21.bittermelon.common.content.blocks.powergrid.distributionboard.DistributionBoardBlockEntity;
import com.site21.bittermelon.common.content.blocks.powergrid.distributionboard.client.DistributionBoardScreen;
import com.site21.bittermelon.common.content.blocks.stickynote.StickyNoteBlockEntity;
import com.site21.bittermelon.common.content.blocks.stickynote.client.StickyNoteScreen;
import com.site21.bittermelon.common.content.blocks.wallwriting.WallWritingBlockEntity;
import com.site21.bittermelon.common.content.blocks.wallwriting.client.WallWritingScreen;
import com.site21.bittermelon.common.content.items.scps.scp377.Fortune;
import com.site21.bittermelon.common.content.items.scps.scp377.client.SCP3771Screen;
import com.site21.bittermelon.common.content.items.wire.client.WireConnectionScreen;
import com.site21.bittermelon.common.content.items.wirecutters.client.WireCutterScreen;
import com.site21.bittermelon.common.systems.character.client.characterselection.CharacterSelectionScreen;
import com.site21.bittermelon.common.systems.containment.client.ContainmentPanelScreen;
import com.site21.bittermelon.common.systems.economy.bank.client.ATMScreen;
import com.site21.bittermelon.common.systems.electronics.ElectronicDevice;
import com.site21.bittermelon.common.systems.electronics.privilege.client.PrivilegeEditorScreen;
import com.site21.bittermelon.common.systems.medical.client.HealthScreen;
import com.site21.bittermelon.common.systems.personnel.registry.PersonnelEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class ScreenSetter {
    public static void displayATMScreen(PersonnelEntry entry) {
        Minecraft.getInstance().setScreen(new ATMScreen(entry));
    }

    public static void displayContainmentPanelScreen(ContainmentPanelBlockEntity blockEntity, boolean hasAccess) {
        Minecraft.getInstance().setScreen(new ContainmentPanelScreen(blockEntity, hasAccess));
    }

    public static void displayIntercomScreen(IntercomBlockEntity blockEntity, boolean canEdit) {
        Minecraft.getInstance().setScreen(new IntercomScreen(blockEntity, canEdit));
    }

    public static void displayWiringScreen(ElectronicDevice electronic, InteractionHand hand) {
        Minecraft.getInstance().setScreen(new WireConnectionScreen(electronic, hand));
    }

    public static void displayHealthScreen(@NotNull LivingEntity entity) {
        Minecraft.getInstance().setScreen(new HealthScreen(entity));
    }

    public static void displayPersonnelScreen(PersonnelTerminalBlockEntity terminalBlockEntity) {
        Minecraft.getInstance().setScreen(new PersonnelTerminalScreen(terminalBlockEntity));
    }

    public static void displayCharacterScreen(int maxCharacters) {
        Minecraft.getInstance().setScreen(new CharacterSelectionScreen(maxCharacters));
    }

    public static void displaySCP3771Screen(Fortune fortune) {
        Minecraft.getInstance().setScreen(new SCP3771Screen(fortune));
    }

    public static void displayPrivilegeEditorScreen(BlockEntity blockEntity) {
        Minecraft.getInstance().setScreen(new PrivilegeEditorScreen(blockEntity));
    }

    public static void displayDistributionBoardScreen(DistributionBoardBlockEntity distributionBoard) {
        Minecraft.getInstance().setScreen(new DistributionBoardScreen(distributionBoard));
    }

    public static void displayWallWritingScreen(WallWritingBlockEntity wallWriting) {
        Minecraft.getInstance().setScreen(new WallWritingScreen(wallWriting));
    }

    public static void displayStickyNoteScreen(StickyNoteBlockEntity stickyNote, int noteIndex) {
        Minecraft.getInstance().setScreen(new StickyNoteScreen(stickyNote, noteIndex));
    }

    public static void displayWireCutterScreen(ElectronicDevice electronic) {
        Minecraft.getInstance().setScreen(new WireCutterScreen(electronic));
    }
}
