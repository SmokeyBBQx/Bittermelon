package com.site21.bittermelon.common.systems.ai.behavior.attack;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.common.systems.rage.RageUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.player.Player;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.ToIntFunction;

public class InduceRage<E extends LivingEntity> extends ExtendedBehaviour<E> {
    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder(1)
            .hasMemory(MemoryModuleType.ATTACK_TARGET);

    protected ToIntFunction<E> rageAmountSupplier = entity -> 1;

    @Nullable
    protected LivingEntity target = null;

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
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

        // Perhaps other mobs may have rage, but for now only players do
        if (target instanceof Player player) {
            RageUtil.updateRage(player, rageAmountSupplier.applyAsInt(entity));
        }
    }

    @Override
    protected void stop(E entity) {
        this.target = null;
    }
}
