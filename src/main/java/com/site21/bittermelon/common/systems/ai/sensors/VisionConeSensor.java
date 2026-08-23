package com.site21.bittermelon.common.systems.ai.sensors;

import com.site21.bittermelon.init.neoforge.BitterSensors;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.library.object.SquareRadius;
import net.tslat.smartbrainlib.util.BrainUtil;
import net.tslat.smartbrainlib.util.EntityRetrievalUtil;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

/**
 * Extends NearbyLivingEntitySensor to filter NEAREST_VISIBLE_LIVING_ENTITIES based on vision cone
 * @param <E> The brain owner entity
 */
public class VisionConeSensor<E extends LivingEntity> extends NearbyLivingEntitySensor<E> {
    protected static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList.of(MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
    public final Supplier<Double> coneAngle;

    public VisionConeSensor(Supplier<Double> coneAngle) {
        this.coneAngle = coneAngle;
    }

    public VisionConeSensor() {
        this(() -> 120.0);
    }

    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return MEMORIES;
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
    public SensorType<? extends ExtendedSensor<?>> type() {
        return BitterSensors.VISION_CONE.get();
    }

    @Override
    protected void doTick(ServerLevel level, E entity) {
        final SquareRadius radius = this.radius.apply(entity);
        final List<LivingEntity> entities = EntityRetrievalUtil.getEntities(entity, radius.xzRadius(), radius.yRadius(), radius.xzRadius(), LivingEntity.class, nearby -> predicate().test(entity, nearby));

        entities.sort(Comparator.comparingDouble(entity::distanceToSqr));

        BrainUtil.setMemory(entity, MemoryModuleType.NEAREST_LIVING_ENTITIES, entities);
        BrainUtil.setMemory(entity, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
                new NearestVisionConeEntities(entity, entities, coneAngle));
    }
}
