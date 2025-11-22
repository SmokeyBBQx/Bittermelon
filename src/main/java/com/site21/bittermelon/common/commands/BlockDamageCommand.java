package com.site21.bittermelon.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.site21.bittermelon.common.systems.blockdamage.BlockDamageHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;

public class BlockDamageCommand {
    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("damageblock")
                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                                .executes(context -> damageBlock(
                                        context.getSource(),
                                        BlockPosArgument.getBlockPos(context, "pos"),
                                        IntegerArgumentType.getInteger(context, "amount")
                                )))));
    }

    private static int damageBlock(@NotNull CommandSourceStack source, BlockPos pos, int amount) {
        BlockDamageHelper.addDamage(source.getLevel(), pos, amount);
        return 1;
    }
}