package com.site21.bittermelon.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.chat.ChatHandler;
import com.site21.bittermelon.init.custom.VerbSets;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.common.systems.chat.ChatHandler.NORMAL_RANGE;
import static com.site21.bittermelon.common.systems.chat.ChatHandler.sendRPMessage;
import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.ACTIVE_CHANNEL;

public class ChatCommands {

    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("ic")
                .executes(ChatCommands::switchToIC)
                .then(Commands.argument("message", StringArgumentType.greedyString())
                        .executes(ChatCommands::sendICMessage)));

        dispatcher.register(Commands.literal("whisper")
                .then(Commands.argument("message", StringArgumentType.greedyString())
                        .executes(ChatCommands::sendWhisperMessage)));

        dispatcher.register(Commands.literal("shout")
                .then(Commands.argument("message", StringArgumentType.greedyString())
                        .executes(ChatCommands::sendShoutMessage)));

        dispatcher.register(Commands.literal("ooc")
                .executes(ChatCommands::switchToOOC)
                .then(Commands.argument("message", StringArgumentType.greedyString())
                        .executes(ChatCommands::sendOOCMessage)));

        dispatcher.register(Commands.literal("looc")
                .executes(ChatCommands::switchToLOOC)
                .then(Commands.argument("message", StringArgumentType.greedyString())
                        .executes(ChatCommands::sendLOOCMessage)));
    }

    private static int switchToIC(CommandContext<CommandSourceStack> context) {
        return switchChannel(context, 0, "IC");
    }

    private static int switchToOOC(CommandContext<CommandSourceStack> context) {
        return switchChannel(context, 1, "OOC");
    }

    private static int switchToLOOC(CommandContext<CommandSourceStack> context) {
        return switchChannel(context, 2, "LOOC");
    }

    private static int switchChannel(CommandContext<CommandSourceStack> context, int channel, String label) {
        ServerPlayer player = context.getSource().getPlayer();
        if (player == null) return 0;

        player.setData(ACTIVE_CHANNEL.get(), channel);
        context.getSource().sendSuccess(() ->
                Component.literal("Switched to " + label).withStyle(ChatFormatting.GRAY), true);
        return 1;
    }

    private static int sendICMessage(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        String message = StringArgumentType.getString(context, "message");
        Character character = getActiveCharacterOrError(player);
        if (character == null) return 0;

        sendRPMessage(character, player, message, NORMAL_RANGE, VerbSets.HUMAN.get());
        return 1;
    }

    private static int sendWhisperMessage(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        String message = StringArgumentType.getString(context, "message");
        Character character = getActiveCharacterOrError(player);
        if (character == null) return 0;

        sendRPMessage(character, player, message, ChatHandler.WHISPER_RANGE, VerbSets.HUMAN.get().whisperVerb());
        return 1;
    }

    private static int sendShoutMessage(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        String message = StringArgumentType.getString(context, "message");
        Character character = getActiveCharacterOrError(player);
        if (character == null) return 0;

        sendRPMessage(character, player, message, ChatHandler.SHOUT_RANGE, VerbSets.HUMAN.get().shoutingVerb());
        return 1;
    }

    private static int sendOOCMessage(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        String message = StringArgumentType.getString(context, "message");
        Character character = getActiveCharacterOrError(player);
        if (character == null) return 0;

        ChatHandler.sendOOCMessage(player, character, message);
        return 1;
    }

    private static int sendLOOCMessage(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        String message = StringArgumentType.getString(context, "message");
        Character character = getActiveCharacterOrError(player);
        if (character == null) return 0;

        ChatHandler.sendLOOCMessage(player, character, message);
        return 1;
    }

    private static Character getActiveCharacterOrError(@NotNull ServerPlayer player) {
        Character character = CharacterManager.get(player.level()).getActiveCharacter(player);
        if (character == null) {
            sendErrorMessage(player);
        }
        return character;
    }

    private static void sendErrorMessage(@NotNull ServerPlayer player) {
        player.sendSystemMessage(Component.literal("You must select a valid character before sending a chat message.").withColor(0xFFFF0000));
    }
}