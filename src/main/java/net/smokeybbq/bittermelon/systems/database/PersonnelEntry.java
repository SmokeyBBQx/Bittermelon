package net.smokeybbq.bittermelon.systems.database;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PersonnelEntry {
    private final UUID id;
    private String name;
    private String occupation;
    private String description;
    private List<String> privileges;

    public PersonnelEntry(String name, String occupation, String description) {
        id = UUID.randomUUID();
        this.name = name;
        this.occupation = occupation;
        this.description = description;
        this.privileges = new ArrayList<>();
    }

    public UUID getID() {return id;}
    public String getName() {return name;}
    public String getOccupation() {return occupation;}
    public String getDescription() {return description;}
    public List<String> getPrivileges() {return privileges;}

    public void setName(String name) {this.name = name;}
    public void setOccupation(String occupation) {this.occupation = occupation;}
    public void setDescription(String description) {this.description = description;}
    public void setPrivileges(List<String> privileges) {this.privileges = privileges;}

    public void addPrivilege(String privilege) {privileges.add(privilege);}
    public void removePrivilege(String privilege) {privileges.remove(privilege);};
}
