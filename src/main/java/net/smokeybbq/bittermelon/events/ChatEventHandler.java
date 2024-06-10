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

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        messageComponent.append(Component.literal(channelFormat).setStyle(Style.EMPTY.withColor((TextColor.parseColor(channelColor)))));
        messageComponent.append(Component.literal(nameFormat));

        // Emote and Dialogue Detection
        String regex = "\"([^\"]*)\"|([^\"\\s]+(\\s+[^\"\\s]+)*)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);

        // TODO: Review emotes
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

    public static double compareDistance(ServerPlayer player1, ServerPlayer player2) {
        double x = Math.abs(player1.getX() - player2.getX());
        double y = Math.abs(player1.getY() - player2.getY());
        double z = Math.abs(player1.getZ() - player2.getZ());
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }
}
