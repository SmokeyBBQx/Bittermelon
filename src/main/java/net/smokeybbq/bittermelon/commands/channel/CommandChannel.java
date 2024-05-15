package net.smokeybbq.bittermelon.commands.channel;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.network.chat.Component;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;
import net.smokeybbq.bittermelon.chat.ChannelManager;

import java.util.UUID;

public class CommandChannel {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("channel")
                .then(Commands.argument("channelName", StringArgumentType.word()).executes(context -> switchChannel(context))));
    }

    private static int switchChannel(CommandContext<CommandSourceStack> context) {
        String channelName = StringArgumentType.getString(context, "channelName");
        try {
            Character activeCharacter = CharacterManager.getInstance().getActiveCharacter(context.getSource().getPlayer());
            if (ChannelManager.getInstance().getChannels().containsKey(channelName)) {
                if (ChannelManager.getInstance().getChannel(channelName).getMembers().contains(activeCharacter)) {
                    ChannelManager.getInstance().setPlayerActiveChannel(activeCharacter, ChannelManager.getInstance().getChannel(channelName));
                } else {
                    context.getSource().sendFailure(Component.literal("You have not joined: " + channelName));
                    return 0;
                }
            } else {
                context.getSource().sendFailure(Component.literal("Channel not found: " + channelName));
                return 0;
            }
        } catch (IllegalArgumentException e) {
        }
        context.getSource().sendSystemMessage(Component.literal("Channel switched to: " + channelName));
        return 1;
    }
}
