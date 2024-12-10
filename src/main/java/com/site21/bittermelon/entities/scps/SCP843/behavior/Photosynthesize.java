package com.site21.bittermelon.entities.scps.SCP843.behavior;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.entities.scps.SCP843.SCP843;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class Photosynthesize<E extends SCP843> extends ExtendedBehaviour<E> {
    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of();
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return level.getMaxLocalRawBrightness(new BlockPos((int) entity.getX(), (int) entity.getEyeY(),
                (int) entity.getZ())) > 8;
    }

    @Override
    protected void start(E entity) {
        int lightLevel = entity.level().getMaxLocalRawBrightness(entity.blockPosition());
        entity.modifySunlight((float) lightLevel / 10);
    }
}
