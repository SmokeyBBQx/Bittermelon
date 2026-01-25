package com.site21.bittermelon.common.systems.character;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.common.systems.character.skills.Skill;
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
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class Character {
    public static final Codec<Character> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, Character> STREAM_CODEC;

    private final UUID uuid;
    private final UUID entityUUID;
    private String name;
    private String description = "";
    private int emoteColor;
    private final EnumMap<Skill, Float> skills;
    private int willpower;
    private PlayerInfo playerInfo;

    /** Full constructor for deserialization purposes.
     * Use other constructors for creating new characters.
     */
    public Character(UUID uuid, UUID entityUUID, String name, String description, int emoteColor, EnumMap<Skill, Float> skills, int willpower) {
        this.uuid = uuid;
        this.entityUUID = entityUUID;
        this.name = name;
        this.description = description;
        this.emoteColor = emoteColor;
        this.skills = skills;
        this.willpower = willpower;
    }

    /** Creates a new character with default anatomyType (human) and random emote color. */
    public Character(UUID entityUUID, String name) {
        this.uuid = UUID.randomUUID();
        this.entityUUID = entityUUID;
        this.name = name;

        emoteColor = (int) (Math.random() * 0xFFFFFF);
        skills = new EnumMap<>(Skill.class);
        willpower = 6;
    }

    /** Creates a new character with default anatomy (human) and specified emote color as hex string (e.g. "FF5733"). */
    public Character(UUID entityUUID, String name, String description, String emoteColor) {
        this(entityUUID, name);
        this.description = description;
        this.emoteColor = TextColor.parseColor("#" + emoteColor).getOrThrow().getValue();
    }

    /** Creates a new character with default anatomy (human) and specified emote color as integer. */
    public Character(UUID entityUUID, String name, String description, int emoteColor) {
        this(entityUUID, name);
        this.description = description;
        this.emoteColor = emoteColor;
    }

    public UUID getId() {
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

    public Optional<PlayerInfo> getPlayerInfo() {
        return Optional.ofNullable(playerInfo);
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

    public void setPlayerInfo(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

//    /**
//     *  Gets the character's medical stats.
//     * @return The character's medical stats. If not set, returns default human anatomy with O- blood type.
//     */
//    public MedicalStats getMedicalStats() {
//        if (medicalStats == null) {
//            medicalStats = AnatomyType.HUMAN.getFactory().build(BloodType.O_MINUS, this);
//        }
//        return medicalStats;
//    }

    /**
     * Updates the character's medical stats. Should be called periodically, e.g. each server tick.
     * @param level The current game level.
     */
    public void update(@NotNull Level level) {
        if (level.isClientSide) return;
    }

    public EnumMap<Skill, Float> getSkills() {
        return skills;
    }

    public float getSkill(Skill skill) {
        return skills.getOrDefault(skill, 1f);
    }

    /**
     * Modifies the character's skill level by the specified amount, ensuring it does not exceed the skill's maximum level.
     * @param skill The skill to modify.
     * @param amount The amount to modify the skill by (can be positive or negative).
     */
    public void modifySkill(Skill skill, float amount) {
        skills.compute(skill, (k, v) -> Math.min(v == null ? 1 + amount : v + amount, skill.getMaxLevel()));
    }

    public int getWillpower() {
        return willpower;
    }

    public void setWillpower(int willpower) {
        this.willpower = willpower;
    }

    /**
     * Saves the character's persistent data to a file in the world's "characterdata" directory.
     * @param data The NBT data to save.
     * @param level The server level (world) to save the data in.
     */
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

    /**
     * Loads the character's persistent data from a file in the world's "characterdata" directory.
     * @param level The server level (world) to load the data from.
     * @return The loaded NBT data, or an empty CompoundTag if loading failed or the file does not exist.
     */
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
                UUIDUtil.CODEC.fieldOf("uuid").forGetter(Character::getId),
                UUIDUtil.CODEC.fieldOf("entityUUID").forGetter(Character::getEntityUUID),
                Codec.STRING.fieldOf("name").forGetter(Character::getName),
                Codec.STRING.fieldOf("description").forGetter(Character::getDescription),
                Codec.INT.fieldOf("emoteColor").forGetter(Character::getEmoteColor),
                Codec.unboundedMap(Skill.CODEC, Codec.FLOAT).fieldOf("skills").forGetter(Character::getSkills),
                Codec.INT.fieldOf("willpower").forGetter(Character::getWillpower),
                PlayerInfo.CODEC.optionalFieldOf("playerInfo").forGetter(Character::getPlayerInfo)
        ).apply(instance, (uuid, entityUUID, name, description, emoteColor,
                           skills, willpower, playerInfo) -> {
            EnumMap<Skill, Float> skillMap = new EnumMap<>(Skill.class);
            Character character = new Character(uuid, entityUUID, name, description, emoteColor, skillMap, willpower);
            playerInfo.ifPresent(character::setPlayerInfo);
            return character;
        }));

        STREAM_CODEC = StreamCodec.of(
                (buf, character) -> {
                    UUIDUtil.STREAM_CODEC.encode(buf, character.getId());
                    UUIDUtil.STREAM_CODEC.encode(buf, character.getEntityUUID());
                    ByteBufCodecs.STRING_UTF8.encode(buf, character.getName());
                    ByteBufCodecs.STRING_UTF8.encode(buf, character.getDescription());
                    ByteBufCodecs.INT.encode(buf, character.getEmoteColor());
                    buf.writeMap(character.getSkills(),
                            FriendlyByteBuf::writeEnum,
                            FriendlyByteBuf::writeFloat
                    );
                    ByteBufCodecs.INT.encode(buf, character.getWillpower());
                    PlayerInfo.STREAM_CODEC.apply(ByteBufCodecs::optional).encode(buf, character.getPlayerInfo());
                },
                (buf) -> {
                    UUID uuid = UUIDUtil.STREAM_CODEC.decode(buf);
                    UUID entityUUID = UUIDUtil.STREAM_CODEC.decode(buf);
                    String name = ByteBufCodecs.STRING_UTF8.decode(buf);
                    String description = ByteBufCodecs.STRING_UTF8.decode(buf);
                    int emoteColor = ByteBufCodecs.INT.decode(buf);
                    EnumMap<Skill, Float> skills = new EnumMap<>(Skill.class);
                    Map<Skill, Float> tempMap = buf.readMap(
                            byteBuf -> byteBuf.readEnum(Skill.class),
                            FriendlyByteBuf::readFloat
                    );
                    skills.putAll(tempMap);
                    int willpower = ByteBufCodecs.INT.decode(buf);
                    Optional<PlayerInfo> playerInfo = PlayerInfo.STREAM_CODEC.apply(ByteBufCodecs::optional).decode(buf);

                    Character character = new Character(uuid, entityUUID, name, description, emoteColor, skills, willpower);
                    playerInfo.ifPresent(character::setPlayerInfo);
                    return character;
                }
        );
    }
}
