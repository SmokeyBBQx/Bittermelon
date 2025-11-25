package com.site21.bittermelon.common.systems.ai.behavior.blockinteraction;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.init.neoforge.BitterMemoryTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathType;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FindBlockingBlock<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder()
            .hasMemory(MemoryModuleType.PATH)
            .noMemory(BitterMemoryTypes.BREAK_TARGET.get());

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull E entity) {
        Path path = BrainUtil.getMemory(entity, MemoryModuleType.PATH);
        return path != null && !path.isDone();
    }

    @Override
    protected void start(E entity) {
        Path path = BrainUtil.getMemory(entity, MemoryModuleType.PATH);
        Node currentNode = path.getNextNode();
        Node previousNode = path.getPreviousNode();

        if (previousNode != null) {
            int dx = currentNode.x - previousNode.x;
            int dy = currentNode.y - previousNode.y;
            int dz = currentNode.z - previousNode.z;

            // Check the block at the next node position
            var blockPos = entity.blockPosition().offset(dx, dy, dz);
            var blockState = entity.level().getBlockState(blockPos);

            if (!blockState.canBeReplaced()) {
                BrainUtil.setMemory(entity, BitterMemoryTypes.BREAK_TARGET.get(), blockPos);
            }
        }

    }
}
