package com.site21.bittermelon.common.systems.atmosphere;

import com.site21.bittermelon.init.neoforge.BitterBlockTags;
import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public final class FloodFill {
    // Based on Ad Astra's Floodfill Algorithm
    // https://github.com/terrarium-earth/Ad-Astra/blob/1.20.x/common/src/main/java/earth/terrarium/adastra/common/utils/floodfill/FloodFill3D.java
    // (See LICENSE for full attribution and licensing)

    private static final Direction[] DIRECTIONS = Direction.values();

    public static final SolidBlockPredicate TEST_FULL_SEAL = (level, pos, state, positions, queue, direction) -> {
        if (state.isAir()) return true;
        if (state.is(BitterBlockTags.PASSES_ATMOS)) return true;
        if (state.isCollisionShapeFullBlock(level, pos)) return false;

        VoxelShape collisionShape = state.getCollisionShape(level, pos);

        if (collisionShape.isEmpty()) return true;
        if (!isSideSolid(collisionShape, direction)) return true;
        if (!isFaceSturdy(collisionShape, direction) && !isFaceSturdy(collisionShape, direction.getOpposite())) {
            return true;
        }

        for (Direction dir : DIRECTIONS) {
            if (dir.getAxis() == direction.getAxis()) continue;
            var adjacentPos = pos.relative(dir);
            var adjacentState = level.getBlockState(adjacentPos);
            if (adjacentState.isAir()) return true;
        }

        positions.add(pos.asLong());
        return false;
    };

    public static @NotNull Set<BlockPos> run(Level level, @NotNull BlockPos start, int limit) {
        return run(level, start, limit, TEST_FULL_SEAL);
    }

    public static @NotNull Set<BlockPos> run(@NotNull Level level, @NotNull BlockPos start, int limit, SolidBlockPredicate predicate) {
        if (level.isClientSide()) return Set.of();
        LongSet visited = new LongOpenHashSet(limit);
        LongArrayFIFOQueue queue = new LongArrayFIFOQueue(limit);

        queue.enqueue(start.asLong());

        while (!queue.isEmpty() && visited.size() < limit) {
            long packedPos = queue.dequeueLong();

            if (visited.contains(packedPos)) continue;
            visited.add(packedPos);

            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(BlockPos.getX(packedPos), BlockPos.getY(packedPos), BlockPos.getZ(packedPos));
            for (Direction direction : DIRECTIONS) {
                mutablePos.set(packedPos);
                mutablePos.move(direction);
                BlockState state = level.getBlockState(mutablePos);

                if (!predicate.test(level, mutablePos, state, visited, queue, direction)) continue;

                queue.enqueue(mutablePos.asLong());
            }
        }

        // TODO: If limit reached, return no atmos

        Set<BlockPos> result = new HashSet<>(visited.size());
        for (long packed : visited) {
            result.add(BlockPos.of(packed));
        }

        return result;
    }

    private static boolean isSideSolid(VoxelShape collisionShape, @NotNull Direction dir) {
        return switch (dir.getAxis()) {
            case X -> isAxisCovered(collisionShape, Direction.Axis.Y, Direction.Axis.Z);
            case Y -> isAxisCovered(collisionShape, Direction.Axis.X, Direction.Axis.Z);
            case Z -> isAxisCovered(collisionShape, Direction.Axis.X, Direction.Axis.Y);
        };
    }

    private static boolean isAxisCovered(@NotNull VoxelShape shape, Direction.Axis axis1, Direction.Axis axis2) {
        return shape.min(axis1) <= 0 && shape.max(axis1) >= 1 && shape.min(axis2) <= 0 && shape.max(axis2) >= 1;
    }


    private static boolean isFaceSturdy(@NotNull VoxelShape collisionShape, Direction dir) {
        VoxelShape faceShape = collisionShape.getFaceShape(dir);
        if (faceShape.isEmpty()) return true;
        var aabbs = faceShape.toAabbs();
        if (aabbs.isEmpty()) return true;
        return checkBounds(aabbs.get(0), dir.getAxis());
    }

    @Contract(pure = true)
    private static boolean checkBounds(AABB bounds, Direction.@NotNull Axis axis) {
        return switch (axis) {
            case X -> bounds.minY <= 0 && bounds.maxY >= 1 && bounds.minZ <= 0 && bounds.maxZ >= 1;
            case Y -> bounds.minX <= 0 && bounds.maxX >= 1 && bounds.minZ <= 0 && bounds.maxZ >= 1;
            case Z -> bounds.minX <= 0 && bounds.maxX >= 1 && bounds.minY <= 0 && bounds.maxY >= 1;
        };
    }


    @FunctionalInterface
    public interface SolidBlockPredicate {
        boolean test(Level level, BlockPos pos, BlockState state, LongSet positions, LongArrayFIFOQueue queue, Direction direction);
    }
}
