package com.site21.bittermelon.common.systems.ai.behavior.blockinteraction;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.init.neoforge.BitterMemoryTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.block.state.BlockState;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiPredicate;

/**
 * Invalidates the break target memory under certain conditions.
 *
 * @param <E> The type of entity this behaviour is applied to.
 */
public class InvalidateBreakTarget<E extends LivingEntity> extends ExtendedBehaviour<E> {
    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder()
            .hasMemory(BitterMemoryTypes.BREAK_TARGET.get())
            .usesMemory(MemoryModuleType.LOOK_TARGET)
            .usesMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);

    protected BiPredicate<E, BlockPos> customPredicate = (entity, target) ->
            (entity.getAttributes().hasAttribute(Attributes.FOLLOW_RANGE)
                    && target.distSqr(entity.getOnPos()) >= Mth.square(entity.getAttributeValue(Attributes.FOLLOW_RANGE)));
    protected long pathfindingAttentionSpan = 200;

    /**
     * Sets a custom predicate to invalidate the break target if none of the previous checks invalidate it first.<br>
     * Overrides the default player gamemode check
     */
    public InvalidateBreakTarget<E> invalidateIf(BiPredicate<E, BlockPos> predicate) {
        this.customPredicate = predicate;

        return this;
    }

    /**
     * Skips the check to see if the entity has been unable to path to its target for a while
     */
    public InvalidateBreakTarget<E> ignoreFailedPathfinding() {
        return stopTryingToPathAfter(0);
    }

    /**
     * Sets the attention span for the brain owner's pathfinding. If the entity has been unable to find a good path to
     * the target after this time, it will invalidate the target.
     */
    public InvalidateBreakTarget<E> stopTryingToPathAfter(long ticks) {
        this.pathfindingAttentionSpan = ticks;

        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(E entity) {
        BlockPos target = BrainUtil.getMemory(entity, BitterMemoryTypes.BREAK_TARGET.get());

        if (target == null) return;

        if (isTargetInvalid(entity, target) || isTiredOfPathing(entity) || customPredicate.test(entity, target)) {
            BrainUtil.clearMemory(entity, BitterMemoryTypes.BREAK_TARGET.get());

            if (BrainUtil.getMemory(entity, MemoryModuleType.LOOK_TARGET) instanceof BlockPosTracker lookTarget
                    && lookTarget.currentBlockPosition().equals(target)) {
                BrainUtil.clearMemory(entity, MemoryModuleType.LOOK_TARGET);
            }
        }
    }

    protected boolean isTargetInvalid(@NotNull E entity, @NotNull BlockPos target) {
        BlockState state = entity.level().getBlockState(target);
        return entity.level().getBlockState(target).isAir();
    }

    protected boolean isTiredOfPathing(E entity) {
        if (pathfindingAttentionSpan <= 0)
            return false;

        Long time = BrainUtil.getMemory(entity, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);

        return time != null && entity.level().getGameTime() - time > pathfindingAttentionSpan;
    }
}
