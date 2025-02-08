package com.site21.bittermelon.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.site21.bittermelon.client.gui.loreopening.LoreOpeningData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

public class SetLoreOpeningCommand {
    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("setloreopening")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("message", StringArgumentType.greedyString())
                        .executes(context -> {
                            String message = StringArgumentType.getString(context, "message");
                            ServerLevel level = context.getSource().getLevel();
                            LoreOpeningData storage = LoreOpeningData.get(level);
                            storage.setMessage(message);

                            context.getSource().sendSuccess(
                                    () -> Component.literal("Lore opening updated successfully"),
                                    true
                            );
                            return Command.SINGLE_SUCCESS;
                        })));
    }
}
