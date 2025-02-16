package com.site21.bittermelon.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.content.medical.factory.Anatomy;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class CharacterCommand {
    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("character")
                .then(Commands.literal("switch")
                        .then(Commands.argument("name", StringArgumentType.greedyString())
                                .executes(context -> switchCharacter(context.getSource(), StringArgumentType.getString(context, "name")))))
                .then(Commands.literal("create")
                        .then(Commands.argument("name", StringArgumentType.greedyString())
                                .executes(context -> createCharacter(context.getSource(), StringArgumentType.getString(context, "name")))))
                .then(Commands.literal("list")
                        .executes(CharacterCommand::listCharacters))
                .then(Commands.literal("info")
                        .executes(context -> showCharacterInfo(context.getSource()))));
    }

    private static int switchCharacter(@NotNull CommandSourceStack source, String name) {
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("Must be run by a player"));
            return 0;
        }

        CharacterManager manager = CharacterManager.get(source.getServer());

        Optional<Character> targetCharacter = manager.getCharactersByEntityUUID(player.getUUID()).stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst();

        if (targetCharacter.isEmpty()) {
            source.sendFailure(Component.literal("No character found with name: " + name));
            return 0;
        }

        if (targetCharacter.get().equals(manager.getActiveCharacter(player))){
            source.sendFailure(Component.literal("Already switched to character: " + name));
            return 0;
        }

        manager.setActiveCharacter(player, targetCharacter.get().getUUID());
        source.sendSuccess(() -> Component.literal("Switched to character: " + name), true);
        return 1;
    }

    private static int createCharacter(@NotNull CommandSourceStack source, String name) {
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("Must be run by a player"));
            return 0;
        }

        CharacterManager manager = CharacterManager.get(source.getServer());
        Character character = new Character(player.getUUID(), name, Anatomy.HUMAN);
        manager.addCharacter(character);
        manager.setActiveCharacter(player, character.getUUID());

        source.sendSuccess(() -> Component.literal("Created and switched to character: " + name), true);
        return 1;
    }

    private static int listCharacters(@NotNull CommandContext<CommandSourceStack> context) {
        CommandSourceStack source = context.getSource();
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("Must be run by a player"));
            return 0;
        }

        CharacterManager manager = CharacterManager.get(source.getServer());
        Character activeCharacter = manager.getActiveCharacter(player);

        List<Character> playerCharacters = manager.getCharactersByEntityUUID(player.getUUID());

        MutableComponent message = Component.literal("Your characters:\n");
        for (Character character : playerCharacters) {
            boolean isActive = activeCharacter != null && character.getUUID().equals(activeCharacter.getUUID());
            message.append(Component.literal(
                            (isActive ? "→ " : "  ") + character.getName() + "\n")
                    .withStyle(isActive ? ChatFormatting.GREEN : ChatFormatting.GRAY));
        }

        source.sendSuccess(() -> message, false);
        return 1;
    }

    private static int showCharacterInfo(@NotNull CommandSourceStack source) {
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("Must be run by a player"));
            return 0;
        }

        CharacterManager manager = CharacterManager.get(source.getServer());
        Character character = manager.getActiveCharacter(player);

        if (character == null) {
            source.sendFailure(Component.literal("No active character"));
            return 0;
        }

        MutableComponent message = Component.literal("Character Info:\n")
                .append("Name: " + character.getName() + "\n")
                .append("Description: " + character.getDescription() + "\n");

        source.sendSuccess(() -> message, false);
        return 1;
    }
}
