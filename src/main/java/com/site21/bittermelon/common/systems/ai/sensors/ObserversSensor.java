package com.site21.bittermelon.common.systems.ai.sensors;

import com.site21.bittermelon.init.neoforge.BitterSensors;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.util.BrainUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.site21.bittermelon.init.neoforge.BitterMemoryTypes.OBSERVERS;

public class ObserversSensor<E extends Mob> extends ExtendedSensor<E> {
    private static final double MAX_DISTANCE = 32.0;
    private static final double ANGLE_THRESHOLD = 0.65;

    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return ObjectArrayList.of(OBSERVERS.get());
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return BitterSensors.OBSERVERS.get();
    }

    @Override
    protected void doTick(@NotNull ServerLevel level, @NotNull E entity) {
        if (level.isClientSide) return;
        List<LivingEntity> observers = new ArrayList<>();

        // Look for entities within the max distance range of the entity.
        List<LivingEntity> potentialObservers = level.getEntitiesOfClass(LivingEntity.class,
                entity.getBoundingBox().inflate(MAX_DISTANCE));

        // Determine whether the potential observer is looking at the entity.
        for (LivingEntity potentialObserver : potentialObservers) {
            if (isLookingAt(potentialObserver, entity)) observers.add(potentialObserver);
        }

        if (!observers.isEmpty()) {
            entity.getBrain().setMemory(OBSERVERS.get(), observers);
        } else {
            BrainUtil.clearMemory(entity, OBSERVERS.get());
        }
    }

    private boolean isLookingAt(@NotNull LivingEntity observer, @NotNull E target) {
        Vec3 observerPos = observer.getEyePosition();
        // Get middle of target's model.
        Vec3 targetPos = target.position().add(0, target.getBbHeight() / 2, 0);

        Vec3 lookDirection;
        if (observer instanceof Player player) {
            lookDirection = player.getViewVector(1.0F);
        } else {
            lookDirection = observer.getLookAngle();
        }

        // Find dot to compare view vector alignment with target position.
        Vec3 toTarget = targetPos.subtract(observerPos).normalize();
        double dot = lookDirection.dot(toTarget);

        if (dot < ANGLE_THRESHOLD) {
            return false;
        }

        return observer.hasLineOfSight(target);
    }
}
