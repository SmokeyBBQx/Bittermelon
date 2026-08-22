package com.site21.bittermelon.common.systems.ai.behavior.mentalbreak;

import com.site21.bittermelon.common.systems.ai.base.NeedsUser;
import com.site21.bittermelon.init.neoforge.BitterMemoryTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryCondition;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.tslat.smartbrainlib.library.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtil;

import java.util.Optional;
import java.util.Set;

public class MurderousRage<E extends LivingEntity & NeedsUser> extends MentalBreak<E> {
    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder()
            .noMemory(BitterMemoryTypes.HAS_MENTAL_BREAK.get())
            .hasMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
            .noMemory(MemoryModuleType.ATTACK_TARGET);
    private final boolean sameTypeOnly;

    @Override
    public Set<MemoryCondition<?, ?>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    public MurderousRage(boolean sameTypeOnly) {
        this.sameTypeOnly = sameTypeOnly;
    }

    public MurderousRage() {
        this(false);
    }

    @Override
    protected void start(E entity) {
        super.start(entity);

        NearestVisibleLivingEntities nearestEntities = BrainUtil.getMemory(entity, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
        if (nearestEntities != null) {
            Optional<LivingEntity> closestEntity;

            if (sameTypeOnly) {
                closestEntity = nearestEntities.findClosest(targetEntity -> entity.getClass().equals(targetEntity.getClass()));
            } else {
                closestEntity = nearestEntities.findClosest(Entity::isAttackable);
            }

            closestEntity.ifPresent(livingEntity -> BrainUtil.setMemory(entity, MemoryModuleType.ATTACK_TARGET, livingEntity));
        }
    }
}
