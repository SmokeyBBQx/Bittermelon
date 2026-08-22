package com.site21.bittermelon.common.content.entities.scp1507.behavior;

import com.site21.bittermelon.common.content.entities.scp1507.SCP1507;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryCondition;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.tslat.smartbrainlib.api.core.behaviour.base.ExtendedBehaviour;
import net.tslat.smartbrainlib.library.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtil;

import java.util.List;
import java.util.Set;

public class TryToBecomeActive extends ExtendedBehaviour<SCP1507> {
    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder()
            .hasMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES);

    @Override
    public Set<MemoryCondition<?, ?>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(SCP1507 entity) {
        List<LivingEntity> nearbyEntities = BrainUtil.getMemory(entity, MemoryModuleType.NEAREST_LIVING_ENTITIES);
        if (nearbyEntities.stream().noneMatch(nearbyEntity -> nearbyEntity instanceof Player))
            SCP1507.setActive(entity);
    }
}
