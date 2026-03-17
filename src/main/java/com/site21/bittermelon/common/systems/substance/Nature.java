package com.site21.bittermelon.common.systems.substance;

import net.minecraft.util.StringRepresentable;

public enum Nature implements StringRepresentable {
    STRONG_ACID,
    WEAK_ACID,
    BASE,
    COMBUSTIBLE,
    WATER_BASED;

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }

    public static final EnumCodec<Nature> CODEC = StringRepresentable.fromEnum(Nature::values);
}
