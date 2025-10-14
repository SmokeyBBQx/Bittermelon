package com.site21.bittermelon.content.chat;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.content.syncsound.SyncSoundEvent;
import com.site21.bittermelon.content.syncsound.SyncSoundType;
import com.site21.bittermelon.init.custom.VerbSets;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.ServerChatEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.ACTIVE_CHANNEL;
import static com.site21.bittermelon.init.neoforge.BitterMobEffects.ELECTROCUTED;
import static com.site21.bittermelon.init.neoforge.BitterMobEffects.TASERED;
import static java.lang.Character.isLetter;

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

        if (player.hasEffect(ELECTROCUTED) || player.hasEffect(TASERED)) {
            message = formatElectrocuted(message, player.getRandom());
        }

        event.setCanceled(true);

        if (character == null) {
            player.sendSystemMessage(Component.literal("You must select a valid character before sending a chat message.").withColor(0xFFFF0000));
            return;
        }

        int channel = player.getData(ACTIVE_CHANNEL.get());

        switch (channel) {
            case 0 -> sendRPMessage(character, player, message, NORMAL_RANGE, VerbSets.HUMAN.get());
            case 1 -> sendOOCMessage(player, character, message);
            case 2 -> sendLOOCMessage(player, character, message);
        }
    }

    public static void sendRPMessage(@NotNull Character character, ServerPlayer player, @NotNull String message, int range, String verb) {
        int emoteColor = character.getEmoteColor();
        MutableComponent messageComponent = Component.empty();

        // If the message does not contain any emotes, use the standard format
        if (!message.contains("*")) {
            messageComponent.append(Component.literal(character.getName() + " " + verb + ", ").withColor(emoteColor));
        } else {
            messageComponent.append(Component.literal("(" + character.getName() + ") ").withColor(emoteColor));
        }

        // Split message into emotes and dialogue
        Matcher matcher = EMOTE_PATTERN.matcher(message);
        while (matcher.find()) {
            // If group 1 is not null, it's an emote. Otherwise, it's dialogue
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

        // Broadcast sound event for speech
        if (!player.level().isClientSide) {
            NeoForge.EVENT_BUS.post(new SyncSoundEvent(player.level(), player.getOnPos(), SyncSoundType.SPEECH, messageComponent, range));
        }
    }

    public static void sendRPMessage(@NotNull Character character, ServerPlayer player, String message, int range, @NotNull VerbSet verbSet) {
        sendRPMessage(character, player, message, range, verbSet.getVerb(message));
    }

    public static void sendOOCMessage(@NotNull ServerPlayer player, @NotNull Character character, String message) {
        String nameFormat = "(OOC) (" + character.getName() + ") " + player.getName().getString() + ": ";
        Component messageComponent = Component.literal(nameFormat + message).withColor(DEFAULT_GRAY);
        sendMessage(messageComponent, player);
    }

    public static void sendLOOCMessage(@NotNull ServerPlayer player, @NotNull Character character, String message) {
        String nameFormat = "(LOOC) (" + character.getName() + ") " + player.getName().getString() + ": ";
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

    private static @NotNull String formatElectrocuted(@NotNull String text, RandomSource random) {
        StringBuilder result = new StringBuilder();

        // Randomly applies effects to letters in the text
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            if (isLetter(c)) {
                if (random.nextFloat() > 0.5f) {
                    result.append(applyElectrocutedEffect(c, random));
                } else {
                    result.append(c);
                }
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }

    @Contract(pure = true)
    private static @NotNull String applyElectrocutedEffect(char c, @NotNull RandomSource random) {
        int effect = random.nextIntBetweenInclusive(0, 2);

        switch (effect) {
            case 0 -> {
                // Repeat the character 1 to 4 times
                return String.valueOf(c).repeat(random.nextIntBetweenInclusive(1, 4));
            }
            case 1 -> {
                return c + "-";
            }
            default -> {
                return c + "--";
            }
        }
    }
}
