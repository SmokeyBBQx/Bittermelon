package com.site21.bittermelon.common.content.entities.scp939.behavior;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryCondition;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.base.ExtendedBehaviour;
import net.tslat.smartbrainlib.library.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtil;

import java.util.Set;

public class SetWalkToDisturbanceLocation<E extends Mob> extends ExtendedBehaviour<E> {
    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder()
            .hasMemory(MemoryModuleType.DISTURBANCE_LOCATION)
            .usesMemories(MemoryModuleType.WALK_TARGET, MemoryModuleType.LOOK_TARGET);

    @Override
    public Set<MemoryCondition<?, ?>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(E entity) {
        BlockPos pos = BrainUtil.getMemory(entity, MemoryModuleType.DISTURBANCE_LOCATION);
        if (pos == null) return;

        BrainUtil.setMemory(entity.getBrain(), MemoryModuleType.LOOK_TARGET, new BlockPosTracker(pos));
        BrainUtil.setMemory(entity.getBrain(), MemoryModuleType.WALK_TARGET, new WalkTarget(pos, 0.7f, 2));
    }
}
