package com.site21.bittermelon.entities.behavior.mood.mentalbreak;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.entities.behavior.needs.NeedsUser;
import com.site21.bittermelon.init.BitterMemoryModuleType;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.Predicate;

public class MentalBreak<E extends LivingEntity & NeedsUser<E>> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(BitterMemoryModuleType.HAS_MENTAL_BREAK.get(), MemoryStatus.VALUE_ABSENT)
    );

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(E entity) {
        BrainUtils.setMemory(entity, BitterMemoryModuleType.HAS_MENTAL_BREAK.get(), true);
    }

    @Override
    protected void stop(E entity) {
        BrainUtils.setMemory(entity, BitterMemoryModuleType.HAS_MENTAL_BREAK.get(), false);
    }
}
