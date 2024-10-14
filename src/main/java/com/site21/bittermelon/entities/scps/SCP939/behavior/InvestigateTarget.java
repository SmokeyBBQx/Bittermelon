package com.site21.bittermelon.entities.scps.SCP939.behavior;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.entities.scps.SCP939.SCP939;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetAttackTarget;
import net.tslat.smartbrainlib.util.BrainUtils;

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
        if (entity.getSuspicion() > 0.9f) {
            LivingEntity nearestAttackable = BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_ATTACKABLE);
            BrainUtils.setMemory(entity, MemoryModuleType.ATTACK_TARGET, nearestAttackable);
            BrainUtils.clearMemory(entity, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        }
    }

    @Override
    protected void tick(E entity) {
        if (entity.getSuspicion() > 0.9f) {
            LivingEntity nearestAttackable = BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_ATTACKABLE);
            BrainUtils.setMemory(entity, MemoryModuleType.ATTACK_TARGET, nearestAttackable);
            BrainUtils.clearMemory(entity, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
        }
    }
}
