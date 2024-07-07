package net.smokeybbq.bittermelon.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;
import net.smokeybbq.bittermelon.medical.simulation.compartments.Compartment;

public class CommandAddTumor {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("addtumor")
                .then(Commands.argument("player", EntityArgument.player())
                        .executes(context -> addTumor(context, EntityArgument.getPlayer(context,"player"))))
        );
    }

    private static int addTumor(CommandContext<CommandSourceStack> context, ServerPlayer player) {
        Character selectedCharacter = CharacterManager.getActiveCharacter(player);
        selectedCharacter.getMedicalStats().addCompartment(new Compartment("Tumor", 1F));
        return 1;
    }
}
