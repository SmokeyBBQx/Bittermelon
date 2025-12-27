package com.site21.bittermelon.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.site21.bittermelon.common.systems.stress.StressUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.STRESS;

public class StressCommand {
    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("stress")
                .requires(source -> source.hasPermission(2))
                // /stress set <targets> <value>
                .then(Commands.literal("set")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("value", IntegerArgumentType.integer(0))
                                        .executes(context -> setStress(
                                                context,
                                                EntityArgument.getPlayers(context, "targets"),
                                                IntegerArgumentType.getInteger(context, "value")
                                        ))
                                )
                        )
                )
                // /stress add <targets> <amount>
                .then(Commands.literal("add")
                        .then(Commands.argument("targets", EntityArgument.players())
                                .then(Commands.argument("amount", IntegerArgumentType.integer())
                                        .executes(context -> updateStress(
                                                context,
                                                EntityArgument.getPlayers(context, "targets"),
                                                IntegerArgumentType.getInteger(context, "amount")
                                        ))
                                )
                        )
                )
                // /stress get [target]
                .then(Commands.literal("get")
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(context -> getStress(
                                        context,
                                        EntityArgument.getPlayer(context, "target")
                                ))
                        )
                        .executes(context -> {
                            Entity source = context.getSource().getEntity();
                            if (source instanceof Player player) {
                                return getStress(context, player);
                            }
                            context.getSource().sendFailure(Component.literal("Must be a player to check stress"));
                            return 0;
                        })
                )
        );
    }

    private static int setStress(@NotNull CommandContext<CommandSourceStack> context, @NotNull Collection<ServerPlayer> targets, int value) {
        if (targets.isEmpty()) {
            context.getSource().sendFailure(Component.literal("No valid players found"));
            return 0;
        }

        for (ServerPlayer target : targets) {
            StressUtil.setStress(target, value);
        }

        context.getSource().sendSuccess(() ->
                Component.literal("Set stress to " + value + " for " + targets.size() + " player(s)"), true);
        return 1;
    }

    private static int updateStress(@NotNull CommandContext<CommandSourceStack> context, @NotNull Collection<ServerPlayer> targets, int amount) {
        if (targets.isEmpty()) {
            context.getSource().sendFailure(Component.literal("No valid players found"));
            return 0;
        }

        for (ServerPlayer target : targets) {
            StressUtil.updateStress(target, amount);
        }

        String action = amount >= 0 ? "Added " + amount : "Removed " + (-amount);
        context.getSource().sendSuccess(() ->
                Component.literal(action + " stress for " + targets.size() + " player(s)"), true);
        return 1;
    }

    private static int getStress(@NotNull CommandContext<CommandSourceStack> context, @NotNull Player target) {
        context.getSource().sendSuccess(() ->
                Component.literal(target.getName().getString() + "'s stress: " + target.getData(STRESS)), false);
        return 1;
    }
}
