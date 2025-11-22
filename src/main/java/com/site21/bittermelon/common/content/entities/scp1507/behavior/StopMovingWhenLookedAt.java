package com.site21.bittermelon.common.content.entities.scp1507.behavior;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.common.content.entities.scp1507.SCP1507;
import com.site21.bittermelon.init.neoforge.BitterMemoryTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.player.Player;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtil;

import java.util.List;

public class StopMovingWhenLookedAt extends ExtendedBehaviour<SCP1507> {
    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder()
            .hasMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES)
            .usesMemory(BitterMemoryTypes.ACTIVE.get());

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(SCP1507 entity) {
        List<LivingEntity> nearbyEntities = BrainUtil.getMemory(entity, MemoryModuleType.NEAREST_LIVING_ENTITIES);
        if (nearbyEntities.stream().anyMatch(nearbyEntity -> nearbyEntity instanceof Player))
            BrainUtil.setMemory(entity, BitterMemoryTypes.ACTIVE.get(), false);
    }
}
