package com.site21.bittermelon.content.character.skills;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum Skill implements StringRepresentable {
    STRENGTH("strength", 2),
    AGILITY("agility", 2),
    TOUGHNESS("toughness", 2),
    MELEE("melee", 2),
    MARKSMANSHIP("marksmanship", 2),
    THROWING("throwing", 2),
    SURGICAL("surgical", 2),
    COOKING("cooking", 2),
    FORTITUDE("fortitude", 2),
    BRAVERY("bravery", 2);

    private final String name;
    private final int maxLevel;

    Skill(String name, int maxLevel) {
        this.name = name;
        this.maxLevel = maxLevel;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public static final EnumCodec<Skill> CODEC = StringRepresentable.fromEnum(Skill::values);
}
