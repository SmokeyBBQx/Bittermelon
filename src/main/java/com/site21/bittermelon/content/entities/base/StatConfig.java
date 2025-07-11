package com.site21.bittermelon.content.entities.base;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record StatConfig(float decayRate, EntityDataAccessor<Float> accessor) {
    @Contract("_, _ -> new")
    public static @NotNull StatConfig of(float decayRate, @NotNull Entity entity) {
        return new StatConfig(decayRate, SynchedEntityData.defineId(entity.getClass(), EntityDataSerializers.FLOAT));
    }
}
