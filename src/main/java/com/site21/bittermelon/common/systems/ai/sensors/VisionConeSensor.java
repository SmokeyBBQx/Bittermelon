package com.site21.bittermelon.common.systems.ai.sensors;

import com.site21.bittermelon.init.neoforge.BitterMemoryTypes;
import com.site21.bittermelon.init.neoforge.BitterSensors;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.base.NearestVisibleEntityFilteredSensor;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

public class VisionConeSensor<E extends LivingEntity> extends NearestVisibleEntityFilteredSensor<E, List<LivingEntity>> {
    public final Supplier<Double> coneAngle;

    public VisionConeSensor(Supplier<Double> coneAngle) {
        this.coneAngle = coneAngle;
    }

    @Override
    protected MemoryModuleType<List<LivingEntity>> getMemory() {
        return BitterMemoryTypes.VISION_CONE_LIVING_ENTITIES.get();
    }

    @Override
    protected BiPredicate<E, LivingEntity> predicate() {
        return (entity, target) -> {
            Vec3 toTarget = target.getEyePosition().subtract(entity.getEyePosition());
            double dot = toTarget.dot(entity.getLookAngle());
            if (dot < 0) return false;
            double coneCos = Math.cos(Math.toRadians(coneAngle.get() / 2.0));
            return dot * dot >= coneCos * coneCos * toTarget.lengthSqr();
        };
    }

    @Override
    protected @Nullable List<LivingEntity> findMatches(E entity, NearestVisibleLivingEntities matcher) {
        List<LivingEntity> matches = matcher.find(target -> predicate().test(entity, target)).toList();
        return matches.isEmpty() ? null : matches;
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return BitterSensors.VISION_CONE.get();
    }
}
