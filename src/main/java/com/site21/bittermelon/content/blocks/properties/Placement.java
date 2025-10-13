package com.site21.bittermelon.content.blocks.properties;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum Placement implements StringRepresentable {
    LEFT("left"),
    RIGHT("right");

    private final String name;

    Placement(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
