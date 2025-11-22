package com.site21.bittermelon.common.systems.ai.behavior.blockinteraction;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.init.neoforge.BitterMemoryTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;
import net.tslat.smartbrainlib.util.BrainUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FindRandomBreakTarget<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder()
            .hasMemory(SBLMemoryTypes.NEARBY_BLOCKS.get())
            .noMemory(BitterMemoryTypes.BREAK_TARGET.get())
            .usesMemory(MemoryModuleType.WALK_TARGET);

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(E entity) {
        List<Pair<BlockPos, BlockState>> nearbyBlocks = BrainUtil.getMemory(entity, SBLMemoryTypes.NEARBY_BLOCKS.get());
        List<Pair<BlockPos, BlockState>> validBlocks = nearbyBlocks.stream()
                .filter(pair -> isValidBlock(entity, pair.getFirst(), pair.getSecond()))
                .toList();

        if (validBlocks.isEmpty()) return;
        int target = entity.getRandom().nextIntBetweenInclusive(0, validBlocks.size() - 1);
        BlockPos targetPos = validBlocks.get(target).getFirst();
        BrainUtil.setMemory(entity, BitterMemoryTypes.BREAK_TARGET.get(), targetPos);
        BrainUtil.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(targetPos, 1, 2));
    }

    private boolean isValidBlock(@NotNull E entity, @NotNull BlockPos pos, @NotNull BlockState state) {
        return !state.canBeReplaced()
                && pos.getY() > entity.getOnPos().getY()
                && pos.distSqr(entity.blockPosition()) <= entity.getAttributeValue(Attributes.FOLLOW_RANGE);
    }
}
