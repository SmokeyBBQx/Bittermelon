package com.site21.bittermelon.client.render;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

import static com.site21.bittermelon.init.neoforge.BitterEntities.SCP_1507;

public final class SleepTransforms {
    private static final Map<EntityType<?>, SleepTransform> TRANSFORMS = new HashMap<>();

    public static void register(EntityType<?> type, SleepTransform transform) {
        TRANSFORMS.put(type, transform);
    }

    public static void init() {
        register(SCP_1507.get(), new SleepTransforms.SleepTransform(90.0f, new Vec3(0.2, 0.0, 0)));
        register(EntityType.PIG, new SleepTransforms.SleepTransform(90.0f, new Vec3(0.2, 0.0, 0)));
        register(EntityType.FOX, new SleepTransforms.SleepTransform(75.0f, new Vec3(0.175, 0.0, 0)));
    }

    public static SleepTransform get(EntityType<?> type) {
        return TRANSFORMS.getOrDefault(type, SleepTransform.DEFAULT);
    }

    public record SleepTransform(float rollDegrees, Vec3 offset) {
        public static final SleepTransform DEFAULT = new SleepTransform(0.0f, new Vec3(0.0f, 0.0f, 0.0f));
    }
}
