package com.site21.bittermelon.common.systems.ai.behavior.herd;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryCondition;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.tslat.smartbrainlib.api.core.behaviour.base.ExtendedBehaviour;
import net.tslat.smartbrainlib.library.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.site21.bittermelon.init.neoforge.BitterMemoryTypes.LEADER;

public class VerifyOrFindLeader<E extends LivingEntity> extends ExtendedBehaviour<E> {
    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder()
            .usesMemory(LEADER.get())
            .hasMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES);

    private List<LivingEntity> allies = new ArrayList<>();

    @Override
    public Set<MemoryCondition<?, ?>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return verifyLeader(entity);
    }

    @Override
    protected void start(E entity) {
        List<LivingEntity> nearbyEntities = BrainUtil.getMemory(entity, MemoryModuleType.NEAREST_LIVING_ENTITIES);

        // Filter nearby entities to only include allies of the same type
        allies = nearbyEntities.stream().filter(e -> e.getType() == entity.getType()).toList();

        // First check if any nearby allies have a leader
        for (LivingEntity ally : allies) {
            LivingEntity allyLeader = BrainUtil.getMemory(ally, LEADER.get());
            if (allyLeader != null && verifyLeader(allyLeader)) {
                setLeader(entity, allyLeader);
                return;
            }
        }

        // If no nearby allies have a leader, pick one of the allies as the leader
        if (!allies.isEmpty()) {
            LivingEntity newLeader = allies.getFirst();
            setLeader(entity, newLeader);
        }
    }

    private boolean verifyLeader(LivingEntity entity) {
        LivingEntity leader = BrainUtil.getMemory(entity, LEADER.get());
        return leader == null;
    }

    private void setLeader(E entity, LivingEntity leader) {
        BrainUtil.setMemory(entity, LEADER.get(), leader);
        for (LivingEntity otherAlly : allies) {
            if (otherAlly != entity) {
                BrainUtil.setMemory(otherAlly, LEADER.get(), leader);
            }
        }
    }
}
