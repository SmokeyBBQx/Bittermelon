package net.smokeybbq.bittermelon.commands.channel;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;
import net.smokeybbq.bittermelon.chat.Channel;
import net.smokeybbq.bittermelon.chat.ChannelManager;

import java.util.UUID;

public class CommandJoinChannel {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("joinchannel")
                .then(Commands.argument("channelName", StringArgumentType.word()).executes(context -> joinChannel(context))));
    }

    private static int joinChannel(CommandContext<CommandSourceStack> context) {
        String channelName = StringArgumentType.getString(context, "channelName");
        try {
            Character activeCharacter = CharacterManager.getInstance().getActiveCharacter(context.getSource().getPlayer().getUUID());
            Channel channel = ChannelManager.getInstance().getChannel(channelName);

            channel.addMember(activeCharacter);
        } catch (IllegalArgumentException e) {
        }
        context.getSource().sendSystemMessage(Component.literal("Channel joined: " + channelName));
        return 1;
    }
}
