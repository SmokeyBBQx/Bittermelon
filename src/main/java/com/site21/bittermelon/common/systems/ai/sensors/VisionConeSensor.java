package com.site21.bittermelon.common.systems.ai.sensors;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;

import java.util.Set;

public class VisionConeSensor<E extends LivingEntity> extends Sensor<E> {
    @Override
    protected void doTick(ServerLevel level, E body) {

    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return Set.of();
    }
}
