package com.site21.bittermelon.entities.ai.behavior.needs;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReevaluateDecision<E extends LivingEntity & NeedsUser<E>> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT)
   );

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(E entity) {
        List<Need<E>> needs = new ArrayList<>(entity.getNeeds());

        needs.removeIf(need -> !need.canBeFulfilled().test(entity));

        if (!needs.isEmpty()) {
            needs.sort((n1, n2) -> {
                float priority1 = n1.priorityFunction().apply(entity.getEntityData().get(n1.data()));
                float priority2 = n2.priorityFunction().apply(entity.getEntityData().get(n2.data()));
                return Float.compare(priority2, priority1);
            });

            List<Activity> activities = needs.stream()
                    .map(Need::activity)
                    .collect(Collectors.toList());

            System.out.println(activities.getFirst());

            entity.getBrain().setActiveActivityToFirstValid(activities);
        }
    }
}
