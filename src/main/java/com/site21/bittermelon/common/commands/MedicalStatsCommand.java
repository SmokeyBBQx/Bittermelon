package com.site21.bittermelon.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.site21.bittermelon.common.systems.medical.blood.BloodType;
import com.site21.bittermelon.common.systems.medical.factory.AnatomyType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.MEDICAL_STATS;

public class MedicalStatsCommand {
    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("medical")
                .then(Commands.literal("reset")
                        .then(Commands.argument("target", EntityArgument.entity())
                                .executes(MedicalStatsCommand::resetMedicalStats))
                ));
    }

    private static int resetMedicalStats(@NotNull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Entity target = EntityArgument.getEntity(context, "target");
        target.setData(MEDICAL_STATS, AnatomyType.HUMAN.getFactory().build(BloodType.O_MINUS));
        context.getSource().sendSuccess(() ->
                Component.literal(target.getDisplayName() + "'s medical stats reset"), true);

        return 1;
    }
}
