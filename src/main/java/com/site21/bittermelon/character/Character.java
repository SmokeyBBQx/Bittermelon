package com.site21.bittermelon.character;

import java.util.UUID;

public class Character {
    private final UUID uuid;
    private final UUID entityUUID;
    private String name;
    private String description = "";
    private String gender = "";
    private int age = 18;
    private float height = 180;
    private float weight = 70;
    private String emoteColor = "FFFF55";

    public Character(UUID entityUUID, String name) {
        this.uuid = UUID.randomUUID();
        this.entityUUID = entityUUID;
        this.name = name;
    }

    public Character(UUID entityUUID, String name, String description, String gender, int age, float height, float weight, String emoteColor) {
        this.uuid = UUID.randomUUID();
        this.entityUUID = entityUUID;
        this.name = name;
        this.description = description;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.emoteColor = emoteColor;
    }

    public UUID getUUID() {
        return uuid;
    }

    public UUID getEntityUUID() {
        return entityUUID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getGender() {return gender;}

    public int getAge() {
        return age;
    }

    public float getHeight() {
        return height;
    }

    public float getWeight() {
        return weight;
    }

    public String getEmoteColor() {
        return emoteColor;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEmoteColor(String emoteColor) {
        this.emoteColor = emoteColor;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
