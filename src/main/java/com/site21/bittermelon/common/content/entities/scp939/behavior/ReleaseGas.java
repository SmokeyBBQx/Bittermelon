package com.site21.bittermelon.common.content.entities.scp939.behavior;

import com.site21.bittermelon.common.content.entities.scp939.SCP939;
import com.site21.bittermelon.init.neoforge.BitterMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryCondition;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.tslat.smartbrainlib.api.core.behaviour.base.DelayedBehaviour;
import net.tslat.smartbrainlib.library.object.MemoryTest;

import java.util.List;
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
        List<LivingEntity> nearbyEntities = entity.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).orElse(List.of());
        for (LivingEntity livingEntity : nearbyEntities) {
            MobEffectInstance effect = new MobEffectInstance(BitterMobEffects.AMNESIA, 1200, 0, true, false);
            livingEntity.addEffect(effect);
        }
    }
}
