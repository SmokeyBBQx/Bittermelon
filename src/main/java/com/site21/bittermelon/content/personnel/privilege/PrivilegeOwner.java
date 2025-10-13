package com.site21.bittermelon.content.personnel.privilege;

import net.minecraft.nbt.CompoundTag;
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

    default void serializePrivileges(CompoundTag tag) {
        CompoundTag privilegesTag = new CompoundTag();
        for (Map.Entry<String, Boolean> entry : getPrivileges().entrySet()) {
            privilegesTag.putBoolean(entry.getKey(), entry.getValue());
        }

        tag.put("privileges", privilegesTag);
    }

    default void deserializePrivileges(@NotNull CompoundTag tag) {
        CompoundTag privilegesTag = tag.getCompound("privileges");
        getPrivileges().clear();

        for (String key : privilegesTag.getAllKeys()) {
            getPrivileges().put(key, privilegesTag.getBoolean(key));
        }
    }
}
