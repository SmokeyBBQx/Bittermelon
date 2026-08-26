package com.site21.bittermelon.common.content.entities.scp939.behavior;

import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryCondition;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.base.ExtendedBehaviour;
import net.tslat.smartbrainlib.library.object.MemoryTest;

import java.util.Set;

public class SweepArea extends ExtendedBehaviour<PathfinderMob> {
    public static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder()
            .usesMemories(MemoryModuleType.WALK_TARGET);
    private static final Long2LongOpenHashMap VISIT_GRID = new Long2LongOpenHashMap();
    private static final Long2IntOpenHashMap ACTIVITY_GRID = new Long2IntOpenHashMap();
    private final Long2LongOpenHashMap touchGrid = new Long2LongOpenHashMap();
    private static final int SEARCH_RADIUS = 8;
    private static final int CELL_SIZE = 4;

    @Override
    public Set<MemoryCondition<?, ?>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean shouldKeepRunning(PathfinderMob entity) {
        return true;
    }

    @Override
    protected void tick(PathfinderMob entity) {
        long key = gridKey(entity.blockPosition());
        VISIT_GRID.put(key, entity.level().getGameTime());
        touchGrid.put(entity.blockPosition().asLong(), entity.level().getGameTime());
        if (!entity.getNavigation().isDone()) return;

        Path path = pickTarget(entity);
        if (path == null) return;
        BlockPos targetPos = path.getTarget();

        if (entity.level() instanceof ServerLevel level) {
            level.sendParticles(
                ParticleTypes.CAMPFIRE_SIGNAL_SMOKE,
                targetPos.getX() + 0.5,
                targetPos.getY() + 0.5,
                targetPos.getZ() + 0.5,
                1,
                0, 0, 0, 0
            );
        }
        entity.getNavigation().moveTo(path, 1.0);
        entity.getLookControl().setLookAt(Vec3.atCenterOf(targetPos));
    }

    private Path pickTarget(PathfinderMob entity) {
        Path best = null;
        float bestScore = Float.NEGATIVE_INFINITY;
        long currentTime = entity.level().getGameTime();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        BlockPos entityPos = entity.blockPosition();

        for (int dx = -SEARCH_RADIUS; dx <= SEARCH_RADIUS; dx++) {
            for (int dz = -SEARCH_RADIUS; dz <= SEARCH_RADIUS; dz++) {
                pos.set(entityPos.getX() + dx, entityPos.getY(), entityPos.getZ() + dz);
                if (entity.level().getBlockState(pos).isCollisionShapeFullBlock(entity.level(), pos)) continue;

                float score = scoreCell(pos, currentTime, entity.getDeltaMovement(), entityPos);
                if (score > bestScore) {
                    Path path = entity.getNavigation().createPath(pos, 0);
                    if (path == null || !path.canReach()) continue;

                    bestScore = score;
                    best = path;
                }
            }
        }

        return best;
    }

    private float scoreCell(BlockPos blockPos, long currentTime, Vec3 heading, BlockPos pos) {
        if (blockPos.distSqr(pos) <= 1) {
            return 0;
        }

        long gridKey = gridKey(blockPos);
        long lastVisitTime = VISIT_GRID.getOrDefault(gridKey, 0L);
        long timeSinceLastVisit = currentTime - lastVisitTime;

        int activityLevel = ACTIVITY_GRID.getOrDefault(gridKey, 0);

        long lastTouch = touchGrid.getOrDefault(blockPos.asLong(), 0L);
        long timeSinceLastTouch = currentTime - lastTouch;

        float momentum = 0;
        if (heading.lengthSqr() > 0.01) {
            Vec3 toCell = new Vec3(blockPos.getX() - pos.getX(), 0, blockPos.getZ() - pos.getZ()).normalize();
            momentum = (float) heading.dot(toCell) * 30;
        }

        return timeSinceLastVisit + activityLevel + momentum + timeSinceLastTouch;
    }

    private static long gridKey(BlockPos pos) {
        int x = Math.floorDiv(pos.getX(), CELL_SIZE);
        int z = Math.floorDiv(pos.getZ(), CELL_SIZE);
        return (x & 0xFFFFFFFFL) | ((long) z << 32);
    }
}
