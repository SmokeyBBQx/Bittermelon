package com.site21.bittermelon.entities.behavior.needs;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class ReevaluateDecision<E extends LivingEntity & NeedsUser> extends ExtendedBehaviour<E> {
    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of();
    }

    @Override
    protected void start(E entity) {
        List<Need> needs = entity.getNeeds();

        Need highestPriorityNeed = needs.stream()
                .max((n1, n2) -> {
                    float value1 = entity.getEntityData().get(n1.need());
                    float value2 = entity.getEntityData().get(n2.need());
                    float priority1 = n1.priorityFunction().apply(value1);
                    float priority2 = n2.priorityFunction().apply(value2);
                    return Float.compare(priority1, priority2);
                })
                .orElseThrow(() -> new IllegalStateException("No needs found for " + entity));

        float currentValue = entity.getEntityData().get(highestPriorityNeed.need());
        float priorityScore = highestPriorityNeed.priorityFunction().apply(currentValue);

        entity.getBrain().setActiveActivityIfPossible(highestPriorityNeed.activity());
    }
}
