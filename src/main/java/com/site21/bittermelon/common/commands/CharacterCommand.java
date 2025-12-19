package com.site21.bittermelon.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.medical.factory.AnatomyType;
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
                .then(Commands.literal("color")
                        .then(Commands.argument("color", StringArgumentType.string())
                                .executes(context -> setCharacterColor(context.getSource(), StringArgumentType.getString(context, "color")))))
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

        if (targetCharacter.get().equals(manager.getActiveCharacter(player))) {
            source.sendFailure(Component.literal("Already switched to character: " + name));
            return 0;
        }

        manager.switchCharacter(player, manager.getActiveCharacter(player), targetCharacter.get());
        source.sendSuccess(() -> Component.literal("Switched to character: " + name), true);
        return 1;
    }

    private static int createCharacter(@NotNull CommandSourceStack source, String name) {
        if (!(source.getEntity() instanceof ServerPlayer player)) {
            source.sendFailure(Component.literal("Must be run by a player"));
            return 0;
        }

        CharacterManager manager = CharacterManager.get(source.getServer());
        List<Character> playerCharacters = manager.getCharactersByEntityUUID(player.getUUID());

        for (Character character : playerCharacters) {
            if (character.getName().equalsIgnoreCase(name)) {
                source.sendFailure(Component.literal("Character by that name already exists"));
                return 0;
            }
        }

        Character character = new Character(player.getUUID(), name, AnatomyType.HUMAN);

        manager.addCharacter(character);
        manager.setActiveCharacter(player, character.getId());

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
            boolean isActive = activeCharacter != null && character.getId().equals(activeCharacter.getId());
            message.append(Component.literal(
                            (isActive ? "→ " : "  ") + character.getName() + "\n")
                    .withStyle(isActive ? ChatFormatting.GREEN : ChatFormatting.GRAY));
        }

        source.sendSuccess(() -> message, false);
        return 1;
    }

    private static int setCharacterColor(@NotNull CommandSourceStack source, @NotNull String colorString) {
        if (colorString.startsWith("#")) {
            colorString = colorString.substring(1);
        }

        if (!colorString.matches("[0-9A-Fa-f]{6}")) {
            source.sendFailure(Component.literal("Invalid hex color format. Use: #RRGGBB or RRGGBB"));
            return 0;
        }

        int color;
        try {
            color = Integer.parseInt(colorString, 16);
        } catch (NumberFormatException e) {
            source.sendFailure(Component.literal("Invalid hex color format"));
            return 0;
        }

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

        character.setEmoteColor(color);
        manager.setDirty();

        source.sendSuccess(() -> Component.literal("Color set to: #" + color + " ")
                .append(Component.literal("■").withStyle(style -> style.withColor(color))), false);
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
