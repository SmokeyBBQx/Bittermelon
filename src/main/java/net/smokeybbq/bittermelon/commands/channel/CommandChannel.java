package net.smokeybbq.bittermelon.commands.channel;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;
import net.smokeybbq.bittermelon.chat.Channel;
import net.smokeybbq.bittermelon.chat.ChannelManager;
import net.smokeybbq.bittermelon.util.CommandUtil;

import java.util.List;
import java.util.Set;

public class CommandChannel {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("channel")
                .then(Commands.literal("switch")
                        .then(Commands.argument("channel", StringArgumentType.greedyString())
                                .executes(context -> switchChannel(context)))
                )
                .then(Commands.literal("join")
                        .then(Commands.argument("channel", StringArgumentType.greedyString())
                                .executes(context -> joinChannel(context)))
                )
                .then(Commands.literal("leave")
                        .then(Commands.argument("channel", StringArgumentType.greedyString())
                                .executes(context -> leaveChannel(context)))
                )
                .then(Commands.literal("add")
                        .then(Commands.argument("name", StringArgumentType.string())
                                .then(Commands.argument("range", IntegerArgumentType.integer(-1))
                                        .then(Commands.argument("chatColor", StringArgumentType.string())
                                                .then(Commands.argument("channelColor", StringArgumentType.string())
                                                        .executes(context -> createChannel(context))))))
                )
                .then(Commands.literal("remove")
                        .then(Commands.argument("channel", StringArgumentType.string())
                                .executes(context -> removeChannel(context)))
                )
                .then(Commands.literal("list")
                        .executes(context -> viewChannels(context, false))
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("character", StringArgumentType.string())
                                        .executes(context -> viewChannels(context, true))))
                )
                .then(Commands.literal("whitelist")
                        .then(Commands.literal("add")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .then(Commands.argument("character", StringArgumentType.string())
                                                .then(Commands.argument("channel", StringArgumentType.greedyString())
                                                        .executes(context -> whitelistAdd(context))))))
                        .then(Commands.literal("remove")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .then(Commands.argument("character", StringArgumentType.string())
                                                .then(Commands.argument("channel", StringArgumentType.greedyString())
                                                        .executes(context -> whitelistAdd(context))))))
                        .then(Commands.literal("list")
                                .then(Commands.argument("channel", StringArgumentType.greedyString())
                                        .executes(context -> whitelistList(context))))
                        .then(Commands.literal("clear")
                                .then(Commands.argument("channel", StringArgumentType.string())
                                        .executes(context -> whitelistClear(context))))
                )
                .then(Commands.argument("channel", StringArgumentType.greedyString())
                        .executes(context -> switchChannel(context)))
        );
    }

    private static int viewChannels(CommandContext<CommandSourceStack> context, boolean doesTargetPlayer) throws CommandSyntaxException {
        ServerPlayer player;
        Character character;
        if (doesTargetPlayer) {
            player = EntityArgument.getPlayer(context, "player");
            String characterName = StringArgumentType.getString(context, "character");
            character = CommandUtil.getCharacterIgnoreCase(player, characterName);
            if (character == null) {
                context.getSource().sendFailure(Component.literal("Character not found: " + characterName));
                return 0;
            }
        } else {
            player = context.getSource().getPlayerOrException();
            character = CharacterManager.getActiveCharacter(player.getUUID());
        }

        List<Channel> channels = ChannelManager.getInstance().getPermittedChannels(character);
        // this should never trigger, but just in case
        if (channels.isEmpty()) {
            context.getSource().sendSystemMessage(Component.literal(character.getName() + " has access to 0 channel(s): "));
            return 1;
        }

        String output = character.getName() + " has access to " + channels.size() + " channel(s): ";
        String channelType = "";
        for (Channel c : channels) {
            if (c.getMembers().contains(character)) {
                if (c.equals(ChannelManager.getCharacterActiveChannel(character))) {
                    channelType = " (Active)";
                } else {
                    channelType = " (Joined)";
                }
            }
            output += c.getName() + channelType + ", ";
        }
        output = output.substring(0, output.lastIndexOf(", "));
        context.getSource().sendSystemMessage(Component.literal(output));
        return 1;
    }

    private static int switchChannel(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String channelName = StringArgumentType.getString(context, "channel");
        Channel channel = CommandUtil.getChannelIgnoreCase(channelName);
        ServerPlayer player = context.getSource().getPlayerOrException();
        Character activeCharacter = CharacterManager.getActiveCharacter(player.getUUID());
        if (channel != null) {
            if (channel.getMembers().contains(activeCharacter)) {
                if (ChannelManager.getCharacterActiveChannel(activeCharacter) != channel) {
                    CommandUtil.writeChannelToPlayerData(channel, player);
                    ChannelManager.setCharacterActiveChannel(activeCharacter, channel);
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
        context.getSource().sendSystemMessage(Component.literal("Now speaking in: " + channel.getName()));
        return 1;
    }

    private static int joinChannel(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String channelName = StringArgumentType.getString(context, "channel");
        Channel channel = CommandUtil.getChannelIgnoreCase(channelName);
        Character activeCharacter = CharacterManager.getActiveCharacter(context.getSource().getPlayerOrException().getUUID());
        if (activeCharacter == null) {
            context.getSource().sendFailure(Component.literal("Switch to a character before managing channels"));
            return 0;
        }
        if (channel != null) {
            if (!ChannelManager.getInstance().isPermittedChannel(activeCharacter, channel)) {
                context.getSource().sendFailure(Component.literal("You are not permitted to join that channel"));
                return 0;
            }
            if (channel.getMembers().contains(activeCharacter)) {
                context.getSource().sendSystemMessage(Component.literal("Already joined channel: " + channel.getName()));
                return 1;
            } else {
                channel.addMember(activeCharacter);
            }
        } else {
            context.getSource().sendFailure(Component.literal("Channel not found: " + channelName));
            return 0;
        }
        context.getSource().sendSystemMessage(Component.literal("Channel joined: " + channel.getName()));
        return 1;
    }

    private static int leaveChannel(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String channelName = StringArgumentType.getString(context, "channel");
        Channel channel = CommandUtil.getChannelIgnoreCase(channelName);
        Character activeCharacter = CharacterManager.getActiveCharacter(context.getSource().getPlayerOrException().getUUID());
        if (activeCharacter == null) {
            context.getSource().sendFailure(Component.literal("Switch to a character before managing channels"));
            return 0;
        }
        if (channel != null) {
            if (!channel.getProperty("canLeave")) {
                context.getSource().sendFailure(Component.literal("You are not permitted to leave that channel"));
                return 0;
            }
            if (!channel.getMembers().contains(activeCharacter)) {
                context.getSource().sendSystemMessage(Component.literal("Not joined channel: " + channel.getName()));
                return 1;
            } else {
                if (channel.equals(ChannelManager.getCharacterActiveChannel(activeCharacter))) {
                    context.getSource().sendFailure(Component.literal("You cannot leave an active channel"));
                    return 0;
                }
                channel.removeMember(activeCharacter);
            }
        } else {
            context.getSource().sendFailure(Component.literal("Channel not found: " + channelName));
            return 0;
        }
        context.getSource().sendSystemMessage(Component.literal("Channel left: " + channel.getName()));
        return 1;
    }

    private static int createChannel(CommandContext<CommandSourceStack> context) {
        String name = StringArgumentType.getString(context, "name");
        int range = IntegerArgumentType.getInteger(context, "range");
        String chatColor = StringArgumentType.getString(context, "chatColor");
        String channelNameColor = StringArgumentType.getString(context, "channelColor");
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

    private static int whitelistAdd(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        String channelName = StringArgumentType.getString(context, "channel");
        String characterName = StringArgumentType.getString(context, "character");
        Channel channel = CommandUtil.getChannelIgnoreCase(channelName);
        Character character = CommandUtil.getCharacterIgnoreCase(player, characterName);
        if (channel != null) {
            if (channel.getProperty("ignoreWhitelist")) {
                context.getSource().sendFailure(Component.literal(channel.getName() + " does not use a whitelist"));
                return 0;
            }
            if (character == null) {
                context.getSource().sendFailure(Component.literal("Character not found: " + characterName));
                return 0;
            }

            Set<Character> whitelist = channel.getWhitelist();
            if (whitelist.contains(character)) {
                context.getSource().sendSystemMessage(Component.literal("Character already whitelisted: " + character.getName()));
                return 1;
            }
            channel.addToWhitelist(character);
        } else {
            context.getSource().sendFailure(Component.literal("Channel not found: " + channelName));
            return 0;
        }
        context.getSource().sendSystemMessage(Component.literal("Character successfully added to whitelist"));
        return 1;
    }

    private static int whitelistRemove(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        String channelName = StringArgumentType.getString(context, "channel");
        String characterName = StringArgumentType.getString(context, "character");
        Channel channel = CommandUtil.getChannelIgnoreCase(channelName);
        Character character = CommandUtil.getCharacterIgnoreCase(player, characterName);
        if (channel != null) {
            if (channel.getProperty("ignoreWhitelist")) {
                context.getSource().sendFailure(Component.literal(channel.getName() + " does not use a whitelist"));
                return 0;
            }
            if (character == null) {
                context.getSource().sendFailure(Component.literal("Character not found: " + characterName));
                return 0;
            }

            Set<Character> whitelist = channel.getWhitelist();
            if (!whitelist.contains(character)) {
                context.getSource().sendSystemMessage(Component.literal("Character is not whitelisted: " + character.getName()));
                return 1;
            }
            channel.removeFromWhitelist(character);
        } else {
            context.getSource().sendFailure(Component.literal("Channel not found: " + channelName));
            return 0;
        }
        context.getSource().sendSystemMessage(Component.literal("Character successfully removed from whitelist"));
        return 1;
    }

    private static int whitelistClear(CommandContext<CommandSourceStack> context) {
        String channelName = StringArgumentType.getString(context, "channel");
        Channel channel = CommandUtil.getChannelIgnoreCase(channelName);
        if (channel != null) {
            if (channel.getProperty("ignoreWhitelist")) {
                context.getSource().sendFailure(Component.literal(channel.getName() + " does not use a whitelist"));
                return 0;
            }

            Set<Character> whitelist = channel.getWhitelist();

            if (whitelist.isEmpty()) {
                context.getSource().sendSystemMessage(Component.literal("Whitelist for '" + channel.getName() + "' is empty"));
                return 1;
            }

            for (Character c : whitelist) {
                channel.removeFromWhitelist(c);
            }
            context.getSource().sendSystemMessage(Component.literal("Whitelist for '" + channel.getName() + "' has been cleared"));
            return 1;
        } else {
            context.getSource().sendFailure(Component.literal("Channel not found: " + channelName));
            return 0;
        }
    }

    private static int whitelistList(CommandContext<CommandSourceStack> context) {
        String channelName = StringArgumentType.getString(context, "channel");
        Channel channel = CommandUtil.getChannelIgnoreCase(channelName);
        if (channel != null) {
            if (channel.getProperty("ignoreWhitelist")) {
                context.getSource().sendFailure(Component.literal(channel.getName() + " does not use a whitelist"));
                return 0;
            }

            Set<Character> whitelist = channel.getWhitelist();

            if (whitelist.isEmpty()) {
                context.getSource().sendSystemMessage(Component.literal("Whitelist for '" + channel.getName() + "' is empty"));
                return 1;
            }

            String result = "";
            int count = 0;
            for (Character c : whitelist) {
                result += c.getName() + " (" + CommandUtil.keyToServerPlayer(c.getPlayerUUID()).getName().getString() + ")"  + ", ";
                count++;
            }
            result = result.substring(0, result.lastIndexOf(", "));
            context.getSource().sendSystemMessage(Component.literal(channel.getName() + " has " + count + " whitelisted character(s): ").append(result));
            return 1;
        } else {
            context.getSource().sendFailure(Component.literal("Channel not found: " + channelName));
            return 0;
        }
    }
}
