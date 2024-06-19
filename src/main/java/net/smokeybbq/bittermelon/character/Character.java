package net.smokeybbq.bittermelon.character;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.PlayerDataStorage;
import net.minecraftforge.event.server.ServerLifecycleEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.smokeybbq.bittermelon.character.medical.MedicalStats;
import net.smokeybbq.bittermelon.chat.ChannelManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class Character {
    private final UUID uuid;
    private final UUID playerUuid;
    private String name;
    private String gender;
    private String description;
    private String skinUrl;
    private int age;
    private double height;
    private double weight;
    private String emoteColor;
    private transient MedicalStats medicalStats;

    public Character(UUID playerUuid, String name, String gender, String description, String skinUrl, int age, double height, double weight, String emoteColor) {
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
        medicalStats = new MedicalStats(this);
        ChannelManager.getInstance().setDefaultChannels(this);
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

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public String getEmoteColor() {
        return emoteColor;
    }

    public MedicalStats getMedicalStats() {
        return medicalStats;
    }

    public void update() {
//        medicalStats.update();
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

