package com.site21.bittermelon.content.personnel;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PersonnelEntry {
    public static final Codec<PersonnelEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("id").forGetter(PersonnelEntry::getID),
            Codec.STRING.fieldOf("name").forGetter(PersonnelEntry::getName),
            Codec.STRING.fieldOf("occupation").forGetter(PersonnelEntry::getOccupation),
            Codec.STRING.optionalFieldOf("department", "").forGetter(PersonnelEntry::getDepartment),
            Codec.STRING.fieldOf("description").forGetter(PersonnelEntry::getDescription),
            Codec.STRING.optionalFieldOf("picture", "").forGetter(PersonnelEntry::getPicture),
            ExtraCodecs.nonEmptyList(Codec.STRING.listOf()).optionalFieldOf("privileges", new ArrayList<>()).forGetter(PersonnelEntry::getPrivileges)
    ).apply(instance, PersonnelEntry::new));

    private final int id;
    private String name;
    private String occupation;
    private String department;
    private String description;
    private String picture;
//    private final int fingerprint;
    private List<String> privileges;

    public PersonnelEntry(int id, String name, String occupation, String department, String description, String picture, List<String> privileges) {
        this.id = id;
        this.name = name;
        this.occupation = occupation;
        this.department = department;
        this.description = description;
        this.picture = picture;
        this.privileges = privileges;
    }

    public PersonnelEntry(String name, String occupation, String description) {
        Random random = new Random();
        this.id = random.nextInt((int) Math.pow(10, 6));
        this.name = name;
        this.occupation = occupation;
        this.description = description;
        this.department = "";
        this.picture = "";
        this.privileges = new ArrayList<>();
    }

    public int getID() {return id;}
    public String getName() {return name;}
    public String getOccupation() {return occupation;}
    public String getDepartment() {return department;}
    public String getDescription() {return description;}
    public String getPicture() {return picture;}
    public List<String> getPrivileges() {return privileges;}

    public void setName(String name) {this.name = name;}
    public void setOccupation(String occupation) {this.occupation = occupation;}
    public void setDepartment(String department) {this.department = department;}
    public void setDescription(String description) {this.description = description;}
    public void setPrivileges(List<String> privileges) {this.privileges = privileges;}

    public void addPrivilege(String privilege) {privileges.add(privilege);}
    public void removePrivilege(String privilege) {privileges.remove(privilege);};
}

