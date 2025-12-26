package com.site21.bittermelon.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.DRUG_REGISTRY_KEY;

public class DrugCommand {
    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(Commands.literal("drug")
                .then(Commands.literal("give")
                        .then(Commands.argument("target", EntityArgument.entity())
                                .then(Commands.argument("drug", ResourceArgument.resource(context, DRUG_REGISTRY_KEY))
                                        .then(Commands.argument("amount", FloatArgumentType.floatArg())
                                                .executes(DrugCommand::giveDrug))))));
    }

    private static int giveDrug(@NotNull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Entity target = EntityArgument.getEntity(context, "target");
        CharacterManager characterManager = CharacterManager.get(target.level());
        Character targetCharacter = characterManager.getActiveCharacter(target);
        Holder.Reference drug = context.getArgument("drug", Holder.Reference.class);

        float amount = FloatArgumentType.getFloat(context, "amount");

        if (targetCharacter == null) return 0;

        MedicalStats medicalStats = targetCharacter.getMedicalStats();
//        medicalStats.addDrug(new DrugInstance(drug, amount));

        return 1;
    }
}
