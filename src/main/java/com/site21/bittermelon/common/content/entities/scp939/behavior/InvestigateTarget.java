package com.site21.bittermelon.common.content.entities.scp939.behavior;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.common.content.entities.scp939.SCP939;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtil;

import java.util.List;

public class InvestigateTarget<E extends SCP939> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT),
            Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT)
    );

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(E entity) {
            LivingEntity nearestAttackable = BrainUtil.getMemory(entity, MemoryModuleType.NEAREST_ATTACKABLE);
            BrainUtil.setMemory(entity, MemoryModuleType.ATTACK_TARGET, nearestAttackable);
            BrainUtil.clearMemory(entity, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
    }

    @Override
    protected void tick(E entity) {
            LivingEntity nearestAttackable = BrainUtil.getMemory(entity, MemoryModuleType.NEAREST_ATTACKABLE);
            BrainUtil.setMemory(entity, MemoryModuleType.ATTACK_TARGET, nearestAttackable);
            BrainUtil.clearMemory(entity, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
    }
}
