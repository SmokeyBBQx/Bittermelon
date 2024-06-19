package net.smokeybbq.bittermelon.events;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;
import net.smokeybbq.bittermelon.chat.Channel;
import net.smokeybbq.bittermelon.chat.ChannelManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatEventHandler {

    @SubscribeEvent
    public void onChatMessage(ServerChatEvent event) {
        ServerPlayer player = event.getPlayer();
        Character activeCharacter = CharacterManager.getActiveCharacter(player.getUUID());
        Channel currentChannel = ChannelManager.getCharacterActiveChannel(activeCharacter);
        String message = event.getMessage().getString();

        event.setCanceled(true);

        if (activeCharacter == null || currentChannel == null) {
            player.sendSystemMessage(Component.literal("Join a channel before sending a chat message"));
            return;
        }

        List<UUID> seenMessage = new ArrayList<>();
        sendMessageToChannel(player, currentChannel, message, seenMessage, true);
    }

    private static void sendMessageToChannel(ServerPlayer player, Channel channel, String message, List<UUID> playersSeen, boolean isOriginalChannel) {
        Character character = CharacterManager.getActiveCharacter(player.getUUID());
        String emoteColor = character.getEmoteColor();
        String chatColor = channel.getChatColor();
        String channelColor = channel.getChannelNameColor();

        String channelFormat = "[" + channel.getName() + "] ";
        String nameFormat = character.getName() + ":";

        MutableComponent messageComponent = Component.empty();
        messageComponent.append(Component.literal(channelFormat).setStyle(Style.EMPTY.withColor(TextColor.parseColor(channelColor))));
        messageComponent.append(Component.literal(nameFormat).setStyle(Style.EMPTY.withColor(TextColor.parseColor(channelColor)))); // TODO: review name colors

        // Emote and Dialogue Detection
        String regex = "\"([^\"]*)\"|([^\"\\s]+(\\s+[^\"\\s]+)*)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                String quotedText = matcher.group(1);
                Component dialogue = Component.literal(" \"" + quotedText + "\"").setStyle(Style.EMPTY.withColor(TextColor.parseColor(chatColor)));
                messageComponent.append(dialogue);
            } else {
                String nonQuotedText = matcher.group(2);
                Component emote = Component.literal(nonQuotedText).setStyle(Style.EMPTY.withColor(TextColor.parseColor(emoteColor)));
                messageComponent.append(" ").append(emote);
            }
        }

        for (Character c : channel.getMembers()) {
            UUID memberUUID = c.getPlayerUUID();
            ServerPlayer p = player.server.getPlayerList().getPlayer(memberUUID);
            if (p != null && !playersSeen.contains(memberUUID)) {
                if (player.distanceTo(p) <= channel.getRange() || channel.getProperty("ignoreRange")) {
                    // TODO: server level comparison requires testing
                    if (player.serverLevel().equals(p.serverLevel()) || channel.getProperty("ignoreDimensions")) {
                        playersSeen.add(memberUUID);
                        p.sendSystemMessage(messageComponent);
                    }
                }
            }
        }

        // to prevent infinite recursion, only subchannels of the original channel will also see the message
        if (isOriginalChannel) {
            for (Channel layer : channel.getLayeredChannels()) {
                sendMessageToChannel(player, layer, message, playersSeen, false);
            }
        }
    }
}
