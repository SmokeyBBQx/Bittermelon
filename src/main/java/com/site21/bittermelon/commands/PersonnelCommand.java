package com.site21.bittermelon.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.site21.bittermelon.content.personnel.PersonnelEntry;
import com.site21.bittermelon.content.personnel.PersonnelRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PersonnelCommand {

    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("personnel")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("add")
                                .then(Commands.argument("player", EntityArgument.player())
                                        .then(Commands.argument("name", StringArgumentType.string())
                                                .executes(context -> addPersonnel(context, null, null, null))
                                                .then(Commands.argument("occupation", StringArgumentType.string())
                                                        .executes(context -> addPersonnel(context,
                                                                StringArgumentType.getString(context, "occupation"), null, null))
                                                        .then(Commands.argument("department", StringArgumentType.string())
                                                                .executes(context -> addPersonnel(context,
                                                                        StringArgumentType.getString(context, "occupation"),
                                                                        StringArgumentType.getString(context, "department"), null))
                                                                .then(Commands.argument("notes", StringArgumentType.greedyString())
                                                                        .executes(context -> addPersonnel(context,
                                                                                StringArgumentType.getString(context, "occupation"),
                                                                                StringArgumentType.getString(context, "department"),
                                                                                StringArgumentType.getString(context, "notes")))
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                        .then(Commands.literal("remove")
                                .then(Commands.argument("id", StringArgumentType.word())
                                        .executes(PersonnelCommand::removePersonnel)
                                )
                        )
                        .then(Commands.literal("list")
                                .executes(PersonnelCommand::listPersonnel)
                        )
        );
    }

    private static int addPersonnel(CommandContext<CommandSourceStack> context, String occupation, String department, String notes) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(context, "player");
        String name = StringArgumentType.getString(context, "name");

        UUID playerUUID = player.getUUID();
        String finalOccupation = occupation != null ? occupation : "Unassigned";
        String finalDepartment = department != null ? department : "";
        String finalNotes = notes != null ? notes : "No notes provided";

        PersonnelEntry entry = new PersonnelEntry(playerUUID, name, finalOccupation, finalNotes);
        entry.setDepartment(finalDepartment);

        PersonnelRegistry registry = PersonnelRegistry.get(context.getSource().getServer());
        registry.addEntry(entry);

        String deptText = finalDepartment.isEmpty() ? "" : " (" + finalDepartment + ")";
        context.getSource().sendSuccess(() ->
                        Component.literal("Added personnel entry for " + name + deptText + " (ID: " + entry.getId() + ")"),
                true);

        return 1;
    }

    private static int removePersonnel(CommandContext<CommandSourceStack> context) {
        String idString = StringArgumentType.getString(context, "id");

        try {
            int id = Integer.parseInt(idString);
            PersonnelRegistry registry = PersonnelRegistry.get(context.getSource().getServer());

            PersonnelEntry entry = registry.getEntry(id);
            if (entry != null) {
                registry.removeEntry(id);
                context.getSource().sendSuccess(() ->
                                Component.literal("Removed personnel entry: " + entry.getName() + " (ID: " + id + ")"),
                        true);
            } else {
                context.getSource().sendFailure(Component.literal("No personnel entry found with ID: " + id));
            }
        } catch (NumberFormatException e) {
            context.getSource().sendFailure(Component.literal("Invalid ID format: " + idString));
        }

        return 1;
    }

    private static int listPersonnel(@NotNull CommandContext<CommandSourceStack> context) {
        PersonnelRegistry registry = PersonnelRegistry.get(context.getSource().getServer());

        if (registry.getPersonnelEntries().isEmpty()) {
            context.getSource().sendSuccess(() -> Component.literal("No personnel entries found."), false);
            return 0;
        }

        context.getSource().sendSuccess(() -> Component.literal("Personnel Entries:"), false);

        registry.getPersonnelEntries().forEach((id, entry) -> {
            context.getSource().sendSuccess(() ->
                            Component.literal("ID: " + id + " | " + entry.getName() + " - " + entry.getOccupation() +
                                    (entry.getDepartment().isEmpty() ? "" : " (" + entry.getDepartment() + ")")),
                    false);
        });

        return 1;
    }
}