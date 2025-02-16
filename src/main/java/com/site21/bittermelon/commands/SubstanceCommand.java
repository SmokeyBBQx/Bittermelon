package com.site21.bittermelon.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.atmosphere.AtmosInstance;
import com.site21.bittermelon.atmosphere.AtmosHandler;
import com.site21.bittermelon.blocks.blockentities.FluidBlockEntity;
import com.site21.bittermelon.items.containers.substance.FluidContainerItem;
import com.site21.bittermelon.items.containers.substance.SubstanceContainerItem;
import com.site21.bittermelon.substance.Substance;
import com.site21.bittermelon.substance.SubstanceStack;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.site21.bittermelon.init.BitterBlocks.FLUID;
import static com.site21.bittermelon.init.BitterRegistries.SUBSTANCE_REGISTRY;
import static com.site21.bittermelon.init.Substances.*;

public class SubstanceCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("substance")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("fluid")
                        .then(Commands.literal("add")
                                .then(Commands.argument("substance", StringArgumentType.word())
                                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                                                .executes(context -> addSubstanceFluid(
                                                                context.getSource(),
                                                                StringArgumentType.getString(context, "substance"),
                                                                IntegerArgumentType.getInteger(context, "amount")
                                                        )
                                                )
                                        )
                                )
                                .then(Commands.literal("temperature")
                                        .then(Commands.argument("temperature", FloatArgumentType.floatArg())
                                                .executes(context -> addTemperatureFluid(
                                                                context.getSource(),
                                                                FloatArgumentType.getFloat(context, "temperature")
                                                        )
                                                )
                                        )
                                )
                        )
                        .then(Commands.literal("show")
                                .executes(context -> showFluidContents(context.getSource()))
                        )
                        .then(Commands.literal("spawn")
                                .executes(context -> spawnFluidBlock(context.getSource())))
                )
                .then(Commands.literal("container")
                        .then(Commands.literal("add")
                                .then(Commands.argument("substance", StringArgumentType.word())
                                        .then(Commands.literal("volume")
                                                .then(Commands.argument("amount", IntegerArgumentType.integer())
                                                        .executes(context -> addSubstanceContainerVolume(
                                                                context.getSource(),
                                                                StringArgumentType.getString(context, "substance"),
                                                                IntegerArgumentType.getInteger(context, "amount")
                                                        ))
                                                )
                                        )
                                        .then(Commands.literal("amount")
                                                .then(Commands.argument("amount", IntegerArgumentType.integer())
                                                        .executes(context -> addSubstanceContainerAmount(
                                                                context.getSource(),
                                                                StringArgumentType.getString(context, "substance"),
                                                                IntegerArgumentType.getInteger(context, "amount")
                                                        ))
                                                )
                                        )
                                )
                        )
                        .then(Commands.literal("show")
                                .executes(context -> showContainerContents(context.getSource()))
                        )
                )
                .then(Commands.literal("atmos")
                        .then(Commands.literal("add")
                                .then(Commands.argument("substance", StringArgumentType.word())
                                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                                                .executes(context -> addSubstanceAtmos(
                                                                context.getSource(),
                                                                StringArgumentType.getString(context, "substance"),
                                                                IntegerArgumentType.getInteger(context, "amount")
                                                        )
                                                )
                                        )
                                )
                        )
                        .then(Commands.literal("temperature")
                                .then(Commands.argument("temperature", FloatArgumentType.floatArg())
                                        .executes(context -> addTemperatureAtmos(
                                                        context.getSource(),
                                                        FloatArgumentType.getFloat(context, "temperature")
                                                )
                                        )
                                )
                        )
                        .then(Commands.literal("show")
                                .executes(context -> showAtmosContents(context.getSource()))
                        )
                )
        );
    }

    private static int addTemperatureFluid(@NotNull CommandSourceStack source, float amount) {
        BlockPos pos = BlockPos.containing(source.getPosition());
        BlockEntity blockEntity = source.getLevel().getBlockEntity(pos);

        if (!(blockEntity instanceof FluidBlockEntity fluidBlockEntity)) {
            source.sendFailure(Component.literal("You must be standing on a puddle block to use this command."));
            return 0;
        }

        fluidBlockEntity.setTemperature(amount);
        source.sendSuccess(() -> Component.literal("Temperature: " + fluidBlockEntity.getTemperature()), true);
        return 1;
    }

    private static int showAtmosContents(@NotNull CommandSourceStack source) {
        BlockPos pos = BlockPos.containing(source.getPosition());
        AtmosInstance instance = AtmosHandler.getAtmosInstanceAt(source.getLevel(), pos);

        if (instance == null) {
            source.sendFailure(Component.literal("No atmosphere found at current position."));
            return 0;
        }

        source.sendSuccess(() -> Component.literal("Temperature: " + instance.getTemperature() + " K"), true);
        source.sendSuccess(() -> Component.literal("Pressure: " + instance.getPressure() + " kPa"), true);

        if (instance.getGases().isEmpty()) {
            source.sendSuccess(() -> Component.literal("No gases present"), true);
        } else {
            source.sendSuccess(() -> Component.literal("Gases:"), true);
            for (SubstanceStack gas : instance.getGases()) {
                source.sendSuccess(() -> Component.literal(" - " + gas.getSubstance().getName() + ": " + gas.getAmount()), true);
            }
        }

        return 1;
    }

    private static int addSubstanceAtmos(@NotNull CommandSourceStack source, String substanceName, int amount) {
        BlockPos pos = BlockPos.containing(source.getPosition());
        Optional<Substance> optionalSubstance = SUBSTANCE_REGISTRY.getOptional(
                ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, substanceName)
        );

        if (optionalSubstance.isEmpty()) {
            source.sendFailure(Component.literal("Substance not found in registry: " + substanceName));
            return 0;
        }

        Substance substance = optionalSubstance.get();
        SubstanceStack stack = new SubstanceStack(substance, amount);

        AtmosHandler.releaseGas(source.getLevel(), pos, stack);
        source.sendSuccess(() -> Component.literal(String.format("Added %d %s to the atmosphere",
                amount, stack.getSubstance().getName())), true);

        return showAtmosContents(source);
    }

    private static int addTemperatureAtmos(@NotNull CommandSourceStack source, float temperature) {
        BlockPos pos = BlockPos.containing(source.getPosition());
        AtmosInstance instance = AtmosHandler.getAtmosInstanceAt(source.getLevel(), pos);

        if (instance == null) {
            source.sendFailure(Component.literal("No atmosphere found at current position."));
            return 0;
        }

        instance.setTemperature(temperature);
        source.sendSuccess(() -> Component.literal("Set atmosphere temperature to: " + temperature), true);
        return 1;
    }

    private static int showContainerContents(CommandSourceStack source) {
        return 0;
    }

    private static int addSubstanceContainerAmount(@NotNull CommandSourceStack source, String substanceName, int amount) {
        ItemStack stack = source.getPlayer().getMainHandItem();
        if (stack.getItem() instanceof SubstanceContainerItem item) {
            Optional<Substance> optionalSubstance = SUBSTANCE_REGISTRY.getOptional(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, substanceName));

            if (optionalSubstance.isPresent()) {
                Substance substance = optionalSubstance.get();
                SubstanceStack substanceStack = new SubstanceStack(substance, amount);
                item.updateSubstance(stack, substanceStack);
                return 1;
            }
        }

        return 0;
    }

    private static int addSubstanceContainerVolume(CommandSourceStack source, String substanceName, int amount) {
        ItemStack stack = source.getPlayer().getMainHandItem();
        if (stack.getItem() instanceof SubstanceContainerItem item) {
            Optional<Substance> optionalSubstance = SUBSTANCE_REGISTRY.getOptional(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, substanceName));

            if (optionalSubstance.isPresent()) {
                Substance substance = optionalSubstance.get();
                SubstanceStack substanceStack = new SubstanceStack(substance, 0);
                substanceStack.setVolume(amount);
                item.updateSubstance(stack, substanceStack);
                return 1;
            }
        }

        return 0;
    }

    private static int showFluidContents(CommandSourceStack source) {
        BlockPos pos = BlockPos.containing(source.getPosition());
        BlockEntity blockEntity = source.getLevel().getBlockEntity(pos);

        if (!(blockEntity instanceof FluidBlockEntity fluidBlockEntity)) {
            source.sendFailure(Component.literal("You must be standing on a puddle block to use this command."));
            return 0;
        }

        source.sendSuccess(() -> Component.literal(fluidBlockEntity.getContentsDescription()), true);
        source.sendSuccess(() -> Component.literal("Temperature: " + fluidBlockEntity.getTemperature()), true);
        source.sendSuccess(() -> Component.literal("Volume: " + fluidBlockEntity.getTotalVolume()), true);
        return 1;
    }

    private static int addSubstanceFluid(CommandSourceStack source, String substanceName, int amount) {
        BlockPos pos = BlockPos.containing(source.getPosition());
        BlockEntity blockEntity = source.getLevel().getBlockEntity(pos);

        if (!(blockEntity instanceof FluidBlockEntity fluidBlockEntity)) {
            source.sendFailure(Component.literal("You must be standing on a fluid block to use this command."));
            return 0;
        }

        Optional<Substance> optionalSubstance = SUBSTANCE_REGISTRY.getOptional(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, substanceName));

        if (optionalSubstance.isPresent()) {
            Substance substance = optionalSubstance.get();
            SubstanceStack stack = new SubstanceStack(substance, amount);

            fluidBlockEntity.updateSubstance(stack);
            source.sendSuccess(() -> Component.literal(String.format("Added %d %s to the puddle", amount, stack.getSubstance().getName())), true);
            source.sendSuccess(() -> Component.literal(fluidBlockEntity.getContentsDescription()), true);
            return 1;
        } else {
            source.sendFailure(Component.literal("Substance not found in registry: " + substanceName));
            return 0;
        }
    }

    private static int spawnFluidBlock(@NotNull CommandSourceStack source) {
        BlockPos pos = BlockPos.containing(source.getPosition());

        source.getLevel().setBlock(pos, FLUID.get().defaultBlockState(), 3);

        if (source.getLevel().getBlockEntity(pos) instanceof FluidBlockEntity fluidBlockEntity) {
            fluidBlockEntity.updateSubstance(new SubstanceStack(GASEOUS_WATER.get(), 1));
        }

        return 1;
    }
}
