package com.site21.bittermelon.common.content.entities.mimicold.navigation;

import com.site21.bittermelon.common.content.entities.mimicold.MimicPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;

public class NPCGroundPathNavigation extends NPCNavigation {
    private boolean avoidSun;

    public NPCGroundPathNavigation(MimicPlayer mob, Level level) {
        super(mob, level);
    }

    @Override
    protected NPCPathfinder createPathFinder(int maxVisitedNodes) {
        this.nodeEvaluator = new NPCWalkNodeEvaluator();
        return new NPCPathfinder(this.nodeEvaluator, maxVisitedNodes);
    }

    @Override
    protected boolean canUpdatePath() {
        return this.mob.onGround() || this.mob.isInLiquid() || this.mob.isPassenger();
    }

    @Override
    protected Vec3 getTempMobPos() {
        return new Vec3(this.mob.getX(), this.getSurfaceY(), this.mob.getZ());
    }

    /**
     * Returns path to given BlockPos
     */
    @Override
    public Path createPath(BlockPos pos, int accuracy) {
        LevelChunk levelchunk = this.level
                .getChunkSource()
                .getChunkNow(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
        if (levelchunk == null) {
            return null;
        } else {
            if (levelchunk.getBlockState(pos).isAir()) {
                BlockPos.MutableBlockPos blockpos$mutableblockpos = pos.mutable().move(Direction.DOWN);

                while (blockpos$mutableblockpos.getY() >= this.level.getMinY() && levelchunk.getBlockState(blockpos$mutableblockpos).isAir()) {
                    blockpos$mutableblockpos.move(Direction.DOWN);
                }

                if (blockpos$mutableblockpos.getY() >= this.level.getMinY()) {
                    return super.createPath(blockpos$mutableblockpos.above(), accuracy);
                }

                blockpos$mutableblockpos.setY(pos.getY() + 1);

                while (blockpos$mutableblockpos.getY() <= this.level.getMaxY() && levelchunk.getBlockState(blockpos$mutableblockpos).isAir()) {
                    blockpos$mutableblockpos.move(Direction.UP);
                }

                pos = blockpos$mutableblockpos;
            }

            if (!levelchunk.getBlockState(pos).isSolid()) {
                return super.createPath(pos, accuracy);
            } else {
                BlockPos.MutableBlockPos blockpos$mutableblockpos1 = pos.mutable().move(Direction.UP);

                while (blockpos$mutableblockpos1.getY() <= this.level.getMaxY() && levelchunk.getBlockState(blockpos$mutableblockpos1).isSolid()) {
                    blockpos$mutableblockpos1.move(Direction.UP);
                }

                return super.createPath(blockpos$mutableblockpos1.immutable(), accuracy);
            }
        }
    }

    /**
     * Returns a path to the given entity or null
     */
    @Override
    public Path createPath(Entity entity, int accuracy) {
        return this.createPath(entity.blockPosition(), accuracy);
    }

    private int getSurfaceY() {
        if (this.mob.isInWater() && this.canFloat()) {
            int i = this.mob.getBlockY();
            BlockState blockstate = this.level.getBlockState(BlockPos.containing(this.mob.getX(), i, this.mob.getZ()));
            int j = 0;

            while (blockstate.is(Blocks.WATER)) {
                blockstate = this.level.getBlockState(BlockPos.containing(this.mob.getX(), ++i, this.mob.getZ()));
                if (++j > 16) {
                    return this.mob.getBlockY();
                }
            }

            return i;
        } else {
            return Mth.floor(this.mob.getY() + 0.5);
        }
    }

    @Override
    protected void trimPath() {
        super.trimPath();
        if (this.avoidSun) {
            if (this.level.canSeeSky(BlockPos.containing(this.mob.getX(), this.mob.getY() + 0.5, this.mob.getZ()))) {
                return;
            }

            for (int i = 0; i < this.path.getNodeCount(); i++) {
                Node node = this.path.getNode(i);
                if (this.level.canSeeSky(new BlockPos(node.x, node.y, node.z))) {
                    this.path.truncateNodes(i);
                    return;
                }
            }
        }
    }

    @Override
    public boolean canNavigateGround() {
        return true;
    }

    protected boolean hasValidPathType(PathType pathType) {
        if (pathType == PathType.WATER) {
            return false;
        } else {
            return pathType == PathType.LAVA ? false : pathType != PathType.OPEN;
        }
    }

    public void setAvoidSun(boolean avoidSun) {
        this.avoidSun = avoidSun;
    }

    public void setCanWalkOverFences(boolean canWalkOverFences) {
        this.nodeEvaluator.setCanWalkOverFences(canWalkOverFences);
    }
}
