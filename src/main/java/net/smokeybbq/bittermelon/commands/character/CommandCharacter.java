package net.smokeybbq.bittermelon.commands.character;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;

import java.util.List;
import java.util.Optional;

public class CommandCharacter {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("character")
                .then(Commands.literal("list")
                        .executes(context -> viewCharacters(context)))
                .then(Commands.literal("switch")
                        .then(Commands.argument("name", StringArgumentType.string())
                                .executes(context -> switchCharacter(context))))
                .then(Commands.literal("create")
                        .then(Commands.argument("name", StringArgumentType.string())
                                .then(Commands.argument("age", IntegerArgumentType.integer(1))
                                        .then(Commands.argument("emoteColor", StringArgumentType.string())
                                                .then(Commands.argument("description", StringArgumentType.greedyString())
                                                        .executes(context -> createCharacter(context))))))
                )
        );
    }

    private static int viewCharacters(CommandContext<CommandSourceStack> context) {
        List<Character> characters = CharacterManager.getInstance().getCharacters(context.getSource().getPlayer().getUUID());
        characters.forEach(character -> context.getSource().sendSystemMessage(Component.literal("Name: " + character.getName() + ", Age: " + character.getAge() + ", Description: " + character.getDescription())));
        return 1;
    }

    private static int createCharacter(CommandContext<CommandSourceStack> context) {
        String name = StringArgumentType.getString(context, "name");
        int age = IntegerArgumentType.getInteger(context, "age");
        String description = StringArgumentType.getString(context, "description");
        String emoteColor = StringArgumentType.getString(context, "emoteColor");

        Character character = new Character(context.getSource().getPlayer().getUUID(), name, "test", description, "test", age, 1.5, 80, emoteColor);
        CharacterManager.getInstance().addCharacter(character);
        context.getSource().sendSystemMessage(Component.literal("Character created: " + name));
        return 1;
    }

    private static int switchCharacter(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String characterName = StringArgumentType.getString(context, "name");
        ServerPlayer player = context.getSource().getPlayerOrException();

        List<Character> characters = CharacterManager.getInstance().getCharacters(player.getUUID());

        Optional<Character> selectedCharacter = characters.stream()
                .filter(c -> c.getName().equalsIgnoreCase(characterName))
                .findFirst();

        if (!selectedCharacter.isPresent()) {
            context.getSource().sendFailure(Component.literal("Character not found: " + characterName));
            return 0;
        }

        if (CharacterManager.getInstance().getActiveCharacter(player.getUUID()) != null) {
            Character currentCharacter = CharacterManager.getInstance().getActiveCharacter(player.getUUID());
            CompoundTag playerData = player.saveWithoutId(new CompoundTag());
            currentCharacter.savePlayerData(playerData);
        }

        if (selectedCharacter.get().getPlayerData() != null) {
            player.load(selectedCharacter.get().getPlayerData());
            player.connection.teleport(player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
            player.refreshDimensions();
            player.getInventory().setChanged();
            player.resetSentInfo();
        }

        CharacterManager.getInstance().setActiveCharacter(player, selectedCharacter.get());
        context.getSource().sendSystemMessage(Component.literal("Character switched: " + selectedCharacter.get().getName()));
        return 1;

    }
}