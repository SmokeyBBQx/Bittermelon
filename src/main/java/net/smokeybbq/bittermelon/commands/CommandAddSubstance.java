package net.smokeybbq.bittermelon.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.smokeybbq.bittermelon.blocks.blockentities.PuddleBlockEntity;
import net.smokeybbq.bittermelon.init.SubstanceInit;
import net.smokeybbq.bittermelon.substances.Substance;

public class CommandAddSubstance {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("addsubstance")
                .requires(source -> source.hasPermission(2))
                .then(Commands.argument("substance", StringArgumentType.word())
                        .then(Commands.argument("amount", FloatArgumentType.floatArg(0, 100))
                                .executes(context -> addSubstance(
                                        context.getSource(),
                                        StringArgumentType.getString(context, "substance"),
                                        FloatArgumentType.getFloat(context, "amount")
                                ))
                        )
                )
        );
    }

    private static int addSubstance(CommandSourceStack source, String substanceName, float amount) throws CommandSyntaxException {
        BlockPos pos = BlockPos.containing(source.getPosition());
        BlockEntity blockEntity = source.getLevel().getBlockEntity(pos);

        if (!(blockEntity instanceof PuddleBlockEntity puddleBlockEntity)) {
            source.sendFailure(Component.literal("You must be standing on a puddle block to use this command."));
            return 0;
        }

        Substance substance = SubstanceInit.getSubstance(substanceName);

        if (substance == null) {
            source.sendFailure(Component.literal("Unknown substance: " + substanceName));
            return 0;
        }

        puddleBlockEntity.addSubstance(substance, amount);
        source.sendSuccess(() -> Component.literal(String.format("Added %.1f%% %s to the puddle", amount, substance.getName())), true);
        source.sendSuccess(puddleBlockEntity::getContentsDescription, true);
         return 1;
    }
}
