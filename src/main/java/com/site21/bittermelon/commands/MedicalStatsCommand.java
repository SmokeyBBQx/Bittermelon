package com.site21.bittermelon.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.site21.bittermelon.systems.character.Character;
import com.site21.bittermelon.systems.character.CharacterManager;
import com.site21.bittermelon.systems.medical.blood.BloodType;
import com.site21.bittermelon.systems.medical.factory.Anatomy;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

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
        CharacterManager characterManager = CharacterManager.get(target.level());
        Character targetCharacter = characterManager.getActiveCharacter(target);

        if (targetCharacter == null) return 0;

        targetCharacter.setMedicalStats(Anatomy.HUMAN.getFactory().build(BloodType.O_MINUS, targetCharacter));
        characterManager.setDirty();
        context.getSource().sendSuccess(() ->
                Component.literal(targetCharacter.getName() + "'s medical stats reset"), true);
        return 1;
    }
}
