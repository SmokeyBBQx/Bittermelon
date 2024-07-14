package net.smokeybbq.bittermelon.character;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraftforge.fml.loading.FMLPaths;
import net.smokeybbq.bittermelon.character.medical.AnimalMedicalStats;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static net.smokeybbq.bittermelon.medical.simulation.compartments.anatomies.HumanFactory.createCompartments;

public class Character {
    private final UUID uuid;
    private final UUID playerUuid;
    private String name;
    private String gender;
    private String description;
    private String skinUrl;
    private int age;
    private float height;
    private float weight;
    private String emoteColor;
    private transient AnimalMedicalStats medicalStats;

    public Character(UUID playerUuid, String name, String gender, String description, String skinUrl, int age, float height, float weight, String emoteColor) {
        this.uuid = UUID.randomUUID();
        this.playerUuid = playerUuid;
        this.name = name;
        this.gender = gender;
        this.description = description;
        this.skinUrl = skinUrl;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.emoteColor = emoteColor;
        this.medicalStats = new AnimalMedicalStats(this, createCompartments());
    }

    public UUID getUUID() {
        return uuid;
    }

    public UUID getPlayerUUID() {
        return playerUuid;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getDescription() {
        return description;
    }

    public String getSkinUrl() {
        return skinUrl;
    }

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

    public AnimalMedicalStats getMedicalStats() {
        return medicalStats;
    }

    public void update() {
        medicalStats.update();
    }

    public void savePlayerData(CompoundTag data) {
        try {
            NbtIo.writeCompressed(data, new File(FMLPaths.GAMEDIR.get() + "/characters/" + uuid.toString() + "/playerData.dat"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("There is an error with savePlayerData");
        }
        CharacterManager.getInstance().updateData(this);
    }

    public CompoundTag getPlayerData() {
        try {
            CompoundTag data = NbtIo.readCompressed(new File(FMLPaths.GAMEDIR.get() + "/characters/" + uuid.toString() + "/playerData.dat"));
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("There is an error with getPlayerData");
        }
        return null;
    }

}

