package com.site21.bittermelon.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.site21.bittermelon.common.systems.blockdamage.BlockDamageUtil;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class BlockDamageCommand {
    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("damageblock")
                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                        .then(Commands.argument("amount", IntegerArgumentType.integer(1, 100))
                                .executes(context -> damageBlock(
                                        context.getSource(),
                                        BlockPosArgument.getBlockPos(context, "pos"),
                                        IntegerArgumentType.getInteger(context, "amount")
                                )))));

        dispatcher.register(Commands.literal("getblockdamage")
                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                        .executes(context -> getBlockDamage(
                                context.getSource(),
                                BlockPosArgument.getBlockPos(context, "pos")
                        ))));
    }

    private static int damageBlock(@NotNull CommandSourceStack source, BlockPos pos, int amount) {
        BlockDamageUtil.addDamage(source.getLevel(), pos, amount);
        return 1;
    }

    private static int getBlockDamage(@NotNull CommandSourceStack source, BlockPos pos) {
        int damage = BlockDamageUtil.getDamage(source.getLevel(), pos);
        source.sendSuccess(() -> Component.literal("Block damage at " + pos + ": " + damage), false);
        return damage;
    }
}