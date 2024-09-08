package net.smokeybbq.bittermelon.chat;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;
import net.smokeybbq.bittermelon.items.pda.PDA;
import net.smokeybbq.bittermelon.items.radio.RadioItem;
import net.smokeybbq.bittermelon.systems.telecommunications.EncodedMessage;
import net.smokeybbq.bittermelon.systems.telecommunications.TelecommsManager;

import java.util.Optional;
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

        // TODO: Radios/microphones recording RP chat nearby

        if (currentChannel instanceof RadioChannel) {
            ChannelManager.setCharacterActiveChannel(activeCharacter, ChannelManager.getInstance().getChannel("RP"));

            Optional<ItemStack> radioStackOpt = RadioItem.findRadioStackInInventory(player);
            if (radioStackOpt.isPresent()) {
                if (radioStackOpt.get().getItem() instanceof PDA digitalRadioItem) {

                }

            }
        }


        sendMessageToChannel(activeCharacter, player, currentChannel, message, 0);
    }

    private static void handleRadioChannel(ServerPlayer player, Channel channel, String message) {


    }

    private static void sendMessageToChannel(Character character, ServerPlayer player, Channel channel, String message, int rangeModifier) {
        String emoteColor = character.getEmoteColor();
        String chatColor = channel.getChatColor();
        String channelColor = channel.getChannelNameColor();
        int range = channel.getRange() + rangeModifier;

        String channelFormat = "[" + channel.getName() + "] ";
        String nameFormat = character.getName() + ":";

        MutableComponent messageComponent = Component.empty();
        messageComponent.append(Component.literal(channelFormat).setStyle(Style.EMPTY.withColor(TextColor.parseColor("#" + channelColor))));
        messageComponent.append(Component.literal(nameFormat).setStyle(Style.EMPTY.withColor(TextColor.parseColor("#" + channelColor)))); // TODO: review name colors

        // TODO: Different dialogue modules

        // Emote and Dialogue Detection
//        String regex = "\"([^\"]*)\"|([^\"\\s]+(\\s+[^\"\\s]+)*)"; For quotation marks
        String regex = "\\*(.*?)\\*|([^*]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                String emoteText = matcher.group(1);
                Component emote = Component.literal(emoteText).setStyle(Style.EMPTY.withColor(TextColor.parseColor("#" + emoteColor)));
                messageComponent.append(" ").append(emote);
            } else {
                String dialogueText = matcher.group(2).trim();
                Component dialogue = Component.literal(" \"" + dialogueText + "\"").setStyle(Style.EMPTY.withColor(TextColor.parseColor("#" + chatColor)));
                messageComponent.append(dialogue);
            }
        }

        for (Character c : channel.getMembers()) {
            UUID memberUUID = c.getEntityUUID();
            ServerPlayer p = player.server.getPlayerList().getPlayer(memberUUID);
            if (p != null) {
                if (player.distanceTo(p) <= range || channel.getProperty(ChannelProperty.IGNORE_RANGE)) {
                    // TODO: server level comparison requires testing
                    // TODO: Radio making sound nearby unless headset is worn
                    if (player.serverLevel().equals(p.serverLevel()) || channel.getProperty(ChannelProperty.IGNORE_DIMENSIONS)) {
                        p.sendSystemMessage(messageComponent);
                    }
                }
            }
        }

        // TODO: Whisper constant
        if (channel.getProperty(ChannelProperty.DOUBLE_SPEAK)) {
            sendMessageToChannel(character, player, ChannelManager.getInstance().getChannel("W"), message, 0);
        }
    }
}
