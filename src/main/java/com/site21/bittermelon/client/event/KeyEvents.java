package com.site21.bittermelon.client.event;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.character.networking.OpenCharacterScreenC2S;
import com.site21.bittermelon.common.systems.medical.client.HealthScreen;
import com.site21.bittermelon.common.systems.throwing.ThrowItemPacket;
import com.site21.bittermelon.init.neoforge.BitterAttachmentTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import static com.site21.bittermelon.init.neoforge.BitterKeyBindings.*;

@EventBusSubscriber(modid = Bittermelon.MOD_ID)
public class KeyEvents {
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (THROW_ITEM_KEY.get().consumeClick()) {
            Minecraft.getInstance().player.swing(InteractionHand.MAIN_HAND);
            ClientPacketDistributor.sendToServer(new ThrowItemPacket(Minecraft.getInstance().player.getUUID()));
        } else if (HEALTH_SCREEN_KEY.get().consumeClick()) {
            HealthScreen.openHealthScreen();
        } else if (CHARACTER_KEY.get().consumeClick()) {
            ClientPacketDistributor.sendToServer(new OpenCharacterScreenC2S(Minecraft.getInstance().player.getUUID()));
        } else if (SCREAM_KEY.get().consumeClick()) {
            sendScreamMessage();
        } else if (LAUGH_KEY.get().consumeClick()) {
            sendLaughMessage();
        } else if (SIGHS_KEY.get().consumeClick()) {
            sendSighsMessage();
        } else if (SMILES_KEY.get().consumeClick()) {
            sendSmilesMessage();
        } else if (SHRUGS_KEY.get().consumeClick()) {
            sendShrugsMessage();
        } else if (COUGHS_KEY.get().consumeClick()) {
            sendCoughsMessage();
        }
    }

    private static void sendScreamMessage() {
        Minecraft mc = Minecraft.getInstance();
        assert mc.level != null;
        assert mc.player != null;

        long time = mc.level.getGameTime();
        if (time - mc.player.getData(BitterAttachmentTypes.LAST_SCREAM_TIME.get()) < 60) return;

        Character character = CharacterManager.get(mc.level).getActiveCharacter(mc.player);
        if (character != null) {
            mc.player.connection.sendChat("*" + character.getName() + " screams!*");
            mc.player.setData(BitterAttachmentTypes.LAST_SCREAM_TIME.get(), time);
        }
    }

    private static void sendLaughMessage() {
        Minecraft mc = Minecraft.getInstance();
        assert mc.level != null;
        assert mc.player != null;

        long time = mc.level.getGameTime();
        if (time - mc.player.getData(BitterAttachmentTypes.LAST_LAUGH_TIME.get()) < 60) return;

        Character character = CharacterManager.get(mc.level).getActiveCharacter(mc.player);
        if (character != null) {
            mc.player.connection.sendChat("*" + character.getName() + " laughs!*");
            mc.player.setData(BitterAttachmentTypes.LAST_LAUGH_TIME.get(), time);
        }
    }

    private static void sendSighsMessage() {
        Minecraft mc = Minecraft.getInstance();
        assert mc.level != null;
        assert mc.player != null;

        long time = mc.level.getGameTime();
        if (time - mc.player.getData(BitterAttachmentTypes.LAST_SIGHS_TIME.get()) < 60) return;

        Character character = CharacterManager.get(mc.level).getActiveCharacter(mc.player);
        if (character != null) {
            mc.player.connection.sendChat("*" + character.getName() + " sighs*");
            mc.player.setData(BitterAttachmentTypes.LAST_SIGHS_TIME.get(), time);
        }
    }

    private static void sendSmilesMessage() {
        Minecraft mc = Minecraft.getInstance();
        assert mc.level != null;
        assert mc.player != null;

        long time = mc.level.getGameTime();
        if (time - mc.player.getData(BitterAttachmentTypes.LAST_SMILES_TIME.get()) < 60) return;

        Character character = CharacterManager.get(mc.level).getActiveCharacter(mc.player);
        if (character != null) {
            mc.player.connection.sendChat("*" + character.getName() + " smiles*");
            mc.player.setData(BitterAttachmentTypes.LAST_SMILES_TIME.get(), time);
        }
    }

    private static void sendShrugsMessage() {
        Minecraft mc = Minecraft.getInstance();
        assert mc.level != null;
        assert mc.player != null;

        long time = mc.level.getGameTime();
        if (time - mc.player.getData(BitterAttachmentTypes.LAST_SHRUGS_TIME.get()) < 60) return;

        Character character = CharacterManager.get(mc.level).getActiveCharacter(mc.player);
        if (character != null) {
            mc.player.connection.sendChat("*" + character.getName() + " shrugs*");
            mc.player.setData(BitterAttachmentTypes.LAST_SHRUGS_TIME.get(), time);
        }
    }

    private static void sendCoughsMessage() {
        Minecraft mc = Minecraft.getInstance();
        assert mc.level != null;
        assert mc.player != null;

        long time = mc.level.getGameTime();
        if (time - mc.player.getData(BitterAttachmentTypes.LAST_COUGHS_TIME.get()) < 60) return;

        Character character = CharacterManager.get(mc.level).getActiveCharacter(mc.player);
        if (character != null) {
            mc.player.connection.sendChat("*" + character.getName() + " coughs*");
            mc.player.setData(BitterAttachmentTypes.LAST_COUGHS_TIME.get(), time);
        }
    }

}
