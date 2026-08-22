package com.site21.bittermelon.common.systems.ai.behavior.attack;

import com.site21.bittermelon.common.systems.stress.StressUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryCondition;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.tslat.smartbrainlib.api.core.behaviour.base.ExtendedBehaviour;
import net.tslat.smartbrainlib.library.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtil;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.function.ToIntFunction;

public class InduceStress<E extends LivingEntity> extends ExtendedBehaviour<E> {
    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder()
            .hasMemory(MemoryModuleType.ATTACK_TARGET)
            .noMemory(MemoryModuleType.ATTACK_COOLING_DOWN);

    protected ToIntFunction<E> attackIntervalSupplier = entity -> 20;
    protected ToIntFunction<E> stressAmountSupplier = entity -> 1;

    @Nullable
    protected LivingEntity target = null;

    /**
     * Set the time between attacks.
     * @param supplier The tick value provider
     * @return this
     */
    public InduceStress<E> attackInterval(ToIntFunction<E> supplier) {
        this.attackIntervalSupplier = supplier;
        return this;
    }

        /**
        * Set the amount of stress to induce on the target.
        * @param supplier The stress amount provider
        * @return this
        */
    public InduceStress<E> stressAmount(ToIntFunction<E> supplier) {
        this.stressAmountSupplier = supplier;
        return this;
    }

    @Override
    public Set<MemoryCondition<?, ?>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        target = BrainUtil.getTargetOfEntity(entity);
        return super.checkExtraStartConditions(level, entity) && target != null;
    }

    @Override
    protected void start(E entity) {
        if (this.target == null) return;

        BehaviorUtils.lookAtEntity(entity, target);
        BrainUtil.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true,
                attackIntervalSupplier.applyAsInt(entity));

        // Perhaps other mobs may have stress, but for now only players do
        if (target instanceof Player player) {
            StressUtil.updateStress(player, stressAmountSupplier.applyAsInt(entity));
        }
    }

    @Override
    protected void stop(E entity) {
        this.target = null;
    }
}
