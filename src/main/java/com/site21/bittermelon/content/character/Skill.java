package com.site21.bittermelon.content.character;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum Skill implements StringRepresentable {
    STRENGTH("strength"),
    AGILITY("agility"),
    TOUGHNESS("toughness"),
    MELEE("melee"),
    MARKSMANSHIP("marksmanship"),
    THROWING("throwing"),
    SURGICAL("surgical"),
    COOKING("cooking"),
    FORTITUDE("fortitude");

    private final String name;

    Skill(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }

    public static final EnumCodec<Skill> CODEC = StringRepresentable.fromEnum(Skill::values);
}
