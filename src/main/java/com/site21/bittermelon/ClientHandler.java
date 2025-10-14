package com.site21.bittermelon;

import com.site21.bittermelon.content.blocks.devices.ElectronicDevice;
import com.site21.bittermelon.content.blocks.devices.implementations.containmentpanel.ContainmentPanelBlockEntity;
import com.site21.bittermelon.content.blocks.devices.implementations.intercom.IntercomBlockEntity;
import com.site21.bittermelon.content.blocks.devices.implementations.intercom.client.IntercomScreen;
import com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.PersonnelTerminalBlockEntity;
import com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.client.PersonnelTerminalScreen;
import com.site21.bittermelon.content.blocks.devices.privilege.client.PrivilegeEditorScreen;
import com.site21.bittermelon.content.blocks.powergrid.distributionboard.DistributionBoardBlockEntity;
import com.site21.bittermelon.content.blocks.powergrid.distributionboard.client.DistributionBoardScreen;
import com.site21.bittermelon.content.blocks.stickynote.StickyNoteBlockEntity;
import com.site21.bittermelon.content.blocks.stickynote.client.StickyNoteScreen;
import com.site21.bittermelon.content.blocks.wallwriting.WallWritingBlockEntity;
import com.site21.bittermelon.content.blocks.wallwriting.client.WallWritingScreen;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.client.characterselection.CharacterSelectionScreen;
import com.site21.bittermelon.content.containment.client.ContainmentPanelScreen;
import com.site21.bittermelon.content.economy.bank.client.ATMScreen;
import com.site21.bittermelon.content.items.scps.scp377.Fortune;
import com.site21.bittermelon.content.items.scps.scp377.client.SCP3771Screen;
import com.site21.bittermelon.content.items.wirecutter.client.WireCutterScreen;
import com.site21.bittermelon.content.items.wires.wire.client.WireConnectionScreen;
import com.site21.bittermelon.content.items.writablepaper.client.PaperEditScreen;
import com.site21.bittermelon.content.medical.client.screen.HealthScreenV2;
import com.site21.bittermelon.content.personnel.registry.PersonnelEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

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

    public static void displayWiringScreen(ElectronicDevice electronic, InteractionHand hand) {
        Minecraft.getInstance().setScreen(new WireConnectionScreen(electronic, hand));
    }

    public static void displayHealthScreen(@NotNull Character character, Player player, ItemStack heldItem) {
        Minecraft.getInstance().setScreen(new HealthScreenV2(character));
    }

    public static void displayPaperEditScreen(ItemStack paper) {
        Minecraft.getInstance().setScreen(new PaperEditScreen(paper));
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
