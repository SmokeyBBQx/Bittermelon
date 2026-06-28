package com.site21.bittermelon.common.systems.ai.sensors;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.base.PredicateSensor;
import net.tslat.smartbrainlib.library.object.FixedNearestVisibleLivingEntities;
import net.tslat.smartbrainlib.library.object.SquareRadius;
import net.tslat.smartbrainlib.util.BrainUtil;
import net.tslat.smartbrainlib.util.EntityRetrievalUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class VisionConeLivingEntitySensor<E extends LivingEntity> extends PredicateSensor<E, LivingEntity> {
    private static final List<MemoryModuleType<?>> MEMORIES = ObjectArrayList.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);

    @Nullable
    protected SquareRadius radius = null;
    protected float coneAngle = 60.0f;

    @Override
    public List<MemoryModuleType<?>> memoriesUsed() {
        return MEMORIES;
    }

    @Override
    public SensorType<? extends ExtendedSensor<?>> type() {
        return null;
    }

    @Override
    protected void doTick(ServerLevel level, E entity) {
        SquareRadius radius = this.radius;

        if (radius == null) {
            double dist = entity.getAttributeValue(Attributes.FOLLOW_RANGE);

            radius = new SquareRadius(dist, dist);
        }

        List<LivingEntity> entities = EntityRetrievalUtil.getEntities(entity, radius.xzRadius(), radius.yRadius(),
                radius.xzRadius(), LivingEntity.class, livingEntity -> predicate().test(entity, livingEntity));

        Predicate<LivingEntity> conePredicate = target -> isInVisionCone(entity, target);
        entities.removeIf(conePredicate.negate());

        entities.sort(Comparator.comparingDouble(entity::distanceToSqr));

        BrainUtil.setMemory(entity, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
                new FixedNearestVisibleLivingEntities(entity, entities));
    }

    private boolean isInVisionCone(@NotNull E entity, @NotNull LivingEntity target) {
        Vec3 lookVec = entity.getLookAngle().normalize();

        Vec3 targetVec = new Vec3(
                target.getX() - entity.getX(),
                target.getY() - entity.getY(),
                target.getZ() - entity.getZ()
        ).normalize();

        double dotProduct = lookVec.dot(targetVec);

        double halfAngleRadians = Math.toRadians(coneAngle / 2);
        double minCosine = Math.cos(halfAngleRadians);

        return dotProduct > minCosine;
    }
}
