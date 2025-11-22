package com.site21.bittermelon.common.systems.ai.behavior.blockinteraction;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.init.neoforge.BitterMemoryTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;

import java.util.List;

public class InvalidateBreakTarget<E extends LivingEntity> extends ExtendedBehaviour<E> {
    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder().hasMemory(BitterMemoryTypes.BREAK_TARGET.get());

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(E entity) {
        super.start(entity);
    }
}
