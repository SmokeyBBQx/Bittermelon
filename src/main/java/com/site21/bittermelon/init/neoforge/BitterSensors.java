package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.ai.sensors.NearbyDrinkableFluidsSensor;
import com.site21.bittermelon.common.systems.ai.sensors.NearbyFoodSensor;
import com.site21.bittermelon.common.systems.ai.sensors.ObserversSensor;
import com.site21.bittermelon.common.systems.ai.sensors.VisionConeLivingEntitySensor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Mob;
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

    public static final DeferredHolder<SensorType<?>, SensorType<VisionConeLivingEntitySensor<?>>> VISION_CONE_LIVING_ENTITIES = SENSOR_TYPES.register(
            "vision_cone_living_entities",
            () -> new SensorType<>(VisionConeLivingEntitySensor::new)
    );

    public static final DeferredHolder<SensorType<?>, SensorType<ObserversSensor<? extends Mob>>> OBSERVERS = SENSOR_TYPES.register(
            "observers",
            () -> new SensorType<>(ObserversSensor::new)
    );
}
