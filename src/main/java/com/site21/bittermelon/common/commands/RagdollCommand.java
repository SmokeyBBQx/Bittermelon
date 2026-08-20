package com.site21.bittermelon.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.site21.bittermelon.common.content.entities.mimicplayer.Mimic;
import com.site21.bittermelon.common.content.entities.ragdoll.RagdollUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class RagdollCommand {
    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("ragdoll")
                .then(Commands.argument("target", EntityArgument.entity())
                        .executes(RagdollCommand::ragdoll))
        );
    }

    private static int ragdoll(@NotNull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Entity target = EntityArgument.getEntity(context, "target");

        if (target instanceof ServerPlayer player) {
            RagdollUtil.ragdollPlayer(player);
        } else if (target instanceof Mimic mimic) {
            RagdollUtil.ragdollWithDiscard(mimic);
        } else {
            return 0;
        }

        return 1;
    }
}
