package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.entities.ai.sensors.NearbyDrinkableFluidsSensor;
import com.site21.bittermelon.content.entities.ai.sensors.NearbyFoodSensor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class BitterSensors {
    public static final DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(BuiltInRegistries.SENSOR_TYPE, Bittermelon.MOD_ID);

    public static final DeferredHolder<SensorType<?>, SensorType<NearbyFoodSensor<?>>> NEARBY_EDIBLE_ITEMS = SENSOR_TYPES.register(
            "nearby_edible_items",
            () -> new SensorType<>(NearbyFoodSensor::new)
    );

    public static final DeferredHolder<SensorType<?>, SensorType<NearbyDrinkableFluidsSensor<?>>> NEARBY_DRINKABLE_FLUIDS = SENSOR_TYPES.register(
            "nearby_drinkable_fluids",
            () -> new SensorType<>(NearbyDrinkableFluidsSensor::new)
    );
}
