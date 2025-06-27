package com.site21.bittermelon.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.content.chat.ChatHandler;
import com.site21.bittermelon.init.custom.VerbSets;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.content.chat.ChatHandler.NORMAL_RANGE;
import static com.site21.bittermelon.content.chat.ChatHandler.sendRPMessage;
import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.ACTIVE_CHANNEL;

public class ChatCommands {
    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        // Whisper command
        dispatcher.register(Commands.literal("ic")
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayer();
                    if (player == null) return 0;
                    player.setData(ACTIVE_CHANNEL.get(), 0);
                    context.getSource().sendSuccess(() ->
                            Component.literal("Switched to IC").withStyle(ChatFormatting.GRAY), true);
                    return 1;
                })
                .then(Commands.argument("message", StringArgumentType.greedyString())
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayer();
                            String message = StringArgumentType.getString(context, "message");
                            Character character = CharacterManager.get(player.level()).getActiveCharacter(player);

                            if (character == null) {
                                sendErrorMessage(player);
                                return 0;
                            }

                            sendRPMessage(character, player, message, NORMAL_RANGE, VerbSets.HUMAN.get());
                            return 1;
                        })));

        dispatcher.register(Commands.literal("whisper")
                .then(Commands.argument("message", StringArgumentType.greedyString())
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayer();
                            String message = StringArgumentType.getString(context, "message");
                            Character character = CharacterManager.get(player.level()).getActiveCharacter(player);

                            if (character == null) {
                                sendErrorMessage(player);
                                return 0;
                            }

                            sendRPMessage(character, player, message, ChatHandler.WHISPER_RANGE, VerbSets.HUMAN.get().whisperVerb());
                            return 1;
                        })));

        // Shout command
        dispatcher.register(Commands.literal("shout")
                .then(Commands.argument("message", StringArgumentType.greedyString())
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayer();
                            String message = StringArgumentType.getString(context, "message");
                            Character character = CharacterManager.get(player.level()).getActiveCharacter(player);

                            if (character == null) {
                                sendErrorMessage(player);
                                return 0;
                            }

                            sendRPMessage(character, player, message, ChatHandler.SHOUT_RANGE, VerbSets.HUMAN.get().shoutingVerb());
                            return 1;
                        })));

        // OOC command
        dispatcher.register(Commands.literal("ooc")
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayer();
                    if (player == null) return 0;
                    player.setData(ACTIVE_CHANNEL.get(), 1);
                    context.getSource().sendSuccess(() ->
                            Component.literal("Switched to OOC").withStyle(ChatFormatting.GRAY), true);
                    return 1;
                })
                .then(Commands.argument("message", StringArgumentType.greedyString())
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayer();
                            String message = StringArgumentType.getString(context, "message");
                            Character character = CharacterManager.get(player.level()).getActiveCharacter(player);

                            if (character == null) {
                                sendErrorMessage(player);
                                return 0;
                            }

                            ChatHandler.sendOOCMessage(player, character, message);
                            return 1;
                        })));

        // LOOC command
        dispatcher.register(Commands.literal("looc")
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayer();
                    if (player == null) return 0;
                    player.setData(ACTIVE_CHANNEL.get(), 2);
                    context.getSource().sendSuccess(() ->
                            Component.literal("Switched to LOOC").withStyle(ChatFormatting.GRAY), true);
                    return 1;
                })
                .then(Commands.argument("message", StringArgumentType.greedyString())
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayer();
                            String message = StringArgumentType.getString(context, "message");
                            Character character = CharacterManager.get(player.level()).getActiveCharacter(player);

                            if (character == null) {
                                sendErrorMessage(player);
                                return 0;
                            }

                            ChatHandler.sendLOOCMessage(player, character, message);
                            return 1;
                        })));
    }

    private static void sendErrorMessage(@NotNull ServerPlayer player) {
        player.sendSystemMessage(Component.literal("You must select a valid character before sending a chat message.").withColor(0xFFFF0000));
    }
}
