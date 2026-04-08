package com.site21.bittermelon.common.content.entities.mimicold.behavior;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.common.content.entities.mimicold.MimicPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.PositionTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtil;

import java.util.List;

public class LookAtTarget<E extends MimicPlayer> extends ExtendedBehaviour<E> {
    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder(1).hasMemory(MemoryModuleType.LOOK_TARGET);

    public LookAtTarget() {
        noTimeout();
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return testAndInvalidateLookTarget(entity);
    }

    @Override
    protected boolean shouldKeepRunning(E entity) {
        return testAndInvalidateLookTarget(entity);
    }

    @Override
    protected void tick(E entity) {
        BrainUtil.withMemory(entity, MemoryModuleType.LOOK_TARGET, target -> entity.getLookControl().setLookAt(target.currentPosition()));
    }

    /**
     * Check and expire the look target if it is no longer valid
     *
     * @return true if the look target is valid
     */
    protected boolean testAndInvalidateLookTarget(E entity) {
        PositionTracker lookTarget = BrainUtil.getMemory(entity, MemoryModuleType.LOOK_TARGET);

        if (lookTarget == null)
            return false;

        if (lookTarget instanceof EntityTracker entityTracker && (!entityTracker.getEntity().isAlive() || entityTracker.getEntity().isSpectator())) {
            BrainUtil.clearMemory(entity, MemoryModuleType.LOOK_TARGET);

            return false;
        }

        return true;
    }
}
