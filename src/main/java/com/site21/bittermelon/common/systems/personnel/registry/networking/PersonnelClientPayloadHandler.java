package com.site21.bittermelon.common.systems.personnel.registry.networking;

import com.site21.bittermelon.common.systems.personnel.registry.PersonnelRegistry;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public class PersonnelClientPayloadHandler {
    public static void addPersonnelEntry(@NotNull AddPersonnelEntry data, @NotNull IPayloadContext ctx) {
        PersonnelRegistry registry = PersonnelRegistry.get(ctx.player().level());
        registry.addEntry(data.entry());
    }

    public static void removePersonnelEntry(@NotNull RemovePersonnelEntry data, @NotNull IPayloadContext ctx) {
        PersonnelRegistry registry = PersonnelRegistry.get(ctx.player().level());
        registry.getPersonnelEntries().remove(data.entryID());
    }

    public static void updatePersonnelEntry(@NotNull UpdatePersonnelEntry data, @NotNull IPayloadContext ctx) {
        PersonnelRegistry registry = PersonnelRegistry.get(ctx.player().level());
        registry.getPersonnelEntries().put(data.entry().getId(), data.entry());
    }
}
