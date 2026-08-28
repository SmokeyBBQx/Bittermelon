package com.site21.bittermelon.common.content.entities.scp939.behavior;

import com.site21.bittermelon.common.content.entities.scp939.SCP939;
import com.site21.bittermelon.common.content.entities.scp939.SCP939State;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryCondition;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.tslat.smartbrainlib.api.core.behaviour.base.ExtendedBehaviour;
import net.tslat.smartbrainlib.library.object.MemoryTest;

import java.util.Set;

public class Listen extends ExtendedBehaviour<SCP939> {
    public static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder()
            .noMemory(MemoryModuleType.WALK_TARGET);

    @Override
    public Set<MemoryCondition<?, ?>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, SCP939 entity) {
        return entity.getNavigation().isDone();
    }

    @Override
    protected boolean shouldKeepRunning(SCP939 entity) {
        return true;
    }

    @Override
    protected void start(SCP939 entity) {
        entity.setState(SCP939State.LISTENING);
    }

    @Override
    protected void stop(SCP939 entity) {
        entity.setState(SCP939State.IDLE);
    }
}
