package com.site21.bittermelon.systems.ai.behavior.mood.mentalbreak;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.systems.ai.base.NeedsUser;
import com.site21.bittermelon.init.neoforge.BitterMemoryTypes;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.tslat.smartbrainlib.util.BrainUtil;

import java.util.List;
import java.util.Optional;

public class MurderousRage<E extends LivingEntity & NeedsUser> extends MentalBreak<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(BitterMemoryTypes.HAS_MENTAL_BREAK.get(), MemoryStatus.VALUE_ABSENT),
            Pair.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT),
            Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT)
    );
    private final boolean sameTypeOnly;

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
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
