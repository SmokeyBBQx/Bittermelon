package com.site21.bittermelon.common.content.entities.scp1507.behavior;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.common.content.entities.scp1507.SCP1507;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class AwakenFlamingoBlocks extends ExtendedBehaviour<SCP1507> {
    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of();
    }
}
