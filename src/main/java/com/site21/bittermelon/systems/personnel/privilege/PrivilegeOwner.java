package com.site21.bittermelon.systems.personnel.privilege;

import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface PrivilegeOwner {
    Map<String, Boolean> getPrivileges();

    String getName();

    default boolean canAccess() {
        return true;
    }

    default boolean hasPermission(@NotNull Map<String, Boolean> privileges, @NotNull Map<String, Boolean> requiredPrivileges) {
        return privileges.keySet().stream().anyMatch(requiredPrivileges.keySet()::contains);
    }

    default void serializePrivileges(@NotNull ValueOutput output) {
        ValueOutput.TypedOutputList<CompoundTag> list = output.list("privileges", CompoundTag.CODEC);

        for (Map.Entry<String, Boolean> entry : getPrivileges().entrySet()) {
            CompoundTag tag = new CompoundTag();
            tag.putString("id", entry.getKey());
            tag.putBoolean("value", entry.getValue());
            list.add(tag);
        }
    }

    default void deserializePrivileges(@NotNull ValueInput input) {
        getPrivileges().clear();
        ValueInput.TypedInputList<CompoundTag> list = input.list("privileges", CompoundTag.CODEC).orElseThrow();

        for (CompoundTag tag : list) {
            tag.getString("id").ifPresent(id ->
                    getPrivileges().put(id, tag.getBooleanOr("value", false))
            );
        }
    }
}
