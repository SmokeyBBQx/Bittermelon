package com.site21.bittermelon.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.site21.bittermelon.common.systems.stress.StressUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.PermissionCheck;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.STRESS;
import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.STRESS_RELIEF;

public class StressCommand {
    public static final PermissionCheck PERMISSION_CHECK = new PermissionCheck.Require(Permissions.COMMANDS_ADMIN);

    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("stress")
                .requires(Commands.hasPermission(PERMISSION_CHECK))
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
                // /stress relief set <targets> <value>
                .then(Commands.literal("relief")
                        .then(Commands.literal("set")
                                .then(Commands.argument("targets", EntityArgument.players())
                                        .then(Commands.argument("value", FloatArgumentType.floatArg(0.0f, 1.0f))
                                                .executes(context -> setStressRelief(
                                                        context,
                                                        EntityArgument.getPlayers(context, "targets"),
                                                        FloatArgumentType.getFloat(context, "value")
                                                ))
                                        )
                                )
                        )
                        // /stress relief add <targets> <amount>
                        .then(Commands.literal("add")
                                .then(Commands.argument("targets", EntityArgument.players())
                                        .then(Commands.argument("amount", FloatArgumentType.floatArg())
                                                .executes(context -> updateStressRelief(
                                                        context,
                                                        EntityArgument.getPlayers(context, "targets"),
                                                        FloatArgumentType.getFloat(context, "amount")
                                                ))
                                        )
                                )
                        )
                        // /stress relief get [target]
                        .then(Commands.literal("get")
                                .then(Commands.argument("target", EntityArgument.player())
                                        .executes(context -> getStressRelief(
                                                context,
                                                EntityArgument.getPlayer(context, "target")
                                        ))
                                )
                                .executes(context -> {
                                    Entity source = context.getSource().getEntity();
                                    if (source instanceof Player player) {
                                        return getStressRelief(context, player);
                                    }
                                    context.getSource().sendFailure(Component.literal("Must be a player to check stress relief"));
                                    return 0;
                                })
                        )
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

    private static int setStressRelief(@NotNull CommandContext<CommandSourceStack> context, @NotNull Collection<ServerPlayer> targets, float value) {
        if (targets.isEmpty()) {
            context.getSource().sendFailure(Component.literal("No valid players found"));
            return 0;
        }

        for (ServerPlayer target : targets) {
            target.setData(STRESS_RELIEF, value);
        }

        context.getSource().sendSuccess(() ->
                Component.literal("Set stress relief to " + String.format("%.2f", value) + " for " + targets.size() + " player(s)"), true);
        return 1;
    }

    private static int updateStressRelief(@NotNull CommandContext<CommandSourceStack> context, @NotNull Collection<ServerPlayer> targets, float amount) {
        if (targets.isEmpty()) {
            context.getSource().sendFailure(Component.literal("No valid players found"));
            return 0;
        }

        for (ServerPlayer target : targets) {
            StressUtil.updateStressRelief(target, amount);
        }

        String action = amount >= 0 ? "Added " + String.format("%.2f", amount) : "Removed " + String.format("%.2f", -amount);
        context.getSource().sendSuccess(() ->
                Component.literal(action + " stress relief for " + targets.size() + " player(s)"), true);
        return 1;
    }

    private static int getStressRelief(@NotNull CommandContext<CommandSourceStack> context, @NotNull Player target) {
        float stressRelief = target.getData(STRESS_RELIEF);
        context.getSource().sendSuccess(() ->
                Component.literal(target.getName().getString() + "'s stress relief: " + String.format("%.2f", stressRelief)), false);
        return 1;
    }
}
