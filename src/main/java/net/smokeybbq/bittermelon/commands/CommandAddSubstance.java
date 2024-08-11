package net.smokeybbq.bittermelon.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.smokeybbq.bittermelon.blocks.blockentities.PuddleBlockEntity;
import net.smokeybbq.bittermelon.init.SubstanceInit;
import net.smokeybbq.bittermelon.items.substancecontainers.SubstanceContainerItem;
import net.smokeybbq.bittermelon.substances.Substance;

import java.util.Objects;

import static net.smokeybbq.bittermelon.init.SubstanceInit.getSubstance;

public class CommandAddSubstance {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("addsubstance")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("puddle")
                        .then(Commands.argument("substance", StringArgumentType.word())
                                .then(Commands.argument("amount", IntegerArgumentType.integer())
                                        .executes(context -> addSubstancePuddle(
                                                context.getSource(),
                                                StringArgumentType.getString(context, "substance"),
                                                IntegerArgumentType.getInteger(context, "amount")
                                        ))
                                )
                        )
                )
                .then(Commands.literal("container")
                        .then(Commands.argument("substance", StringArgumentType.word())
                                .then(Commands.argument("amount", IntegerArgumentType.integer())
                                        .executes(context -> addSubstanceContainer(
                                                context.getSource(),
                                                StringArgumentType.getString(context, "substance"),
                                                IntegerArgumentType.getInteger(context, "amount")
                                        ))
                                )
                        )
                )
        );
    }

    private static int addSubstancePuddle(CommandSourceStack source, String substanceName, int amount) throws CommandSyntaxException {
        BlockPos pos = BlockPos.containing(source.getPosition());
        BlockEntity blockEntity = source.getLevel().getBlockEntity(pos);

        if (!(blockEntity instanceof PuddleBlockEntity puddleBlockEntity)) {
            source.sendFailure(Component.literal("You must be standing on a puddle block to use this command."));
            return 0;
        }

        Substance substance = getSubstance(substanceName);

        puddleBlockEntity.updateSubstance(substance, amount);
        source.sendSuccess(() -> Component.literal(String.format("Added %.1f%% %s to the puddle", amount, substance.getName())), true);
        source.sendSuccess(puddleBlockEntity::getContentsDescription, true);
        return 1;
    }

    private static int addSubstanceContainer(CommandSourceStack source, String substanceName, int amount) {
        Player player = Objects.requireNonNull(source.getPlayer());
        ItemStack itemStack = player.getMainHandItem();

        if (!(itemStack.getItem() instanceof SubstanceContainerItem containerItem)) {
            source.sendFailure(Component.literal("You must be holding a container!"));
            return 0;
        }

        Substance substance = SubstanceInit.getSubstance(substanceName);
        if (substance == null) {
            source.sendFailure(Component.literal("Invalid substance name!"));
            return 0;
        }

        containerItem.updateSubstance(itemStack, substance, amount);
        source.sendSuccess(() -> Component.literal("Updated " + substanceName + " by " + amount + " in the container"), true);

        return 1;
    }
}
