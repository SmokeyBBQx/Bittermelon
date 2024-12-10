package com.site21.bittermelon.entities.scps.SCP939.behavior;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.entities.scps.SCP939.SCP939;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class Rest<E extends SCP939> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT)
    );

    private final float restAmount;

    public Rest(float restAmount) {
        noTimeout();
        this.restAmount = restAmount;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void tick(E entity) {
        entity.modifyRest(restAmount);
    }

    @Override
    protected boolean shouldKeepRunning(E entity) {
        return entity.getRest() < 100;
    }
}
