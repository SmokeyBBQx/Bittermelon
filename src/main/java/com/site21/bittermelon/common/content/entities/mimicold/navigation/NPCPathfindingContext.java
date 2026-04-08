package com.site21.bittermelon.common.content.entities.mimicold.navigation;

import com.site21.bittermelon.common.content.entities.mimicold.MimicPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathTypeCache;

import javax.annotation.Nullable;

public class NPCPathfindingContext {
    private final CollisionGetter level;
    @Nullable
    private final PathTypeCache cache;
    private final BlockPos mobPosition;
    private final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

    public NPCPathfindingContext(CollisionGetter level, MimicPlayer mob) {
        this.level = level;
        if (mob.level() instanceof ServerLevel serverlevel) {
            this.cache = serverlevel.getPathTypeCache();
        } else {
            this.cache = null;
        }

        this.mobPosition = mob.blockPosition();
    }

    public PathType getPathTypeFromState(int x, int y, int z) {
        BlockPos blockpos = this.mutablePos.set(x, y, z);
        return this.cache.getOrCompute(this.level, blockpos);
    }

    public BlockState getBlockState(BlockPos pos) {
        return this.level.getBlockState(pos);
    }

    public CollisionGetter level() {
        return this.level;
    }

    public BlockPos mobPosition() {
        return this.mobPosition;
    }

    BlockPos currentEvalPos() {
        return this.mutablePos;
    }
}
