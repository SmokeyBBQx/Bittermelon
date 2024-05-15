package net.smokeybbq.bittermelon.commands.character;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.server.level.ServerPlayer;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;

import java.util.List;

public class CommandDisplayCharacter {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("viewcharacters")
                .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> viewCharacters(context,EntityArgument.getPlayer(context,"player"))))
        );
    }

    private static int viewCharacters(CommandContext<CommandSourceStack> context, ServerPlayer player) {
        List<Character> characters = CharacterManager.getInstance().getCharacters(player.getUUID());
            characters.forEach(character -> context.getSource().sendSystemMessage(Component.literal("Name: " + character.getName() + ", Age: " + character.getAge() + ", Description: " + character.getDescription())));
        return 1;
    }
}
