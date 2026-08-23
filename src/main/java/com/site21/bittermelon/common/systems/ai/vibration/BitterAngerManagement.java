package com.site21.bittermelon.common.systems.ai.vibration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BitterAngerManagement {
    private static final int MAX_ANGER = 150;
    private static final int ANGER_DECAY = 1;

    public static final Codec<BitterAngerManagement> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.unboundedMap(UUIDUtil.CODEC, ExtraCodecs.NON_NEGATIVE_INT)
                            .fieldOf("subjects")
                            .forGetter(management -> management.subjects)
            ).apply(instance, BitterAngerManagement::new)
    );

    private final Map<UUID, Integer> subjects = new HashMap<>();

    public BitterAngerManagement(Map<UUID, Integer> subjects) {
        this.subjects.putAll(subjects);
    }

    public BitterAngerManagement() {}

    public void increaseAnger(Entity entity, int amount) {
        UUID uuid = entity.getUUID();
        int current = subjects.getOrDefault(uuid, 0);
        subjects.put(uuid, Math.min(MAX_ANGER, current + amount));
    }

    public void clearAnger(Entity entity) {
        subjects.remove(entity.getUUID());
    }

    public int getAnger(Entity entity) {
        return subjects.getOrDefault(entity.getUUID(), 0);
    }

    public void tick(ServerLevel level) {
        subjects.entrySet().removeIf(entry -> {
            int newAnger = entry.getValue() - ANGER_DECAY;
            if (newAnger <= 0) return true;
            entry.setValue(newAnger);
            return false;
        });
    }

    public @Nullable Entity getTopTarget(ServerLevel level) {
        UUID topUUID = null;
        int maxAnger = 0;

        for (Map.Entry<UUID, Integer> entry : subjects.entrySet()) {
            if (entry.getValue() > maxAnger) {
                maxAnger = entry.getValue();
                topUUID = entry.getKey();
            }
        }

        if (topUUID == null) return null;
        return level.getEntity(topUUID);
    }

    public int getHighestAnger(ServerLevel level) {
        int maxAnger = 0;
        for (Map.Entry<UUID, Integer> entry : subjects.entrySet()) {
            if (entry.getValue() > maxAnger) {
                maxAnger = entry.getValue();
            }
        }
        return maxAnger;
    }
}
