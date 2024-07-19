package net.smokeybbq.bittermelon.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;
import net.smokeybbq.bittermelon.miscellaneous.Stumble;

public class CommandStumble {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("stumble")
                .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> stumble(context,EntityArgument.getPlayer(context,"player"))))
        );
    }

    private static int stumble(CommandContext<CommandSourceStack> context, ServerPlayer player) {
        Stumble stumble = new Stumble(player);
        return 1;
    }


}
