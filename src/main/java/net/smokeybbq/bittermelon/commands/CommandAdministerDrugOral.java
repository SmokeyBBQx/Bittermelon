package net.smokeybbq.bittermelon.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;
import net.smokeybbq.bittermelon.character.medical.MedicalStats;
import net.smokeybbq.bittermelon.medical.conditions.Influenza;
import net.smokeybbq.bittermelon.medical.simulation.OralAdministration;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.substance.medicine.Acetaminophen;

import java.util.Collection;
import java.util.Optional;

public class CommandAdministerDrugOral {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("condition")
                .then(Commands.argument("characterName", StringArgumentType.string())
                        .then(Commands.argument("dosage", DoubleArgumentType.doubleArg())
                                .then(Commands.argument("absorptionModifier", DoubleArgumentType.doubleArg())
                                        .then(Commands.argument("eliminationModifier", DoubleArgumentType.doubleArg())
                                                .then(Commands.argument("metabolismModifier", DoubleArgumentType.doubleArg())
                                                        .executes(context -> addCondition(context)))))))
        );
    }

    private static int addCondition(CommandContext<CommandSourceStack> context) {
        String characterName = StringArgumentType.getString(context, "characterName");
        double dosage = DoubleArgumentType.getDouble(context, "dosage");
        double absorptionModifier = DoubleArgumentType.getDouble(context, "absorptionModifier");
        double eliminationModifier = DoubleArgumentType.getDouble(context, "eliminationModifier");
        double metabolismModifier = DoubleArgumentType.getDouble(context, "metabolismModifier");

        Collection<Character> characters = CharacterManager.getInstance().getCharacterMap().values();

        Optional<Character> selectedCharacter = characters.stream()
                .filter(c -> c.getName().equalsIgnoreCase(characterName))
                .findFirst();

        MedicalStats medicalStats = selectedCharacter.get().getMedicalStats();
        Substance substance = new Acetaminophen(absorptionModifier, eliminationModifier, metabolismModifier);
        OralAdministration simulation = new OralAdministration(dosage, selectedCharacter.get(), substance);
        medicalStats.simulationHandler.addSimulation(simulation);
        return 1;
    }
}
