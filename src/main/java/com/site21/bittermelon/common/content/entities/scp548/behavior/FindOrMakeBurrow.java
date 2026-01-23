package com.site21.bittermelon.common.content.entities.scp548.behavior;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.common.content.blocks.burrow.BurrowBlockEntity;
import com.site21.bittermelon.init.neoforge.BitterBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.FreePositionTracker;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;
import net.tslat.smartbrainlib.util.BrainUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.site21.bittermelon.init.neoforge.BitterBlocks.BURROW;

public class FindOrMakeBurrow extends ExtendedBehaviour<Mob> {
    private static final int DIGGING_DURATION = 60;

    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder()
            .hasMemories(SBLMemoryTypes.NEARBY_BLOCKS.get());

    private BlockPos burrowPos = null;
    private int diggingTicks = 0;

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean shouldKeepRunning(Mob entity) {
        return burrowPos != null;
    }

    @Override
    protected void start(@NotNull Mob entity) {
        List<Pair<BlockPos, BlockState>> nearbyBlocks = BrainUtil.getMemory(entity, SBLMemoryTypes.NEARBY_BLOCKS.get());
        if (nearbyBlocks == null) return;
        Level level = entity.level();

        // First, look for existing burrow
        for (Pair<BlockPos, BlockState> blockEntry : nearbyBlocks) {
            BlockPos pos = blockEntry.getFirst();
            BlockState state = blockEntry.getSecond();
            if (state.is(BURROW) && canEnterBurrow(level, entity, pos, state)) {
                burrowPos = pos;
                setWalkTarget(entity, pos);
                return;
            }
        }

        // Otherwise, look for burrowable block
        for (Pair<BlockPos, BlockState> blockEntry : nearbyBlocks) {
            BlockPos pos = blockEntry.getFirst();
            BlockState state = blockEntry.getSecond();
            if ((state.is(BitterBlockTags.BURROWABLE) || state.is(Blocks.DIRT)) && level.getBlockState(pos.above()).isAir()) {
                burrowPos = pos.above();
                setWalkTarget(entity, pos);
                return;
            }
        }
    }

    @Override
    protected void tick(@NotNull Mob entity) {
        if (hasArrived(entity)) {
            BrainUtil.clearMemory(entity, MemoryModuleType.WALK_TARGET);

            if (!entity.level().getBlockState(burrowPos).is(BURROW)) {
                diggingTicks++;
                spawnDiggingParticles(entity);

                if (diggingTicks >= DIGGING_DURATION) {
                    entity.level().setBlock(burrowPos, BURROW.get().defaultBlockState(), 3);
                }
            } else {
                if (diggingTicks == 0 || diggingTicks >= DIGGING_DURATION + 10) {
                    enterBurrow(entity);
                }
            }
        }
    }

    private void spawnDiggingParticles(@NotNull Mob entity) {
        if (entity.level() instanceof ServerLevel level) {
            BlockState state = level.getBlockState(burrowPos.below());
            level.sendParticles(
                    new BlockParticleOption(ParticleTypes.BLOCK, state),
                    burrowPos.getX() + 0.5,
                    burrowPos.getY() + 0.1,
                    burrowPos.getZ() + 0.5,
                    2,
                    0.1, 0.2, 0.1,
                    0.05
            );
        }
    }

    private boolean hasArrived(Mob entity) {
        return burrowPos != null && entity.blockPosition().distManhattan(burrowPos) <= 0;
    }

    private void setWalkTarget(Mob entity, BlockPos pos) {
        BrainUtil.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(pos, 1, 0));
        BrainUtil.setMemory(entity, MemoryModuleType.LOOK_TARGET, new FreePositionTracker(pos.getCenter()));
    }

    private void enterBurrow(@NotNull Mob entity) {
        if (entity.level().getBlockEntity(burrowPos) instanceof BurrowBlockEntity burrow) {
            burrow.insertEntity(entity);
        }
        burrowPos = null;
    }

    private boolean canEnterBurrow(@NotNull Level level, @NotNull Mob entity, BlockPos pos, BlockState state) {
        AABB aabb = new AABB(pos);
        return level.getBlockEntity(pos) instanceof BurrowBlockEntity burrow
                && !burrow.isOccupied()
                && level.getEntitiesOfClass(LivingEntity.class, aabb, entity1 -> !entity1.equals(entity)).isEmpty();
    }

    @Override
    protected void stop(Mob entity) {
        burrowPos = null;
        diggingTicks = 0;
    }
}
