package com.site21.bittermelon.content.character;

import com.site21.bittermelon.content.character.skills.Skill;
import com.site21.bittermelon.content.medical.factory.Anatomy;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.neoforged.fml.loading.FMLPaths;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.UUID;

public class PlayerCharacter extends Character {

    public PlayerCharacter(UUID uuid, UUID entityUUID, String name, String description, int emoteColor,
                           MedicalStats medicalStats, EnumMap<Skill, Float> skills) {
        super(uuid, entityUUID, name, description, emoteColor, medicalStats, skills);
    }


}
