package com.site21.bittermelon.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class PersonnelEntry {
    private final int id;
    private String name;
    private String occupation;
    private String department;
    private String description;
    private String picture;
//    private final UUID fingerprint;
    private List<String> privileges;

    public PersonnelEntry(String name, String occupation, String description) {
        Random random = new Random();
        id = random.nextInt((int) Math.pow(10, 6));
        this.name = name;
        this.occupation = occupation;
        this.description = description;
        this.privileges = new ArrayList<>();
    }

    public int getID() {return id;}
    public String getName() {return name;}
    public String getOccupation() {return occupation;}
    public String getDepartment() {return department;}
    public String getDescription() {return description;}
    public List<String> getPrivileges() {return privileges;}

    public void setName(String name) {this.name = name;}
    public void setOccupation(String occupation) {this.occupation = occupation;}
    public void setDepartment(String department) {this.department = department;}
    public void setDescription(String description) {this.description = description;}
    public void setPrivileges(List<String> privileges) {this.privileges = privileges;}

    public void addPrivilege(String privilege) {privileges.add(privilege);}
    public void removePrivilege(String privilege) {privileges.remove(privilege);};
}

