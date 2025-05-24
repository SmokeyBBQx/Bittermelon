package com.site21.bittermelon.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.networking.client.OpenCPRScreen;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class PersonnelCommand {
//    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
//        dispatcher.register(Commands.literal("personnel")
//                .then(Commands.literal("add")
//                        .executes(CPRCommand::performCPR))
//        );
//    }
//
//    private static int performCPR(@NotNull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
//        Entity target = EntityArgument.getEntity(context, "target");
//        Character targetCharacter = CharacterManager.get(target.level()).getActiveCharacter(target);
//
//        if (targetCharacter == null) return 0;
//
//        ServerPlayer commandSender = context.getSource().getPlayerOrException();
//        PacketDistributor.sendToPlayer(commandSender, new OpenCPRScreen(targetCharacter.getUUID()));
//
//        return 1;
//    }
}
