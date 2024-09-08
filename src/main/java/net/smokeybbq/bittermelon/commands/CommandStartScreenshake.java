package net.smokeybbq.bittermelon.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.smokeybbq.bittermelon.client.effects.ScreenshakeHandler;

public class CommandStartScreenshake {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("screenshake")
                .then(Commands.argument("player", EntityArgument.player())
                        .then(Commands.argument("duration", IntegerArgumentType.integer())
                                .then(Commands.argument("intensity", FloatArgumentType.floatArg())
                                        .executes(context -> stumble(
                                                        context,
                                                        EntityArgument.getPlayer(context, "player"),
                                                        IntegerArgumentType.getInteger(context, "duration"),
                                                        FloatArgumentType.getFloat(context, "intensity")
                                                )
                                        )
                                )
                        )
                )
        );
    }

    private static int stumble(CommandContext<CommandSourceStack> context, ServerPlayer player, int duration, float intensity) {
        ScreenshakeHandler.startScreenshake(player, duration, intensity);
        return 1;
    }
}
