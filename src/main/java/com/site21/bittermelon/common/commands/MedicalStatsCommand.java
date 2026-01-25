package com.site21.bittermelon.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.site21.bittermelon.common.systems.medical.anatomy.Anatomy;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.MEDICAL_STATS;
import static com.site21.bittermelon.init.neoforge.BitterRegistries.ANATOMY_REGISTRY_KEY;

public class MedicalStatsCommand {
    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
        dispatcher.register(Commands.literal("medical")
                .then(Commands.literal("reset")
                        .then(Commands.argument("target", EntityArgument.entity())
                                .then(Commands.argument("anatomy", ResourceArgument.resource(buildContext, ANATOMY_REGISTRY_KEY))
                                        .executes(MedicalStatsCommand::resetMedicalStats))
                        )));
    }

    private static int resetMedicalStats(@NotNull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Entity target = EntityArgument.getEntity(context, "target");
        Anatomy anatomy = ResourceArgument.getResource(context, "anatomy", ANATOMY_REGISTRY_KEY).value();
        target.setData(MEDICAL_STATS, anatomy.toInstance(target));
        context.getSource().sendSuccess(() ->
                Component.literal(target.getDisplayName() + "'s medical stats reset"), true);

        return 1;
    }
}
