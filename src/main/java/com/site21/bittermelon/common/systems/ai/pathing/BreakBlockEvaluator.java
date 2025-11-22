package com.site21.bittermelon.common.systems.ai.pathing;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.jetbrains.annotations.NotNull;

public class BreakBlockEvaluator extends WalkNodeEvaluator {
    public @NotNull PathType getPathType(@NotNull PathfindingContext context, int x, int y, int z) {
        PathType pathType = getPathTypeStatic(context, new BlockPos.MutableBlockPos(x, y, z));
        if (pathType == PathType.BLOCKED) {
            return PathType.WALKABLE;
        }
        return pathType;
    }
}
