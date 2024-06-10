package net.smokeybbq.bittermelon.commands.channel;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.arguments.TimeArgument;
import net.minecraft.network.chat.Component;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;
import net.smokeybbq.bittermelon.chat.Channel;
import net.smokeybbq.bittermelon.chat.ChannelManager;

import java.util.UUID;

public class CommandChannel {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("channel")
                .then(Commands.literal("switch")
                        .then(Commands.argument("channel", StringArgumentType.word())
                                .executes(context -> switchChannel(context))))
                .then(Commands.literal("join")
                        .then(Commands.argument("channel", StringArgumentType.word())
                                .executes(context -> joinChannel(context))))
                .then(Commands.literal("add")
                        .then(Commands.argument("name", StringArgumentType.string())
                                .then(Commands.argument("range", IntegerArgumentType.integer(-1))
                                        .then(Commands.argument("chatColor", StringArgumentType.string())
                                                .then(Commands.argument("nameColor", StringArgumentType.string())
                                                        .executes(context -> createChannel(context))))))
                )
                .then(Commands.argument("channel", StringArgumentType.word())
                        .executes(context -> switchChannel(context)))
        );
    }

    private static int switchChannel(CommandContext<CommandSourceStack> context) {
        String channelName = StringArgumentType.getString(context, "channel");
        try {
            Character activeCharacter = CharacterManager.getInstance().getActiveCharacter(context.getSource().getPlayer().getUUID());
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

    private static int joinChannel(CommandContext<CommandSourceStack> context) {
        String channelName = StringArgumentType.getString(context, "channel");
        try {
            Character activeCharacter = CharacterManager.getInstance().getActiveCharacter(context.getSource().getPlayer().getUUID());
            Channel channel = ChannelManager.getInstance().getChannel(channelName);

            channel.addMember(activeCharacter);
        } catch (IllegalArgumentException e) {
        }
        context.getSource().sendSystemMessage(Component.literal("Channel joined: " + channelName));
        return 1;
    }

    private static int createChannel(CommandContext<CommandSourceStack> context) {
        String name = StringArgumentType.getString(context, "name");
        int range = IntegerArgumentType.getInteger(context, "range");
        String chatColor = StringArgumentType.getString(context, "chatColor");
        String channelNameColor = StringArgumentType.getString(context, "nameColor");

        Channel channel = new Channel(name, range, chatColor, channelNameColor);
        ChannelManager.getInstance().addChannel(name, channel);
        context.getSource().sendSystemMessage(Component.literal("Channel created: " + name + "(Talk Range: " + range + " Chat Color: " + chatColor + " Channel Name Color: " + channelNameColor + ")"));
        return 1;
    }
}
