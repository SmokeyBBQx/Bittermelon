package com.site21.bittermelon.common.content.entities.scp939.behavior;

import com.site21.bittermelon.common.content.entities.scp939.SCP939;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryCondition;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.tslat.smartbrainlib.api.core.behaviour.base.DelayedBehaviour;
import net.tslat.smartbrainlib.library.object.MemoryTest;

import java.util.Set;

public class ReleaseGas extends DelayedBehaviour<SCP939> {
    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder()
            .hasMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES);

    public ReleaseGas(int delayTicks) {
        super(delayTicks);
    }

    @Override
    public Set<MemoryCondition<?, ?>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void doDelayedAction(SCP939 entity) {

    }
}
