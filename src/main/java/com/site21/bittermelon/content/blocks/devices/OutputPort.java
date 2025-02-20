package com.site21.bittermelon.content.blocks.devices;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class OutputPort<T> {
    private final String id;
    private T value;
    private final T defaultValue;

    public OutputPort(String id, T defaultValue) {
        this.id = id;
        this.defaultValue = defaultValue;
    }

    public void save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        if (value instanceof Float f) {
            tag.putFloat("value", f);
        } else if (value instanceof Integer i) {
            tag.putInt("value", i);
        } else if (value instanceof Boolean b) {
            tag.putBoolean("value", b);
        } else if (value instanceof String s) {
            tag.putString("value", s);
        }
    }

    public void load(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        if (defaultValue instanceof Float) {
            value = (T) Float.valueOf(tag.getFloat("value"));
        } else if (defaultValue instanceof Integer) {
            value = (T) Integer.valueOf(tag.getInt("value"));
        } else if (defaultValue instanceof Boolean) {
            value = (T) Boolean.valueOf(tag.getBoolean("value"));
        } else if (defaultValue instanceof String) {
            value = (T) tag.getString("value");
        }
    }
}
