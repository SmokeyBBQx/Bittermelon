package com.site21.bittermelon.common.systems.ai.behavior.movement;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.common.systems.ai.base.BitterMob;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchArea<E extends BitterMob<?>> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT),
            Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED));

    private static final int EXPLORATION_RADIUS = 16;
    private static final int MIN_DISTANCE_BETWEEN_POINTS = 4;
    private static final int MAX_VISITED_POINTS = 50;

    private static final int STUCK_CHECK_INTERVAL = 20;
    private static final double STUCK_DISTANCE_THRESHOLD = 0.1;
    private static final int STUCK_TIME_THRESHOLD = 100;

    private final Set<BlockPos> visitedLocations = new HashSet<>();
    private BlockPos currentTarget;

    private Vec3 lastPosition;
    private int stuckTicks;
    private int ticksSinceLastCheck;
    private int consecutiveStuckChecks;


    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(@NotNull E entity) {
        if (visitedLocations.size() > MAX_VISITED_POINTS) {
            visitedLocations.clear();
        }

        visitedLocations.add(entity.blockPosition());
        resetStuckDetection(entity);
        findNewTarget(entity);
    }

    @Override
    protected void stop(E entity) {
        currentTarget = null;
        resetStuckDetection(entity);
    }

    @Override
    protected boolean shouldKeepRunning(E entity) {
        return visitedLocations.size() < MAX_VISITED_POINTS;
    }

    @Override
    protected void tick(E entity) {
        if (currentTarget == null) {
            findNewTarget(entity);
            return;
        }

        checkIfStuck(entity);
//        System.out.println("Is this even running for " + entity.getUUID());

        if (entity.getNavigation().isDone()) {
            visitedLocations.add(currentTarget);
            findNewTarget(entity);
        }
    }

    private void checkIfStuck(E entity) {
        ticksSinceLastCheck++;

        if (ticksSinceLastCheck >= STUCK_CHECK_INTERVAL) {
            ticksSinceLastCheck = 0;

            Vec3 currentPos = entity.position();

            if (lastPosition != null) {
                double distanceMoved = currentPos.distanceTo(lastPosition);

                if (distanceMoved < STUCK_DISTANCE_THRESHOLD) {
                    stuckTicks += STUCK_CHECK_INTERVAL;
                    consecutiveStuckChecks++;
                } else {
                    resetStuckDetection(entity);
                }
            }

            lastPosition = currentPos;
        }

        if (stuckTicks >= STUCK_TIME_THRESHOLD || entity.getNavigation().isStuck()) {
            handleStuckSituation(entity);
        }
    }

    private void handleStuckSituation(E entity) {
        System.out.println("Entity is stuck! Consecutive stuck checks: " + consecutiveStuckChecks);

        if (consecutiveStuckChecks >= 3) {
            visitedLocations.clear();
            consecutiveStuckChecks = 0;
        }

        entity.getNavigation().stop();

        findNewTarget(entity);

        resetStuckDetection(entity);
    }


    private void resetStuckDetection(@NotNull E entity) {
        lastPosition = entity.position();
        stuckTicks = 0;
        ticksSinceLastCheck = 0;
    }

    private void findNewTarget(E entity) {
        int attempts = 0;
        BlockPos newTarget = null;

        while (attempts < 12) {
            Vec3 pos = LandRandomPos.getPos(entity, EXPLORATION_RADIUS, 5);
            if (pos == null) {
                attempts++;
                continue;
            }

            BlockPos potential = new BlockPos((int)pos.x, (int)pos.y, (int)pos.z);

//            if (attempts < 11) {
                if (isValidExplorationTarget(entity, potential)) {
                    newTarget = potential;
                    break;
                }
//            } else {
//                newTarget = potential;
//                break;
//            }

            attempts++;
        }

        if (newTarget != null) {
//            if (entity.level().getBlockState(newTarget.below()).getBlock() != AIR) {
//                entity.level().setBlock(newTarget.below(), GLOWSTONE.defaultBlockState(), 3);
//            } else {
//                entity.level().setBlock(newTarget.below(), TORCH.defaultBlockState(), 3);
//            }
            currentTarget = newTarget;
//            entity.getNavigation().moveTo(newTarget.getX(), newTarget.getY(), newTarget.getZ(), 1.0F);
            BrainUtil.setMemory(entity, MemoryModuleType.WALK_TARGET,
                    new WalkTarget(newTarget, 1.0F, 2));
        }
    }

    private boolean isValidExplorationTarget(@NotNull E entity, BlockPos pos) {
        Path path = entity.getNavigation().createPath(pos, 0);
        if (path == null || !path.canReach()) {
            return false;
        }

        for (BlockPos visited : visitedLocations) {
            if (visited.closerThan(pos, MIN_DISTANCE_BETWEEN_POINTS)) {
                return false;
            }
        }

        return true;
    }
}
