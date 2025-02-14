package com.site21.bittermelon.character;

import com.site21.bittermelon.medical.factory.Anatomy;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.neoforged.fml.loading.FMLPaths;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

public class PlayerCharacter extends Character {
    private String skinURL;

    public PlayerCharacter(UUID entityUUID, String name, Anatomy anatomy) {
        super(entityUUID, name, anatomy);
    }

    public String getSkinURL() {
        return skinURL;
    }

    public void setSkinURL(String skinURL) {
        this.skinURL = skinURL;
    }

    public void savePlayerData(CompoundTag data) {
//        try {
//            NbtIo.writeCompressed(data, new File(FMLPaths.GAMEDIR.get() + "/characters/" + this.getUUID().toString() + "/playerData.dat").toPath());
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.err.println("There is an error with savePlayerData");
//        }
//        CharacterManager.get().updateData(this);
    }

    public CompoundTag getPlayerData() {
        try {
            Path path = new File(FMLPaths.GAMEDIR.get() + "/characters/" + this.getUUID().toString() + "/playerData.dat").toPath();
            return NbtIo.readCompressed(path, NbtAccounter.unlimitedHeap());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("There is an error with getPlayerData");
        }
        return null;
    }
}
