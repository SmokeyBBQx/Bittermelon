package net.smokeybbq.bittermelon.commands.channel;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;
import net.smokeybbq.bittermelon.chat.Channel;
import net.smokeybbq.bittermelon.chat.ChannelManager;
import net.smokeybbq.bittermelon.util.CommandUtil;

public class CommandChannel {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("channel")
                .then(Commands.literal("switch")
                        .then(Commands.argument("channel", StringArgumentType.word())
                                .executes(context -> switchChannel(context)))
                )
                .then(Commands.literal("join")
                        .then(Commands.argument("channel", StringArgumentType.word())
                                .executes(context -> joinChannel(context)))
                )
                .then(Commands.literal("add")
                        .then(Commands.argument("name", StringArgumentType.string())
                                .then(Commands.argument("range", IntegerArgumentType.integer(-1))
                                        .then(Commands.argument("chatColor", StringArgumentType.string())
                                                .then(Commands.argument("nameColor", StringArgumentType.string())
                                                        .executes(context -> createChannel(context))))))
                )
                .then(Commands.literal("remove")
                        .then(Commands.argument("channel", StringArgumentType.string())
                                .executes(context -> removeChannel(context)))
                )
                .then(Commands.argument("channel", StringArgumentType.word())
                        .executes(context -> switchChannel(context)))
        );
    }

    private static int switchChannel(CommandContext<CommandSourceStack> context) {
        String channelName = StringArgumentType.getString(context, "channel");
        Channel channel = CommandUtil.getChannelIgnoreCase(channelName);
        ServerPlayer player = context.getSource().getPlayer();
        try {
            Character activeCharacter = CharacterManager.getInstance().getActiveCharacter(player.getUUID());
            if (channel != null) {
                if (channel.getMembers().contains(activeCharacter)) {
                    if (ChannelManager.getCharacterActiveChannel(activeCharacter) != channel) {
                        CommandUtil.writeChannelToPlayerData(channel, player);
                        ChannelManager.getInstance().setCharacterActiveChannel(activeCharacter, channel);
                    } else {
                        context.getSource().sendSystemMessage(Component.literal("Already speaking in: " + channel.getName()));
                        return 1;
                    }
                } else {
                    context.getSource().sendFailure(Component.literal("You have not joined: " + channel.getName()));
                    return 0;
                }
            } else {
                context.getSource().sendFailure(Component.literal("Channel not found: " + channelName));
                return 0;
            }
        } catch (IllegalArgumentException e) {
        }
        context.getSource().sendSystemMessage(Component.literal("Now speaking in: " + channel.getName()));
        return 1;
    }

    private static int joinChannel(CommandContext<CommandSourceStack> context) {
        String channelName = StringArgumentType.getString(context, "channel");
        Channel channel = CommandUtil.getChannelIgnoreCase(channelName);
        try {
            Character activeCharacter = CharacterManager.getInstance().getActiveCharacter(context.getSource().getPlayer().getUUID());
            if (activeCharacter == null) {
                context.getSource().sendFailure(Component.literal("Switch to a character before joining a channel"));
                return 0;
            }
            if (channel != null) {
                if (channel.getMembers().contains(activeCharacter)) {
                    context.getSource().sendSystemMessage(Component.literal("Already joined channel: " + channel.getName()));
                    return 1; // is this supposed to return 0 or 1?
                } else {
                    channel.addMember(activeCharacter);
                }
            } else {
                context.getSource().sendFailure(Component.literal("Channel not found: " + channelName));
                return 0;
            }
        } catch (IllegalArgumentException e) {
        }
        context.getSource().sendSystemMessage(Component.literal("Channel joined: " + channel.getName()));
        return 1;
    }

    private static int createChannel(CommandContext<CommandSourceStack> context) {
        String name = StringArgumentType.getString(context, "name");
        int range = IntegerArgumentType.getInteger(context, "range");
        String chatColor = StringArgumentType.getString(context, "chatColor");
        String channelNameColor = StringArgumentType.getString(context, "nameColor");
        Channel channel = CommandUtil.getChannelIgnoreCase(name);

        if (channel != null) {
            context.getSource().sendFailure(Component.literal("Channel '" + channel.getName() + "' already exists"));
            return 0;
        }
        Channel newChannel = new Channel(name, range, chatColor, channelNameColor);
        ChannelManager.getInstance().addChannel(name, newChannel);
        context.getSource().sendSystemMessage(Component.literal("Channel created: " + name + " (Talk Range: " + range + ", Chat Color: " + chatColor + ", Channel Name Color: " + channelNameColor + ")"));
        return 1;
    }

    private static int removeChannel(CommandContext<CommandSourceStack> context) {
        String channelName = StringArgumentType.getString(context, "channel");
        Channel channel = CommandUtil.getChannelIgnoreCase(channelName);
        if (channel != null) {
            ChannelManager.getInstance().removeChannel(channel);
        } else {
            context.getSource().sendFailure(Component.literal("Channel not found: " + channelName));
            return 0;
        }
        context.getSource().sendSystemMessage(Component.literal("Channel removed: " + channel.getName()));
        return 1;
    }
}
