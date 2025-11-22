package com.site21.bittermelon.common.systems.ai.behavior.blockinteraction;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.init.neoforge.BitterMemoryTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FindBlockingBlock<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MemoryTest.builder().hasMemory(MemoryModuleType.PATH).noMemory(BitterMemoryTypes.BREAK_TARGET.get());
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull E entity) {
        Path path = BrainUtil.getMemory(entity, MemoryModuleType.PATH);
        return path != null && !path.isDone();
    }

    @Override
    protected void start(E entity) {
        Path path = BrainUtil.getMemory(entity, MemoryModuleType.PATH);
        BlockPos nextPos = path.getNextNodePos();
        Level level = entity.level();

        for (int y = 0; y <= 1; y++) {
            BlockPos checkPos = nextPos.above(y);
            BlockState state = level.getBlockState(checkPos);

            if (!state.isAir()) {
                BrainUtil.setMemory(entity, BitterMemoryTypes.BREAK_TARGET.get(), checkPos);
                return;
            }
        }
    }
}
