package com.site21.bittermelon.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.site21.bittermelon.systems.stumble.StumbleHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class StumbleCommand {
    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("stumble")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .executes(context -> stumble(context, EntityArgument.getEntities(context, "targets")))
                )
                .executes(context -> {
                    Entity source = context.getSource().getEntity();
                    if (source instanceof LivingEntity living) {
                        StumbleHandler.stumble(living);
                        return 1;
                    }
                    context.getSource().sendFailure(Component.literal("Must be a living entity to stumble"));
                    return 0;
                })
        );
    }

    private static int stumble(CommandContext<CommandSourceStack> context, @NotNull Collection<? extends Entity> targets) {
        int count = 0;

        for (Entity target : targets) {
            if (target instanceof LivingEntity living) {
                StumbleHandler.stumble(living);
                count++;
            }
        }

        if (count > 0) {
            int finalCount = count;
            context.getSource().sendSuccess(() ->
                    Component.literal("Made " + finalCount + " entities stumble"), true);
        } else {
            context.getSource().sendFailure(
                    Component.literal("No valid entities found to stumble"));
        }

        return count;
    }
}
