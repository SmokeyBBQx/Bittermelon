package com.site21.bittermelon.entities.scps.SCP939.behavior;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.entities.scps.SCP939.SCP939;
import com.site21.bittermelon.init.BitterSounds;
import com.site21.bittermelon.util.LocalMessageHelper;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;

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
    protected void start (E entity) {
        entity.playSound(BitterSounds.GHOSTLY_EXHALE.get(), 0.5f, 1.0f);
    }

    @Override
    protected void doDelayedAction(E entity) {
        LocalMessageHelper.sendLocalMessage(entity, 32, Component.literal("Releasing amnestics"));

        entity.modifyRest(-2.5f);
    }
}
