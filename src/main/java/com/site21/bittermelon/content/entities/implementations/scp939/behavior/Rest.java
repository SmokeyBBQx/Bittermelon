package com.site21.bittermelon.content.entities.implementations.scp939.behavior;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.content.entities.implementations.scp939.SCP939;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.site21.bittermelon.content.entities.base.Need.REST;

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
    protected void tick(@NotNull E entity) {
        entity.modifyNeed(REST, restAmount);
    }

    @Override
    protected boolean shouldKeepRunning(@NotNull E entity) {
        return entity.getNeed(REST) < 100;
    }
}
