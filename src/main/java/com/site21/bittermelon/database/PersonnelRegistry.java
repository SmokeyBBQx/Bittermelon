package com.site21.bittermelon.database;

import com.site21.bittermelon.util.DataManager;
import net.neoforged.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class PersonnelRegistry extends DataManager<Integer, PersonnelEntry> {

    protected PersonnelRegistry() {
        super(FMLPaths.GAMEDIR.get().resolve("personnel_registry/").toString(), PersonnelEntry.class);
    }

    @Override
    protected String getFileName(@NotNull PersonnelEntry data) {
        return String.valueOf(data.getID());
    }

    @Override
    protected Integer getKey(@NotNull PersonnelEntry data) {
        return data.getID();
    }

    public void addEntry(PersonnelEntry entry) {
        addData(entry.getID(), entry);
    }

    public void removeEntry(int entryID) {
        deleteData(entryID);
    }

    public void addPrivilege(int registryID, String privilege) {
        dataMap.get(registryID).addPrivilege(privilege);
    }

    public void removePrivilege(int registryID, String privilege) {
        dataMap.get(registryID).removePrivilege(privilege);
    }

    public void setPrivilege(int registryID, List<String> privileges) {
        dataMap.get(registryID).setPrivileges(privileges);
    }
}
