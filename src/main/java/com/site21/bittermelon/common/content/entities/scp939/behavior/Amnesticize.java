package com.site21.bittermelon.common.content.entities.scp939.behavior;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.common.systems.ai.base.Need;
import com.site21.bittermelon.common.content.entities.scp939.SCP939;
import com.site21.bittermelon.init.neoforge.BitterSounds;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Amnesticize<E extends SCP939> extends DelayedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
    );

    public Amnesticize(int delayTicks) {
        super(delayTicks);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start (@NotNull E entity) {
        entity.playSound(BitterSounds.GHOSTLY_EXHALE.get(), 0.5f, 1.0f);
    }

    @Override
    protected void doDelayedAction(@NotNull E entity) {
        entity.modifyNeed(Need.REST, 2.5f);
    }
}
