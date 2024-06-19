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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;
import net.smokeybbq.bittermelon.chat.Channel;
import net.smokeybbq.bittermelon.chat.ChannelManager;
import net.smokeybbq.bittermelon.util.CommandUtil;

import java.util.List;

public class CommandCharacter {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("character")
                .then(Commands.literal("list")
                        .executes(context -> viewCharacters(context, false))
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(context -> viewCharacters(context, true)))
                )
                .then(Commands.literal("switch")
                        .then(Commands.argument("name", StringArgumentType.string())
                                .executes(context -> switchCharacter(context)))
                )
                .then(Commands.literal("create")
                        .then(Commands.argument("name", StringArgumentType.string())
                                .then(Commands.argument("age", IntegerArgumentType.integer(1))
                                        .then(Commands.argument("emoteColor", StringArgumentType.string())
                                                .then(Commands.argument("description", StringArgumentType.greedyString())
                                                        .executes(context -> createCharacter(context))))))
                )
                .then(Commands.literal("remove")
                        .then(Commands.argument("character", StringArgumentType.string())
                                .executes(context -> removeCharacter(context, false))
                                .then(Commands.argument("target", EntityArgument.player())
                                        .executes(context -> removeCharacter(context, true))))
                )
        );
        dispatcher.register(Commands.literal("switchcharacter")
                .then(Commands.argument("name", StringArgumentType.string())
                        .executes(context -> switchCharacter(context))));
    }

    private static int viewCharacters(CommandContext<CommandSourceStack> context, boolean doesTargetPlayer) throws CommandSyntaxException {
        ServerPlayer player;
        if (doesTargetPlayer) {
            player = EntityArgument.getPlayer(context, "target");
        } else {
            player = context.getSource().getPlayerOrException();
        }

        List<Character> characters = CharacterManager.getInstance().getCharacters(player.getUUID());
        characters.forEach(character -> context.getSource().sendSystemMessage(Component.literal("Name: " + character.getName() + ", Age: " + character.getAge() + ", Description: " + character.getDescription())));
        return 1;
    }

    // does this need a try/catch block?
    private static int createCharacter(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "name");
        int age = IntegerArgumentType.getInteger(context, "age");
        String description = StringArgumentType.getString(context, "description");
        String emoteColor = StringArgumentType.getString(context, "emoteColor");
        Character character = CommandUtil.getCharacterIgnoreCase(context.getSource().getPlayerOrException(), name);

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
        Character selectedCharacter = CommandUtil.getCharacterIgnoreCase(player, characterName);
        Character activeCharacter = CharacterManager.getActiveCharacter(player.getUUID());

        if (selectedCharacter == null) {
            context.getSource().sendFailure(Component.literal("Character not found: " + characterName));
            return 0;
        }
        if (selectedCharacter == activeCharacter) {
            context.getSource().sendSystemMessage(Component.literal("Character already active: " + characterName));
            return 1; // someone please tell me if these return 0 or 1
        }

        // ensures that if there is an active character it is stored in persistent data before saving
        if (CommandUtil.validateStoredCharacterUUID(player) == 1) {
            CommandUtil.setActiveLevel(player);
            CompoundTag playerData = player.saveWithoutId(new CompoundTag());
            activeCharacter.savePlayerData(playerData);
        }

        CompoundTag newPlayerData = selectedCharacter.getPlayerData();
        if (newPlayerData != null) {
            player.load(newPlayerData);

            // Sends player to their stored position, if the dimension stored is valid
            ServerLevel newLevel = CommandUtil.getActiveLevel(player);
            if (newLevel != null) {
                player.teleportTo(newLevel, player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
            } else {
                // implement send to spawn
            }

//            player.refreshDimensions(); // doesn't appear to do anything
            player.getInventory().setChanged();
            player.resetSentInfo();
        } else {
            // implement default playerData
        }

        CharacterManager.getInstance().setActiveCharacter(player, selectedCharacter);
        context.getSource().sendSystemMessage(Component.literal("Character switched: " + selectedCharacter.getName()));
        // retrieve and set player channel
        Channel channel = CommandUtil.getActiveChannelFromData(player);
        if (channel != null) {
            if (ChannelManager.getCharacterActiveChannel(selectedCharacter) != channel) {
                CommandUtil.executePlayerCommand(player, "channel switch " + channel.getName());
            } else {
                // manually sends message for clarity even when command is not called
                context.getSource().sendSystemMessage(Component.literal("Now speaking in: " + channel.getName()));
            }
        }
        return 1;

    }

    private static int removeCharacter(CommandContext<CommandSourceStack> context, boolean doesTargetPlayer) throws CommandSyntaxException {
        String characterName = StringArgumentType.getString(context, "character");
        ServerPlayer player;
        if (doesTargetPlayer) {
            player = EntityArgument.getPlayer(context, "target");
        } else {
            player = context.getSource().getPlayerOrException();
        }

        Character selectedCharacter = CommandUtil.getCharacterIgnoreCase(player, characterName);
        Character activeCharacter = CharacterManager.getActiveCharacter(player.getUUID());

        if (selectedCharacter == null) {
            context.getSource().sendFailure(Component.literal("Character not found: " + characterName));
            return 0;
        }
        // replace this return case once there is a way to reset playerData
        if (selectedCharacter == activeCharacter) {
            context.getSource().sendFailure(Component.literal("You cannot delete an active character"));
            return 0;
        }

        CharacterManager.getInstance().removeCharacter(selectedCharacter);
        context.getSource().sendSystemMessage(Component.literal("Character removed: " + characterName));
        return 1;
    }
}
