package net.smokeybbq.bittermelon.commands.channel;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;
import net.smokeybbq.bittermelon.chat.Channel;
import net.smokeybbq.bittermelon.chat.ChannelManager;

public class CommandCreateChannel {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("createchannel")
                .then(Commands.argument("name", StringArgumentType.string())
                        .then(Commands.argument("range", IntegerArgumentType.integer(-1))
                            .then(Commands.argument("chatColor", StringArgumentType.string())
                                .then(Commands.argument("channelNameColor", StringArgumentType.string())
                                    .executes(context -> createCharacter(context)))
        ))));
    }

    private static int createCharacter(CommandContext<CommandSourceStack> context) {
        String name = StringArgumentType.getString(context, "name");
        int range = IntegerArgumentType.getInteger(context, "range");
        String chatColor = "#" + StringArgumentType.getString(context, "chatColor");
        String channelNameColor = "#" + StringArgumentType.getString(context, "channelNameColor");

        Channel channel = new Channel(name, range, chatColor, channelNameColor);
        ChannelManager.getInstance().addChannel(name, channel);
        context.getSource().sendSystemMessage(Component.literal("Channel created: " + name + "(Talk Range: " + range + " Chat Color: " + chatColor + " Channel Name Color: " + channelNameColor + ")"));
        return 1;
    }
}