package com.site21.bittermelon.content.character;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.content.character.skills.Skill;
import com.site21.bittermelon.content.medical.blood.BloodType;
import com.site21.bittermelon.content.medical.factory.Anatomy;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelResource;
import net.neoforged.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

public class Character {
    public static final Codec<Character> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, Character> STREAM_CODEC;

    private final UUID uuid;
    private final UUID entityUUID;
    private String name;
    private String description = "";
    private int emoteColor;
    private MedicalStats medicalStats;
    private final EnumMap<Skill, Float> skills;
    private int willpower;

    public Character(UUID uuid, UUID entityUUID, String name, String description, int emoteColor, MedicalStats medicalStats, EnumMap<Skill, Float> skills, int willpower) {
        this.uuid = uuid;
        this.entityUUID = entityUUID;
        this.name = name;
        this.description = description;
        this.emoteColor = emoteColor;
        this.medicalStats = medicalStats;
        this.skills = skills;
        this.willpower = willpower;
    }

    public Character(UUID entityUUID, String name, @NotNull Anatomy anatomy) {
        this.uuid = UUID.randomUUID();
        this.entityUUID = entityUUID;
        this.name = name;

        emoteColor = (int) (Math.random() * 0xFFFFFF);
        medicalStats = anatomy.getFactory().build(BloodType.O_MINUS, this);
        skills = new EnumMap<>(Skill.class);
        willpower = 6;
    }

    public Character(UUID entityUUID, String name, String description, String emoteColor) {
        this(entityUUID, name, Anatomy.HUMAN);
        this.description = description;
        this.emoteColor = TextColor.parseColor("#" + emoteColor).getOrThrow().getValue();
    }

    public Character(UUID entityUUID, String name, String description, int emoteColor) {
        this(entityUUID, name, Anatomy.HUMAN);
        this.description = description;
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

    public int getEmoteColor() {
        return emoteColor;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEmoteColor(int emoteColor) {
        this.emoteColor = emoteColor;
    }

    public void setEmoteColor(String emoteColor) {
        this.emoteColor = TextColor.parseColor(emoteColor).getOrThrow().getValue();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMedicalStats(MedicalStats medicalStats) {
        this.medicalStats = medicalStats;
    }

    public MedicalStats getMedicalStats() {
        if (medicalStats == null) {
            return Anatomy.HUMAN.getFactory().build(BloodType.O_MINUS, this);
        }
        return medicalStats;
    }

    public void update(Level level) {
        if (medicalStats != null) {
            medicalStats.update(level);
        } else {
            System.out.println("Medical stats null for " + name);
        }
    }

    public EnumMap<Skill, Float> getSkills() {
        return skills;
    }

    public float getSkill(Skill skill) {
        return skills.getOrDefault(skill, 1f);
    }

    public void modifySkill(Skill skill, float amount) {
        skills.compute(skill, (k, v) -> Math.min(v == null ? 1 + amount : v + amount, skill.getMaxLevel()));
    }

    public int getWillpower() {
        return willpower;
    }

    public void setWillpower(int willpower) {
        this.willpower = willpower;
    }

    public void savePlayerData(CompoundTag data, @NotNull ServerLevel level) {
        try {
            Path dataDir = level.getServer().getWorldPath(LevelResource.ROOT).resolve("characterdata");
            Files.createDirectories(dataDir);

            Path playerDataFile = dataDir.resolve(uuid + ".dat");
            NbtIo.writeCompressed(data, playerDataFile);
        } catch (IOException e) {
            System.err.println("Failed to save player data for " + getName() + ": " + e.getMessage());
        }
    }

    public CompoundTag getPlayerData(@NotNull ServerLevel level) {
        try {
            Path worldPath = level.getServer().getWorldPath(LevelResource.ROOT);
            Path playerDataFile = worldPath.resolve("characterdata").resolve(uuid + ".dat");

            if (!Files.exists(playerDataFile)) {
                return new CompoundTag();
            }

            return NbtIo.readCompressed(playerDataFile, NbtAccounter.unlimitedHeap());
        } catch (IOException e) {
            System.err.println("Failed to load player data for " + this.getName() + ": " + e.getMessage());
            return new CompoundTag();
        }
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                UUIDUtil.CODEC.fieldOf("uuid").forGetter(Character::getUUID),
                UUIDUtil.CODEC.fieldOf("entityUUID").forGetter(Character::getEntityUUID),
                Codec.STRING.fieldOf("name").forGetter(Character::getName),
                Codec.STRING.fieldOf("description").forGetter(Character::getDescription),
                Codec.INT.fieldOf("emoteColor").forGetter(Character::getEmoteColor),
                MedicalStats.CODEC.fieldOf("medicalStats").forGetter(Character::getMedicalStats),
                Codec.unboundedMap(Skill.CODEC, Codec.FLOAT).fieldOf("skills").forGetter(Character::getSkills),
                Codec.INT.fieldOf("willpower").forGetter(Character::getWillpower)
        ).apply(instance, (uuid, entityUUID, name, description, emoteColor,
                           medicalStats, skills, willpower) -> {
            EnumMap<Skill, Float> skillMap = new EnumMap<>(Skill.class);
            return new Character(uuid, entityUUID, name, description, emoteColor, medicalStats, skillMap, willpower);
        }));

        STREAM_CODEC = StreamCodec.of(
                (buf, character) -> {
                    UUIDUtil.STREAM_CODEC.encode(buf, character.getUUID());
                    UUIDUtil.STREAM_CODEC.encode(buf, character.getEntityUUID());
                    ByteBufCodecs.STRING_UTF8.encode(buf, character.getName());
                    ByteBufCodecs.STRING_UTF8.encode(buf, character.getDescription());
                    ByteBufCodecs.INT.encode(buf, character.getEmoteColor());
                    MedicalStats.STREAM_CODEC.encode(buf, character.getMedicalStats());
                    buf.writeMap(character.getSkills(),
                            FriendlyByteBuf::writeEnum,
                            FriendlyByteBuf::writeFloat
                    );
                    ByteBufCodecs.INT.encode(buf, character.getWillpower());
                },
                (buf) -> {
                    UUID uuid = UUIDUtil.STREAM_CODEC.decode(buf);
                    UUID entityUUID = UUIDUtil.STREAM_CODEC.decode(buf);
                    String name = ByteBufCodecs.STRING_UTF8.decode(buf);
                    String description = ByteBufCodecs.STRING_UTF8.decode(buf);
                    int emoteColor = ByteBufCodecs.INT.decode(buf);
                    MedicalStats medicalStats = MedicalStats.STREAM_CODEC.decode(buf);

                    EnumMap<Skill, Float> skills = new EnumMap<>(Skill.class);
                    Map<Skill, Float> tempMap = buf.readMap(
                            byteBuf -> byteBuf.readEnum(Skill.class),
                            FriendlyByteBuf::readFloat
                    );
                    skills.putAll(tempMap);
                    int willpower = ByteBufCodecs.INT.decode(buf);

                    return new Character(uuid, entityUUID, name, description, emoteColor, medicalStats, skills, willpower);
                }
        );
    }
}
