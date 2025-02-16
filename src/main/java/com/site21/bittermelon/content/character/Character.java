package com.site21.bittermelon.content.character;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.content.medical.blood.BloodType;
import com.site21.bittermelon.content.medical.factory.Anatomy;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.TextColor;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.UUID;

public class Character {
    public static final Codec<Character> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("uuid").forGetter(Character::getUUID),
            UUIDUtil.CODEC.fieldOf("entityUUID").forGetter(Character::getEntityUUID),
            Codec.STRING.fieldOf("name").forGetter(Character::getName),
            Codec.STRING.fieldOf("description").forGetter(Character::getDescription),
            Codec.INT.fieldOf("emoteColor").forGetter(Character::getEmoteColor)
    ).apply(instance, (uuid, entityUUID, name, description, emoteColor) -> {
        Character character = new Character(entityUUID, name, Anatomy.HUMAN);
        character.setDescription(description);
        character.setEmoteColor(emoteColor);
        return character;
    }));

    private final UUID uuid;
    private final UUID entityUUID;
    private String name;
    private String description = "";
    private int emoteColor;
    private transient final MedicalStats medicalStats;
    private final EnumMap<Skills, Float> skills;

    public Character(UUID entityUUID, String name, @NotNull Anatomy anatomy) {
        this.uuid = UUID.randomUUID();
        this.entityUUID = entityUUID;
        this.name = name;

        emoteColor = (int) (Math.random() * 0xFFFFFF);
        medicalStats = anatomy.getFactory().build(BloodType.O_MINUS, this);
        skills = new EnumMap<>(Skills.class);
    }

    public Character(UUID entityUUID, String name, String description, String emoteColor) {
        this(entityUUID, name, Anatomy.HUMAN);
        this.description = description;
        this.emoteColor = TextColor.parseColor("#" + emoteColor).getOrThrow().getValue();
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

    public MedicalStats getMedicalStats() {
        if (medicalStats == null) {
            return Anatomy.HUMAN.getFactory().build(BloodType.O_MINUS, this);
        }
        return medicalStats;
    }

    public void update() {
        if (medicalStats != null) {
            medicalStats.update();
        }
    }

    public float getSkill(Skills skill) {
        return skills.getOrDefault(skill, 0f);
    }

    public float modifySkill(Skills skill, float amount) {
        return skills.merge(skill, amount, Float::sum);
    }
}
