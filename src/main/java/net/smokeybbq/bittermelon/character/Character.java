package net.smokeybbq.bittermelon.character;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraftforge.fml.loading.FMLPaths;
import net.smokeybbq.bittermelon.character.medical.species.Species;
import net.smokeybbq.bittermelon.character.medical.species.MedicalStats;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Character {
    private final UUID uuid;
    private final UUID entityUUID;
    private final String name;
    private final String description;
    private final String skinUrl;
    private final int age;
    private final float height;
    private final float weight;
    private final String emoteColor;
    private final transient Species species;
    private final transient MedicalStats medicalStats;

    public Character(UUID entityUUID, String name, String description, String skinUrl, int age, float height, float weight, String emoteColor, Species species) {
        this.uuid = UUID.randomUUID();
        this.entityUUID = entityUUID;
        this.name = name;
        this.description = description;
        this.skinUrl = skinUrl;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.emoteColor = emoteColor;
        this.species = species;
        medicalStats = species.getMedicalStats();
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

    public MedicalStats getMedicalStats() {
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
            return NbtIo.readCompressed(new File(FMLPaths.GAMEDIR.get() + "/characters/" + uuid.toString() + "/playerData.dat"));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("There is an error with getPlayerData");
        }
        return null;
    }

}

