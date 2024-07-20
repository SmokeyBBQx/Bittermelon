package net.smokeybbq.bittermelon.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;
import net.smokeybbq.bittermelon.character.medical.species.mammal.MammalMedicalStats;

import java.util.Collection;
import java.util.Optional;

public class CommandAdministerDrugOral {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("administeroral")
                .then(Commands.argument("characterName", StringArgumentType.string())
                        .then(Commands.argument("dosage", FloatArgumentType.floatArg())
                                .then(Commands.argument("absorptionModifier", FloatArgumentType.floatArg())
                                        .then(Commands.argument("eliminationModifier", FloatArgumentType.floatArg())
                                                .then(Commands.argument("metabolismModifier", FloatArgumentType.floatArg())
                                                        .executes(context -> addCondition(context)))))))
        );
    }

    private static int addCondition(CommandContext<CommandSourceStack> context) {
        String characterName = StringArgumentType.getString(context, "characterName");
        float dosage = FloatArgumentType.getFloat(context, "dosage");
        float absorptionModifier = FloatArgumentType.getFloat(context, "absorptionModifier");
        float eliminationModifier = FloatArgumentType.getFloat(context, "eliminationModifier");
        float metabolismModifier = FloatArgumentType.getFloat(context, "metabolismModifier");

        Collection<Character> characters = CharacterManager.getInstance().getCharacterMap().values();

        Optional<Character> selectedCharacter = characters.stream()
                .filter(c -> c.getName().equalsIgnoreCase(characterName))
                .findFirst();

        MammalMedicalStats medicalStats = (MammalMedicalStats) selectedCharacter.get().getMedicalStats();

//        IVAdministration ivAdministration = new IVAdministration(dosage, selectedCharacter.get(), new Toxin("Penicillin", 0.5F, 0.3F, 0.3F, 0.001F));
//        selectedCharacter.get().getMedicalStats().simulationHandler.addSimulation(ivAdministration);
        return 1;
    }
}
