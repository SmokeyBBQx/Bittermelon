package com.site21.bittermelon.common.systems.ai.behavior.blockinteraction;

import com.site21.bittermelon.init.neoforge.BitterMemoryTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryCondition;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.tslat.smartbrainlib.api.core.behaviour.base.ExtendedBehaviour;
import net.tslat.smartbrainlib.library.object.MemoryTest;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;
import net.tslat.smartbrainlib.util.BrainUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class FindRandomBreakTarget<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder()
            .hasMemory(SBLMemoryTypes.NEARBY_BLOCKS.get())
            .noMemory(BitterMemoryTypes.BREAK_TARGET.get())
            .usesMemory(MemoryModuleType.WALK_TARGET);

    protected Predicate<BlockPos> canBreakPredicate = pos -> true;

    /**
     * Set a predicate to determine if a block is breakable
     * @param predicate The predicate to test block positions
     * @return this
     */
    public FindRandomBreakTarget<E> breakablePredicate(Predicate<BlockPos> predicate) {
        this.canBreakPredicate = predicate;

        return this;
    }

    @Override
    public Set<MemoryCondition<?, ?>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(E entity) {
        List<BlockInWorld> blocks = BrainUtil.getMemory(entity, SBLMemoryTypes.NEARBY_BLOCKS.get()).stream()
                .filter(block -> isValidBlock(entity, block.getPos(), block.getState()))
                .toList();

        if (blocks.isEmpty()) return;
        int target = entity.getRandom().nextIntBetweenInclusive(0, blocks.size() - 1);
        BlockPos targetPos = blocks.get(target).getPos();
//        BrainUtil.setMemory(entity, BitterMemoryTypes.BREAK_TARGET.get(), targetPos);
//        BrainUtil.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(targetPos, 1, 2));
    }

    private boolean isValidBlock(@NotNull E entity, @NotNull BlockPos pos, @NotNull BlockState state) {
        return !state.canBeReplaced()
                && pos.getY() > entity.getOnPos().getY()
                && pos.distSqr(entity.blockPosition()) <= entity.getAttributeValue(Attributes.FOLLOW_RANGE)
                && canBreakPredicate.test(pos);
    }
}
