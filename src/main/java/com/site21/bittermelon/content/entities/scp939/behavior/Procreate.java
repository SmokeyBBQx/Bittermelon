package com.site21.bittermelon.content.entities.scp939.behavior;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.systems.ai.base.Need;
import com.site21.bittermelon.content.entities.scp939.SCP939;
import com.site21.bittermelon.util.LocalMessageHelper;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;

import java.util.List;

public class Procreate<E extends SCP939> extends DelayedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT)
    );

    public Procreate(int delayTicks) {
        super(delayTicks);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void doDelayedAction(E entity) {
        LocalMessageHelper.sendLocalMessage(entity, 32, Component.literal("Procreating"));
        entity.setNeed(Need.PROCREATION, -100);
    }
}
