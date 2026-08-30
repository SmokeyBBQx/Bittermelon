package com.site21.bittermelon.common.systems.ai.sensors;

import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.util.SensoryUtil;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Extends NearestVisibleLivingEntities to filter entities based on vision cone
 */
public class NearestVisionConeEntities extends NearestVisibleLivingEntities {
    public final Supplier<Double> coneAngle;

    public NearestVisionConeEntities(Supplier<Double> coneAngle) {
        super();
        this.coneAngle = coneAngle;
    }

    public NearestVisionConeEntities(LivingEntity entity, List<LivingEntity> entities, Supplier<Double> coneAngle) {
        super();
        this.coneAngle = coneAngle;
        this.nearbyEntities = entities;
        this.lineOfSightTest = new Predicate<>() {
            final Object2BooleanOpenHashMap<LivingEntity> cache = new Object2BooleanOpenHashMap<>(entities.size());

            @Override
            public boolean test(LivingEntity target) {
                return this.cache.computeIfAbsent(target, (Predicate<LivingEntity>)target1 ->
                        SensoryUtil.isEntityTargetable(entity, target1) && isWithinCone(entity, target));
            }
        };
    }

    private boolean isWithinCone(LivingEntity entity, LivingEntity target) {
        if (target.isInvisible()) return false;

        Vec3 toTarget = target.getEyePosition().subtract(entity.getEyePosition());
        double dot = toTarget.dot(entity.getLookAngle());
        if (dot < 0) return false;
        double coneCos = Math.cos(Math.toRadians(coneAngle.get() / 2.0));
        return dot * dot >= coneCos * coneCos * toTarget.lengthSqr();
    }
}
