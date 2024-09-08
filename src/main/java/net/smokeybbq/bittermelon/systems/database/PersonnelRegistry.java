package net.smokeybbq.bittermelon.systems.database;

import net.minecraftforge.fml.loading.FMLPaths;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.util.DataManager;

import java.util.List;
import java.util.UUID;

public class PersonnelRegistry extends DataManager<UUID, PersonnelEntry> {

    protected PersonnelRegistry() {
        super(FMLPaths.GAMEDIR.get().resolve("personnel_registry/").toString(), PersonnelEntry.class);
    }

    @Override
    protected String getFileName(PersonnelEntry data) {
        return data.getID().toString();
    }

    @Override
    protected UUID getKey(PersonnelEntry data) {
        return data.getID();
    }

    public void addEntry(PersonnelEntry entry) {
        addData(entry.getID(), entry);
    }

    public void removeEntry(UUID entryID) {
        deleteData(entryID);
    }

    public void addPrivilege(UUID registryID, String privilege) {
        dataMap.get(registryID).addPrivilege(privilege);
    }

    public void removePrivilege(UUID registryID, String privilege) {
        dataMap.get(registryID).removePrivilege(privilege);
    }

    public void setPrivilege(UUID registryID, List<String> privileges) {
        dataMap.get(registryID).setPrivileges(privileges);
    }
}
