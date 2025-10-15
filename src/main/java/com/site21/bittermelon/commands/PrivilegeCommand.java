package com.site21.bittermelon.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.site21.bittermelon.systems.personnel.privilege.PrivilegeGroup;
import com.site21.bittermelon.systems.personnel.privilege.PrivilegeManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class PrivilegeCommand {
    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("privilege")
                .requires(source -> source.hasPermission(4))
                .then(Commands.literal("add")
                        .then(Commands.argument("name", StringArgumentType.string())
                                .executes(PrivilegeCommand::addPrivilege)))
                .then(Commands.literal("remove")
                        .then(Commands.argument("name", StringArgumentType.string())
                                .executes(PrivilegeCommand::removePrivilege)))
                .then(Commands.literal("list")
                        .executes(PrivilegeCommand::listPrivileges))
        );

        dispatcher.register(Commands.literal("privilegegroup")
                .requires(source -> source.hasPermission(4))
                .then(Commands.literal("create")
                        .then(Commands.argument("name", StringArgumentType.string())
                                .executes(PrivilegeCommand::createGroup)))
                .then(Commands.literal("remove")
                        .then(Commands.argument("name", StringArgumentType.string())
                                .executes(PrivilegeCommand::removeGroup)))
                .then(Commands.literal("addprivilege")
                        .then(Commands.argument("group", StringArgumentType.string())
                                .then(Commands.argument("privilege", StringArgumentType.string())
                                        .executes(PrivilegeCommand::addPrivilegeToGroup))))
                .then(Commands.literal("removeprivilege")
                        .then(Commands.argument("group", StringArgumentType.string())
                                .then(Commands.argument("privilege", StringArgumentType.string())
                                        .executes(PrivilegeCommand::removePrivilegeFromGroup))))
                .then(Commands.literal("addparent")
                        .then(Commands.argument("group", StringArgumentType.string())
                                .then(Commands.argument("parent", StringArgumentType.string())
                                        .executes(PrivilegeCommand::addParentToGroup))))
                .then(Commands.literal("removeparent")
                        .then(Commands.argument("group", StringArgumentType.string())
                                .then(Commands.argument("parent", StringArgumentType.string())
                                        .executes(PrivilegeCommand::removeParentFromGroup))))
                .then(Commands.literal("list")
                        .executes(PrivilegeCommand::listGroups))
                .then(Commands.literal("info")
                        .then(Commands.argument("name", StringArgumentType.string())
                                .executes(PrivilegeCommand::groupInfo)))
        );
    }

    private static int addPrivilege(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String privilege = StringArgumentType.getString(context, "name");
        PrivilegeManager manager = PrivilegeManager.get(context.getSource().getServer());

        if (manager.addPrivilege(privilege)) {
            context.getSource().sendSuccess(() -> Component.literal("Added privilege: " + privilege), true);
            return 1;
        } else {
            context.getSource().sendFailure(Component.literal("Privilege already exists: " + privilege));
            return 0;
        }
    }

    private static int removePrivilege(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String privilege = StringArgumentType.getString(context, "name");
        PrivilegeManager manager = PrivilegeManager.get(context.getSource().getServer());

        if (manager.removePrivilege(privilege)) {
            context.getSource().sendSuccess(() -> Component.literal("Removed privilege: " + privilege), true);
            return 1;
        } else {
            context.getSource().sendFailure(Component.literal("Privilege does not exist: " + privilege));
            return 0;
        }
    }

    private static int listPrivileges(@NotNull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        PrivilegeManager manager = PrivilegeManager.get(context.getSource().getServer());

        if (manager.getPrivileges().isEmpty()) {
            context.getSource().sendSuccess(() -> Component.literal("No privileges defined"), false);
        } else {
            context.getSource().sendSuccess(() -> Component.literal("Privileges: " + String.join(", ", manager.getPrivileges())), false);
        }
        return 1;
    }

    private static int createGroup(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "name");
        PrivilegeManager manager = PrivilegeManager.get(context.getSource().getServer());

        PrivilegeGroup group = new PrivilegeGroup(name);
        if (manager.addPrivilegeGroup(group)) {
            context.getSource().sendSuccess(() -> Component.literal("Created privilege group: " + name), true);
            return 1;
        } else {
            context.getSource().sendFailure(Component.literal("Group already exists or name conflicts with privilege: " + name));
            return 0;
        }
    }

    private static int removeGroup(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "name");
        PrivilegeManager manager = PrivilegeManager.get(context.getSource().getServer());

        if (manager.getPrivilegeGroup(name) != null) {
            manager.removePrivilegeGroup(name);
            context.getSource().sendSuccess(() -> Component.literal("Removed privilege group: " + name), true);
            return 1;
        } else {
            context.getSource().sendFailure(Component.literal("Group does not exist: " + name));
            return 0;
        }
    }

    private static int addPrivilegeToGroup(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String groupName = StringArgumentType.getString(context, "group");
        String privilege = StringArgumentType.getString(context, "privilege");
        PrivilegeManager manager = PrivilegeManager.get(context.getSource().getServer());

        PrivilegeGroup group = manager.getPrivilegeGroup(groupName);
        if (group == null) {
            context.getSource().sendFailure(Component.literal("Group does not exist: " + groupName));
            return 0;
        }

        if (!manager.privilegeExists(privilege)) {
            context.getSource().sendFailure(Component.literal("Privilege does not exist: " + privilege));
            return 0;
        }

        if (group.addPrivilege(privilege)) {
            manager.setDirty();
            context.getSource().sendSuccess(() -> Component.literal("Added privilege '" + privilege + "' to group '" + groupName + "'"), true);
            return 1;
        } else {
            context.getSource().sendFailure(Component.literal("Privilege already in group: " + privilege));
            return 0;
        }
    }

    private static int removePrivilegeFromGroup(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String groupName = StringArgumentType.getString(context, "group");
        String privilege = StringArgumentType.getString(context, "privilege");
        PrivilegeManager manager = PrivilegeManager.get(context.getSource().getServer());

        PrivilegeGroup group = manager.getPrivilegeGroup(groupName);
        if (group == null) {
            context.getSource().sendFailure(Component.literal("Group does not exist: " + groupName));
            return 0;
        }

        if (group.removePrivilege(privilege)) {
            manager.setDirty();
            context.getSource().sendSuccess(() -> Component.literal("Removed privilege '" + privilege + "' from group '" + groupName + "'"), true);
            return 1;
        } else {
            context.getSource().sendFailure(Component.literal("Privilege not in group: " + privilege));
            return 0;
        }
    }

    private static int addParentToGroup(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String groupName = StringArgumentType.getString(context, "group");
        String parentName = StringArgumentType.getString(context, "parent");
        PrivilegeManager manager = PrivilegeManager.get(context.getSource().getServer());

        PrivilegeGroup group = manager.getPrivilegeGroup(groupName);
        PrivilegeGroup parent = manager.getPrivilegeGroup(parentName);

        if (group == null) {
            context.getSource().sendFailure(Component.literal("Group does not exist: " + groupName));
            return 0;
        }

        if (parent == null) {
            context.getSource().sendFailure(Component.literal("Parent group does not exist: " + parentName));
            return 0;
        }

        if (groupName.equals(parentName)) {
            context.getSource().sendFailure(Component.literal("Group cannot be its own parent"));
            return 0;
        }

        if (group.addParent(parentName)) {
            manager.setDirty();
            context.getSource().sendSuccess(() -> Component.literal("Added parent '" + parentName + "' to group '" + groupName + "'"), true);
            return 1;
        } else {
            context.getSource().sendFailure(Component.literal("Parent already exists or would create cycle: " + parentName));
            return 0;
        }
    }

    private static int removeParentFromGroup(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String groupName = StringArgumentType.getString(context, "group");
        String parentName = StringArgumentType.getString(context, "parent");
        PrivilegeManager manager = PrivilegeManager.get(context.getSource().getServer());

        PrivilegeGroup group = manager.getPrivilegeGroup(groupName);
        if (group == null) {
            context.getSource().sendFailure(Component.literal("Group does not exist: " + groupName));
            return 0;
        }

        if (group.removeParent(parentName)) {
            manager.setDirty();
            context.getSource().sendSuccess(() -> Component.literal("Removed parent '" + parentName + "' from group '" + groupName + "'"), true);
            return 1;
        } else {
            context.getSource().sendFailure(Component.literal("Parent not found in group: " + parentName));
            return 0;
        }
    }

    private static int listGroups(@NotNull CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        PrivilegeManager manager = PrivilegeManager.get(context.getSource().getServer());

        if (manager.getPrivilegeGroups().isEmpty()) {
            context.getSource().sendSuccess(() -> Component.literal("No privilege groups defined"), false);
        } else {
            context.getSource().sendSuccess(() -> Component.literal("Privilege groups: " + String.join(", ", manager.getPrivilegeGroups().keySet())), false);
        }
        return 1;
    }

    private static int groupInfo(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "name");
        PrivilegeManager manager = PrivilegeManager.get(context.getSource().getServer());

        PrivilegeGroup group = manager.getPrivilegeGroup(name);
        if (group == null) {
            context.getSource().sendFailure(Component.literal("Group does not exist: " + name));
            return 0;
        }

        context.getSource().sendSuccess(() -> Component.literal("Group: " + name), false);
//        context.getSource().sendSuccess(() -> Component.literal("Privileges: " + String.join(", ", group.getPrivileges())), false);
        context.getSource().sendSuccess(() -> Component.literal("Parents: " + String.join(", ", group.getParents())), false);

        return 1;
    }
}
