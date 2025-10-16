package com.site21.bittermelon.common.systems.personnel.privilege.networking;

import com.site21.bittermelon.common.systems.personnel.privilege.PrivilegeManager;
import com.site21.bittermelon.common.systems.personnel.registry.PersonnelRegistry;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class PrivilegeClientPayloadHandler {
    public static void handleAddPrivilegeGroup(final @NotNull AddPrivilegeGroup data, final @NotNull IPayloadContext ctx) {
        PrivilegeManager manager = PrivilegeManager.get(ctx.player().level());
        manager.getPrivilegeGroups().put(data.group().getName(), data.group());
    }

    public static void handleRemovePrivilegeGroup(final @NotNull RemovePrivilegeGroup data, final @NotNull IPayloadContext ctx) {
        PrivilegeManager manager = PrivilegeManager.get(ctx.player().level());
        manager.getPrivilegeGroups().remove(data.group());
    }

    public static void handleAddPrivilege(final @NotNull AddPrivilege data, final @NotNull IPayloadContext ctx) {
        PrivilegeManager manager = PrivilegeManager.get(ctx.player().level());
        manager.addPrivilegeFromServer(data.privilege());
    }

    public static void handleRemovePrivilege(final @NotNull RemovePrivilege data, final @NotNull IPayloadContext ctx) {
        PrivilegeManager manager = PrivilegeManager.get(ctx.player().level());
        manager.getPrivileges().remove(data.privilege());
    }

    public static void setPrivilegeForEntry(final @NotNull SetPrivilegeForEntry data, final @NotNull IPayloadContext ctx) {
        PersonnelRegistry registry = PersonnelRegistry.get(ctx.player().level());
        registry.getEntry(data.entryID()).setPrivilege(data.privilege(), data.value());
    }

    public static void removePrivilegeForEntry(final @NotNull RemovePrivilegeForEntry data, final @NotNull IPayloadContext ctx) {
        PersonnelRegistry registry = PersonnelRegistry.get(ctx.player().level());
        registry.getEntry(data.entryID()).removePrivilege(data.privilege());
    }

    public static void setPrivilegeForGroup(final @NotNull SetPrivilegeForGroup data, final @NotNull IPayloadContext ctx) {
        PrivilegeManager manager = PrivilegeManager.get(ctx.player().level());
        manager.getPrivilegeGroup(data.group()).setPrivilege(data.privilege(), data.value());
    }

    public static void removePrivilegeForGroup(final @NotNull RemovePrivilegeForGroup data, final @NotNull IPayloadContext ctx) {
        PrivilegeManager manager = PrivilegeManager.get(ctx.player().level());
        manager.getPrivilegeGroup(data.group()).removePrivilege(data.privilege());
    }
}
