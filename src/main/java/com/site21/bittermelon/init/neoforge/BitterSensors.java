package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.ai.sensors.ObserversSensor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class BitterSensors {
    public static final DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(BuiltInRegistries.SENSOR_TYPE, Bittermelon.MOD_ID);

    public static final DeferredHolder<SensorType<?>, SensorType<ObserversSensor<? extends Mob>>> OBSERVERS = SENSOR_TYPES.register(
            "observers",
            () -> new SensorType<>(ObserversSensor::new)
    );
}
