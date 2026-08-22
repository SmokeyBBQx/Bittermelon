package com.site21.bittermelon.common.systems.ai.behavior.mentalbreak;

import com.site21.bittermelon.common.systems.ai.base.NeedsUser;
import com.site21.bittermelon.init.neoforge.BitterMemoryTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryCondition;
import net.tslat.smartbrainlib.api.core.behaviour.base.ExtendedBehaviour;
import net.tslat.smartbrainlib.library.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtil;

import java.util.Set;

public abstract class MentalBreak<E extends LivingEntity & NeedsUser> extends ExtendedBehaviour<E> {
    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder()
            .noMemory(BitterMemoryTypes.BREAK_TARGET.get());

    @Override
    public Set<MemoryCondition<?, ?>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(E entity) {
        BrainUtil.setMemory(entity, BitterMemoryTypes.HAS_MENTAL_BREAK.get(), true);
    }

    @Override
    protected void stop(E entity) {
        BrainUtil.setMemory(entity, BitterMemoryTypes.HAS_MENTAL_BREAK.get(), false);
    }
}
