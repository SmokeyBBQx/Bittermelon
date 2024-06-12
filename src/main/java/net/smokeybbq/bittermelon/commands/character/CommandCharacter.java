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
import net.smokeybbq.bittermelon.chat.Channel;
import net.smokeybbq.bittermelon.chat.ChannelManager;

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

    // does this need a try/catch block?
    private static int createCharacter(CommandContext<CommandSourceStack> context) {
        String name = StringArgumentType.getString(context, "name");
        int age = IntegerArgumentType.getInteger(context, "age");
        String description = StringArgumentType.getString(context, "description");
        String emoteColor = StringArgumentType.getString(context, "emoteColor");
        Character character = getCharacter(context, name);

        if (character != null) {
            context.getSource().sendFailure(Component.literal("Character '" + character.getName() + "' already exists"));
            return 0;
        } else {
            character = new Character(context.getSource().getPlayer().getUUID(), name, "test", description, "test", age, 1.5, 80, emoteColor);
            CharacterManager.getInstance().addCharacter(character);
        }

        context.getSource().sendSystemMessage(Component.literal("Character created: " + name));
        return 1;
    }
    // TODO: figure out default data for characters
    private static int switchCharacter(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String characterName = StringArgumentType.getString(context, "name");
        ServerPlayer player = context.getSource().getPlayerOrException();
        Character selectedCharacter = getCharacter(context, characterName);
        Character activeCharacter = CharacterManager.getInstance().getActiveCharacter(player.getUUID());

        if (selectedCharacter == null) {
            context.getSource().sendFailure(Component.literal("Character not found: " + characterName));
            return 0;
        }

        CompoundTag persistentData = player.getPersistentData();
        if (activeCharacter != null) {
            // enforces and validates activeCharacterUUID in persistentData
            if (!persistentData.contains("bittermelon:activeCharacterUUID")) {
                persistentData.putUUID("bittermelon:activeCharacterUUID", activeCharacter.getUUID());
            } else if (!persistentData.getUUID("bittermelon:activeCharacterUUID").equals(activeCharacter.getUUID())) {
                persistentData.remove("bittermelon:activeCharacterUUID");
                persistentData.putUUID("bittermelon:activeCharacterUUID", activeCharacter.getUUID());
            }

            CompoundTag playerData = player.saveWithoutId(new CompoundTag());
            activeCharacter.savePlayerData(playerData);
        }

        CompoundTag newPlayerData = selectedCharacter.getPlayerData();
        if (newPlayerData != null) {
            player.load(newPlayerData);
            player.connection.teleport(player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
            player.refreshDimensions();
            player.getInventory().setChanged();
            player.resetSentInfo();
            // retrieve and set player channel
            persistentData = player.getPersistentData(); // refresh stored data
            if (persistentData.contains("bittermelon:activeChannel")) {
                Channel channel = ChannelManager.getInstance().getChannel(persistentData.getString("bittermelon:activeChannel"));
                if (channel != null) {
                    ChannelManager.setPlayerActiveChannel(selectedCharacter, channel);
                }
            }
        } else {
            // implement default playerData
        }

        CharacterManager.getInstance().setActiveCharacter(player, selectedCharacter);
        context.getSource().sendSystemMessage(Component.literal("Character switched: " + selectedCharacter.getName()));
        return 1;

    }

    // get player characters, NCS
    private static Character getCharacter(CommandContext<CommandSourceStack> context, String characterName) {
        List<Character> characters = CharacterManager.getInstance().getCharacters(context.getSource().getPlayer().getUUID());

        Optional<Character> selectedCharacter = characters.stream()
                .filter(c -> c.getName().equalsIgnoreCase(characterName))
                .findFirst();

        if (!selectedCharacter.isPresent()) {
            return null;
        }
        return selectedCharacter.get();
    }
}
