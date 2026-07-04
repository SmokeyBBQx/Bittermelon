package com.site21.bittermelon.client.render;

import net.minecraft.world.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

import static com.site21.bittermelon.init.neoforge.BitterEntities.SCP_1507;

public final class SleepRotations {
    private static final Map<EntityType<?>, SleepTransform> TRANSFORMS = new HashMap<>();

    public static void register(EntityType<?> type, SleepTransform transform) {
        TRANSFORMS.put(type, transform);
    }

    public static void init() {
        register(SCP_1507.get(), new SleepRotations.SleepTransform(90.0f, 0.05f));
        register(EntityType.PIG, new SleepRotations.SleepTransform(90.0f, 0.05f));
        register(EntityType.FOX, new SleepRotations.SleepTransform(75.0f, 0.0f));
    }

    public static SleepTransform get(EntityType<?> type) {
        return TRANSFORMS.getOrDefault(type, SleepTransform.DEFAULT);
    }

    public record SleepTransform(float rollDegrees, float yOffset) {
        public static final SleepTransform DEFAULT = new SleepTransform(0.0f, 0.0f);
    }
}
