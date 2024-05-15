package net.smokeybbq.bittermelon.commands.character;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.smokeybbq.bittermelon.character.Character;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.smokeybbq.bittermelon.character.CharacterManager;

public class CommandCreateCharacter {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("createcharacter")
                .then(Commands.argument("name", StringArgumentType.string())
                        .then(Commands.argument("age", IntegerArgumentType.integer(1))
                                .then(Commands.argument("emoteColor", StringArgumentType.string())
                                        .then(Commands.argument("description", StringArgumentType.greedyString())
                                                .executes(context -> createCharacter(context))))))
        );
    }

    private static int createCharacter(CommandContext<CommandSourceStack> context) {
        String name = StringArgumentType.getString(context, "name");
        int age = IntegerArgumentType.getInteger(context, "age");
        String description = StringArgumentType.getString(context, "description");
        String emoteColor = "#" + StringArgumentType.getString(context, "emoteColor");

        Character character = new Character(context.getSource().getPlayer().getUUID(), name, "test", description, "test", age, 1.5, emoteColor);
        CharacterManager.getInstance().addCharacter(context.getSource().getPlayer().getUUID(), character);
        return 1;
    }
}
