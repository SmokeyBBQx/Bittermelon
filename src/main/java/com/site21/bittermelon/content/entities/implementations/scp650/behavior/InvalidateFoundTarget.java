package com.site21.bittermelon.content.entities.implementations.scp650.behavior;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.content.entities.implementations.scp650.SCP650;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.site21.bittermelon.init.neoforge.BitterMemoryTypes.FOUND_TARGET;
import static com.site21.bittermelon.init.neoforge.BitterMemoryTypes.OBSERVERS;

public class InvalidateFoundTarget<E extends SCP650> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(FOUND_TARGET.get(), MemoryStatus.VALUE_PRESENT),
            Pair.of(OBSERVERS.get(), MemoryStatus.VALUE_PRESENT)
    );

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(@NotNull E entity) {
        // If the entity has been observed after it has teleported to its target, clear the target to let it find a new one.
        BrainUtils.clearMemory(entity, FOUND_TARGET.get());
    }
}
