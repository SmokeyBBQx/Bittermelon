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

import java.util.Collection;
import java.util.Optional;

public class CommandCondition {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("condition")
                .then(Commands.argument("characterName", StringArgumentType.string())
                        .then(Commands.argument("affectedArea", StringArgumentType.string())
                                .then(Commands.argument("severity", DoubleArgumentType.doubleArg())
                                        .executes(context -> addCondition(context)))))
        );
    }

    private static int addCondition(CommandContext<CommandSourceStack> context) {
        String characterName = StringArgumentType.getString(context, "characterName");
        String affectedArea = StringArgumentType.getString(context, "affectedArea");
        double severity = DoubleArgumentType.getDouble(context, "severity");
        Collection<Character> characters = CharacterManager.getInstance().getCharacterMap().values();

        Optional<Character> selectedCharacter = characters.stream()
                .filter(c -> c.getName().equalsIgnoreCase(characterName))
                .findFirst();

        MedicalStats medicalStats = selectedCharacter.get().getMedicalStats();
        Influenza influenza = new Influenza(100, false, severity, affectedArea, selectedCharacter.get());
        medicalStats.addCondition(influenza);

        return 1;
    }
}
