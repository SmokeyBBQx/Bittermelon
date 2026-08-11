package com.site21.bittermelon.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.site21.bittermelon.common.systems.medical.legacy.anatomy.Anatomy;
import com.site21.bittermelon.common.systems.medical.legacy.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.legacy.damage.DamageGen;
import com.site21.bittermelon.common.systems.medical.legacy.medicalstats.MedicalStats;
import com.site21.bittermelon.init.custom.Compartments;
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
                        ))
                .then(Commands.literal("damage")
                        .then(Commands.argument("target", EntityArgument.entity())
                                .executes(MedicalStatsCommand::applyDamage))
                ));
    }

    private static int resetMedicalStats(@NotNull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Entity target = EntityArgument.getEntity(context, "target");
        Anatomy anatomy = ResourceArgument.getResource(context, "anatomy", ANATOMY_REGISTRY_KEY).value();
        target.setData(MEDICAL_STATS, anatomy.toInstance(target));
        context.getSource().sendSuccess(() ->
                Component.literal(target.getDisplayName() + "'s medical stats reset"), true);

        return 1;
    }

    private static int applyDamage(@NotNull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Entity target = EntityArgument.getEntity(context, "target");
        MedicalStats medicalStats = target.getData(MEDICAL_STATS);

        for (CompartmentInstance compartment : medicalStats.getCompartments().values()) {
            if (compartment.getCompartment().equals(Compartments.UPPER_ARM.get())) {
                // DamageGen.makeLaceration(medicalStats, target.getRandom(), compartment, 0, 4, 2);
                DamageGen.handleBullet(medicalStats, target.getRandom(), compartment, 0.8f);
            }
        }

        target.syncData(MEDICAL_STATS);

        return 1;
    }
}
