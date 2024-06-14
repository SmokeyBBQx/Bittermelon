package net.smokeybbq.bittermelon.events;

import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;
import net.smokeybbq.bittermelon.chat.Channel;
import net.smokeybbq.bittermelon.chat.ChannelManager;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.smokeybbq.bittermelon.util.LocalMessageHandler.compareDistance;

public class ChatEventHandler {

    @SubscribeEvent
    public void onChatMessage(ServerChatEvent event) {
        ServerPlayer player = event.getPlayer();
        Character activeCharacter = CharacterManager.getActiveCharacter(player);
        Channel currentChannel = ChannelManager.getPlayerActiveChannel(activeCharacter);
        String message = event.getMessage().getString();

        event.setCanceled(true);

        String emoteColor = activeCharacter.getEmoteColor();
        String chatColor = currentChannel.getChatColor();
        String channelColor = currentChannel.getChannelNameColor();

        String channelFormat = "[" + currentChannel.getName() + "] ";
        String nameFormat = activeCharacter.getName() + ":";

        MutableComponent messageComponent = Component.empty();
        messageComponent.append(Component.literal(channelFormat));
        messageComponent.append(Component.literal(nameFormat).setStyle(Style.EMPTY.withColor(TextColor.parseColor(channelColor))));

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

        for (Character character : currentChannel.getMembers()) {
            UUID memberUUID = character.getPlayerUUID();
            ServerPlayer p = player.server.getPlayerList().getPlayer(memberUUID);
            if (p != null && compareDistance(player, p) <= currentChannel.getRange()) {
                p.sendSystemMessage(messageComponent);
            }
        }
    }

}
