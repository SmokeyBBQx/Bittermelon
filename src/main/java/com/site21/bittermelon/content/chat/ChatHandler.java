package com.site21.bittermelon.content.chat;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.init.VerbSets;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ServerChatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EventBusSubscriber(modid = Bittermelon.MOD_ID)
public class ChatHandler {
    private static final Pattern EMOTE_PATTERN = Pattern.compile("\\*(.*?)\\*|([^*]+)");
    private static final int DEFAULT_GRAY = 0xFF808080;

    public static final int WHISPER_RANGE = 5;
    public static final int NORMAL_RANGE = 16;
    public static final int SHOUT_RANGE = 32;

    @SubscribeEvent
    public static void onChatMessage(@NotNull ServerChatEvent event) {
        ServerPlayer player = event.getPlayer();
        Character character = CharacterManager.get(player.level()).getActiveCharacter(player);
        String message = event.getMessage().getString();

        event.setCanceled(true);

        if (character == null) {
            player.sendSystemMessage(Component.literal("You must select a valid character before sending a chat message.").withColor(0xFFFF0000));
            return;
        }

        sendRPMessage(character, player, message, NORMAL_RANGE, VerbSets.HUMAN.get());
    }

    public static void sendRPMessage(@NotNull Character character, ServerPlayer player, @NotNull String message, int range, String verb) {
        int emoteColor = character.getEmoteColor();
        MutableComponent messageComponent = Component.empty();

        if (!message.contains("*")) {
            messageComponent.append(Component.literal(character.getName() + " " + verb + ", ").withColor(emoteColor));
        } else {
            messageComponent.append(Component.literal("(" + character.getName() + ") ").withColor(emoteColor));
        }

        Matcher matcher = EMOTE_PATTERN.matcher(message);
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                String emoteText = matcher.group(1);
                Component emote = Component.literal(emoteText).withColor(emoteColor);
                messageComponent.append(emote).append(" ");
            } else {
                String dialogueText = matcher.group(2).trim();
                Component dialogue = Component.literal("\"" + dialogueText + "\" ");
                messageComponent.append(dialogue);
            }
        }

        sendMessage(messageComponent, player, range);
    }

    public static void sendRPMessage(@NotNull Character character, ServerPlayer player, String message, int range, @NotNull VerbSet verbSet) {
        sendRPMessage(character, player, message, range, verbSet.getVerb(message));
    }

    public static void sendOOCMessage(@NotNull ServerPlayer player, @NotNull Character character, String message) {
        String nameFormat = "(OOC) (" + character.getName() + ") " + player.getName().getString() + ": " + message;
        Component messageComponent = Component.literal(nameFormat + message).withColor(DEFAULT_GRAY);
        sendMessage(messageComponent, player);
    }

    public static void sendLOOCMessage(@NotNull ServerPlayer player, @NotNull Character character, String message) {
        String nameFormat = "(LOOC) (" + character.getName() + ") " + player.getName().getString() + ": " + message;
        Component messageComponent = Component.literal(nameFormat + message).withColor(DEFAULT_GRAY);
        sendMessage(messageComponent, player, 16);
    }

    public static void sendMessage(Component message, @NotNull ServerPlayer player, int range) {
        for (ServerPlayer serverPlayer : player.server.getPlayerList().getPlayers()) {
            if (player.distanceTo(serverPlayer) <= range) {
                serverPlayer.sendSystemMessage(message);
            }
        }
    }

    public static void sendMessage(Component message, @NotNull ServerPlayer player) {
        for (ServerPlayer serverPlayer : player.server.getPlayerList().getPlayers()) {
            serverPlayer.sendSystemMessage(message);
        }
    }
}
