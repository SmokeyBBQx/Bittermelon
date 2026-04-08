package com.site21.bittermelon.common.content.entities.mimicold.navigation;

import com.google.common.collect.ImmutableSet;
import com.site21.bittermelon.common.content.entities.mimicold.MimicPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class NPCNavigation {
    private static final int MAX_TIME_RECOMPUTE = 20;
    private static final int STUCK_CHECK_INTERVAL = 100;
    private static final float STUCK_THRESHOLD_DISTANCE_FACTOR = 0.25F;
    protected final MimicPlayer mob;
    protected final Level level;
    @Nullable
    protected Path path;
    protected double speedModifier;
    protected int tick;
    protected int lastStuckCheck;
    protected Vec3 lastStuckCheckPos = Vec3.ZERO;
    protected Vec3i timeoutCachedNode = Vec3i.ZERO;
    protected long timeoutTimer;
    protected long lastTimeoutCheck;
    protected double timeoutLimit;
    protected float maxDistanceToWaypoint = 0.5F;
    protected boolean hasDelayedRecomputation;
    protected long timeLastRecompute;
    protected NPCNodeEvaluator nodeEvaluator;
    @Nullable
    private BlockPos targetPos;
    /**
     * Distance in which a path point counts as target-reaching
     */
    private int reachRange;
    private float maxVisitedNodesMultiplier = 1.0F;
    private final NPCPathfinder pathFinder;
    private boolean isStuck;
    private float requiredPathLength = 16.0F;

    public NPCNavigation(MimicPlayer mob, Level level) {
        this.mob = mob;
        this.level = level;
        this.pathFinder = this.createPathFinder(Mth.floor(32.0 * 16.0));
    }

    public void updatePathfinderMaxVisitedNodes() {
        int i = Mth.floor(this.getMaxPathLength() * 16.0F);
        this.pathFinder.setMaxVisitedNodes(i);
    }

    public void setRequiredPathLength(float requiredPathLength) {
        this.requiredPathLength = requiredPathLength;
        this.updatePathfinderMaxVisitedNodes();
    }

    private float getMaxPathLength() {
        return Math.max((float)this.mob.getAttributeValue(Attributes.FOLLOW_RANGE), this.requiredPathLength);
    }

    public void resetMaxVisitedNodesMultiplier() {
        this.maxVisitedNodesMultiplier = 1.0F;
    }

    public void setMaxVisitedNodesMultiplier(float multiplier) {
        this.maxVisitedNodesMultiplier = multiplier;
    }

    @Nullable
    public BlockPos getTargetPos() {
        return this.targetPos;
    }

    protected abstract NPCPathfinder createPathFinder(int maxVisitedNodes);

    /**
     * Sets the speed
     */
    public void setSpeedModifier(double speed) {
        this.speedModifier = speed;
    }

    public void recomputePath() {
        if (this.level.getGameTime() - this.timeLastRecompute > 20L) {
            if (this.targetPos != null) {
                this.path = null;
                this.path = this.createPath(this.targetPos, this.reachRange);
                this.timeLastRecompute = this.level.getGameTime();
                this.hasDelayedRecomputation = false;
            }
        } else {
            this.hasDelayedRecomputation = true;
        }
    }

    /**
     * Returns path to given BlockPos
     */
    @Nullable
    public final Path createPath(double x, double y, double z, int accuracy) {
        return this.createPath(BlockPos.containing(x, y, z), accuracy);
    }

    /**
     * Returns a path to one of the elements of the stream or null
     */
    @Nullable
    public Path createPath(Stream<BlockPos> targets, int accuracy) {
        return this.createPath(targets.collect(Collectors.toSet()), 8, false, accuracy);
    }

    @Nullable
    public Path createPath(Set<BlockPos> positions, int distance) {
        return this.createPath(positions, 8, false, distance);
    }

    /**
     * Returns path to given BlockPos
     */
    @Nullable
    public Path createPath(BlockPos pos, int accuracy) {
        return this.createPath(ImmutableSet.of(pos), 8, false, accuracy);
    }

    @Nullable
    public Path createPath(BlockPos pos, int regionOffset, int accuracy) {
        return this.createPath(ImmutableSet.of(pos), 8, false, regionOffset, accuracy);
    }

    /**
     * Returns a path to the given entity or null
     */
    @Nullable
    public Path createPath(Entity entity, int accuracy) {
        return this.createPath(ImmutableSet.of(entity.blockPosition()), 16, true, accuracy);
    }

    /**
     * Returns a path to one of the given targets or null
     */
    @Nullable
    protected Path createPath(Set<BlockPos> targets, int regionOffset, boolean offsetUpward, int accuracy) {
        return this.createPath(targets, regionOffset, offsetUpward, accuracy, this.getMaxPathLength());
    }

    @Nullable
    protected Path createPath(Set<BlockPos> targets, int regionOffset, boolean offsetUpward, int accuracy, float followRange) {
        if (targets.isEmpty()) {
            return null;
        } else if (this.mob.getY() < this.level.getMinY()) {
            return null;
        } else if (!this.canUpdatePath()) {
            return null;
        } else if (this.path != null && !this.path.isDone() && targets.contains(this.targetPos)) {
            return this.path;
        } else {
            ProfilerFiller profilerfiller = Profiler.get();
            profilerfiller.push("pathfind");
            BlockPos blockpos = offsetUpward ? this.mob.blockPosition().above() : this.mob.blockPosition();
            int i = (int)(followRange + regionOffset);
            PathNavigationRegion pathnavigationregion = new PathNavigationRegion(this.level, blockpos.offset(-i, -i, -i), blockpos.offset(i, i, i));
            Path path = this.pathFinder.findPath(pathnavigationregion, this.mob, targets, followRange, accuracy, this.maxVisitedNodesMultiplier);
            profilerfiller.pop();
            if (path != null && path.getTarget() != null) {
                this.targetPos = path.getTarget();
                this.reachRange = accuracy;
                this.resetStuckTimeout();
            }

            return path;
        }
    }

    /**
     * Try to find and set a path to XYZ. Returns {@code true} if successful.
     */
    public boolean moveTo(double x, double y, double z, double speed) {
        return this.moveTo(this.createPath(x, y, z, 1), speed);
    }

    public boolean moveTo(double x, double y, double z, int accuracy, double speed) {
        return this.moveTo(this.createPath(x, y, z, accuracy), speed);
    }

    /**
     * Try to find and set a path to EntityLiving. Returns {@code true} if successful.
     */
    public boolean moveTo(Entity entity, double speed) {
        Path path = this.createPath(entity, 1);
        return path != null && this.moveTo(path, speed);
    }

    /**
     * Sets a new path. If it's different from the old path. Checks to adjust path for sun avoiding, and stores start coords.
     */
    public boolean moveTo(@Nullable Path pathEntity, double speed) {
        if (pathEntity == null) {
            this.path = null;
            return false;
        } else {
            if (!pathEntity.sameAs(this.path)) {
                this.path = pathEntity;
            }

            if (this.isDone()) {
                return false;
            } else {
                this.trimPath();
                if (this.path.getNodeCount() <= 0) {
                    return false;
                } else {
                    this.speedModifier = speed;
                    Vec3 vec3 = this.getTempMobPos();
                    this.lastStuckCheck = this.tick;
                    this.lastStuckCheckPos = vec3;
                    return true;
                }
            }
        }
    }

    @Nullable
    public Path getPath() {
        return this.path;
    }

    public void tick() {
        this.tick++;
        if (this.hasDelayedRecomputation) {
            this.recomputePath();
        }

        if (!this.isDone()) {
            if (this.canUpdatePath()) {
                this.followThePath();
            } else if (this.path != null && !this.path.isDone()) {
                Vec3 vec3 = this.getTempMobPos();
                Vec3 vec31 = this.path.getNextEntityPos(this.mob);
                if (vec3.y > vec31.y && !this.mob.onGround() && Mth.floor(vec3.x) == Mth.floor(vec31.x) && Mth.floor(vec3.z) == Mth.floor(vec31.z)) {
                    this.path.advance();
                }
            }

            if (!this.isDone()) {
                Vec3 vec32 = this.path.getNextEntityPos(this.mob);
                this.mob.getMoveControl().setWantedPosition(vec32.x, this.getGroundY(vec32), vec32.z, this.speedModifier);
            }
        }
    }

    protected double getGroundY(Vec3 vec) {
        BlockPos blockpos = BlockPos.containing(vec);
        return this.level.getBlockState(blockpos.below()).isAir() ? vec.y : WalkNodeEvaluator.getFloorLevel(this.level, blockpos);
    }

    protected void followThePath() {
        Vec3 vec3 = this.getTempMobPos();
        this.maxDistanceToWaypoint = this.mob.getBbWidth() > 0.75F ? this.mob.getBbWidth() / 2.0F : 0.75F - this.mob.getBbWidth() / 2.0F;
        Vec3i vec3i = this.path.getNextNodePos();
        double d0 = Math.abs(this.mob.getX() - (vec3i.getX() + ((int)(this.mob.getBbWidth() + 1)) / 2D)); //Forge: Fix MC-94054. The casting to int must match what is done in Path#getEntityPosAtNode
        double d1 = Math.abs(this.mob.getY() - vec3i.getY());
        double d2 = Math.abs(this.mob.getZ() - (vec3i.getZ() + ((int)(this.mob.getBbWidth() + 1)) / 2D)); //Forge: Fix MC-94054. The casting to int must match what is done in Path#getEntityPosAtNode
        boolean flag = d0 <= this.maxDistanceToWaypoint && d2 <= this.maxDistanceToWaypoint && d1 < 1.0; //Forge: Fix MC-94054
        if (flag || this.canCutCorner(this.path.getNextNode().type) && this.shouldTargetNextNodeInDirection(vec3)) {
            this.path.advance();
        }

        this.doStuckDetection(vec3);
    }

    private boolean shouldTargetNextNodeInDirection(Vec3 vec) {
        if (this.path.getNextNodeIndex() + 1 >= this.path.getNodeCount()) {
            return false;
        } else {
            Vec3 vec3 = Vec3.atBottomCenterOf(this.path.getNextNodePos());
            if (!vec.closerThan(vec3, 2.0)) {
                return false;
            } else if (this.canMoveDirectly(vec, this.path.getNextEntityPos(this.mob))) {
                return true;
            } else {
                Vec3 vec31 = Vec3.atBottomCenterOf(this.path.getNodePos(this.path.getNextNodeIndex() + 1));
                Vec3 vec32 = vec3.subtract(vec);
                Vec3 vec33 = vec31.subtract(vec);
                double d0 = vec32.lengthSqr();
                double d1 = vec33.lengthSqr();
                boolean flag = d1 < d0;
                boolean flag1 = d0 < 0.5;
                if (!flag && !flag1) {
                    return false;
                } else {
                    Vec3 vec34 = vec32.normalize();
                    Vec3 vec35 = vec33.normalize();
                    return vec35.dot(vec34) < 0.0;
                }
            }
        }
    }

    /**
     * Checks if entity haven't been moved when last checked and if so, stops the current navigation.
     */
    protected void doStuckDetection(Vec3 positionVec3) {
        if (this.tick - this.lastStuckCheck > 100) {
            float f = this.mob.getSpeed() >= 1.0F ? this.mob.getSpeed() : this.mob.getSpeed() * this.mob.getSpeed();
            float f1 = f * 100.0F * 0.25F;
            if (positionVec3.distanceToSqr(this.lastStuckCheckPos) < f1 * f1) {
                this.isStuck = true;
                this.stop();
            } else {
                this.isStuck = false;
            }

            this.lastStuckCheck = this.tick;
            this.lastStuckCheckPos = positionVec3;
        }

        if (this.path != null && !this.path.isDone()) {
            Vec3i vec3i = this.path.getNextNodePos();
            long i = this.level.getGameTime();
            if (vec3i.equals(this.timeoutCachedNode)) {
                this.timeoutTimer = this.timeoutTimer + (i - this.lastTimeoutCheck);
            } else {
                this.timeoutCachedNode = vec3i;
                double d0 = positionVec3.distanceTo(Vec3.atBottomCenterOf(this.timeoutCachedNode));
                this.timeoutLimit = this.mob.getSpeed() > 0.0F ? d0 / this.mob.getSpeed() * 20.0 : 0.0;
            }

            if (this.timeoutLimit > 0.0 && this.timeoutTimer > this.timeoutLimit * 3.0) {
                this.timeoutPath();
            }

            this.lastTimeoutCheck = i;
        }
    }

    private void timeoutPath() {
        this.resetStuckTimeout();
        this.stop();
    }

    private void resetStuckTimeout() {
        this.timeoutCachedNode = Vec3i.ZERO;
        this.timeoutTimer = 0L;
        this.timeoutLimit = 0.0;
        this.isStuck = false;
    }

    public boolean isDone() {
        return this.path == null || this.path.isDone();
    }

    public boolean isInProgress() {
        return !this.isDone();
    }

    public void stop() {
        this.path = null;
    }

    protected abstract Vec3 getTempMobPos();

    protected abstract boolean canUpdatePath();

    protected void trimPath() {
        if (this.path != null) {
            for (int i = 0; i < this.path.getNodeCount(); i++) {
                Node node = this.path.getNode(i);
                Node node1 = i + 1 < this.path.getNodeCount() ? this.path.getNode(i + 1) : null;
                BlockState blockstate = this.level.getBlockState(new BlockPos(node.x, node.y, node.z));
                if (blockstate.is(BlockTags.CAULDRONS)) {
                    this.path.replaceNode(i, node.cloneAndMove(node.x, node.y + 1, node.z));
                    if (node1 != null && node.y >= node1.y) {
                        this.path.replaceNode(i + 1, node.cloneAndMove(node1.x, node.y + 1, node1.z));
                    }
                }
            }
        }
    }

    /**
     * Checks if the specified entity can safely walk to the specified location.
     */
    protected boolean canMoveDirectly(Vec3 posVec31, Vec3 posVec32) {
        return false;
    }

    public boolean canCutCorner(PathType pathType) {
        return pathType != PathType.DANGER_FIRE && pathType != PathType.DANGER_OTHER && pathType != PathType.WALKABLE_DOOR;
    }

    protected static boolean isClearForMovementBetween(Mob mob, Vec3 pos1, Vec3 pos2, boolean allowSwimming) {
        Vec3 vec3 = new Vec3(pos2.x, pos2.y + mob.getBbHeight() * 0.5, pos2.z);
        return mob.level()
                .clip(new ClipContext(pos1, vec3, ClipContext.Block.COLLIDER, allowSwimming ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE, mob))
                .getType()
                == HitResult.Type.MISS;
    }

    public boolean isStableDestination(BlockPos pos) {
        BlockPos blockpos = pos.below();
        return this.level.getBlockState(blockpos).isSolidRender();
    }

    public NPCNodeEvaluator getNodeEvaluator() {
        return this.nodeEvaluator;
    }

    public void setCanFloat(boolean canSwim) {
        this.nodeEvaluator.setCanFloat(canSwim);
    }

    public boolean canFloat() {
        return this.nodeEvaluator.canFloat();
    }

    public boolean shouldRecomputePath(BlockPos pos) {
        if (this.hasDelayedRecomputation) {
            return false;
        } else if (this.path != null && !this.path.isDone() && this.path.getNodeCount() != 0) {
            Node node = this.path.getEndNode();
            Vec3 vec3 = new Vec3((node.x + this.mob.getX()) / 2.0, (node.y + this.mob.getY()) / 2.0, (node.z + this.mob.getZ()) / 2.0);
            return pos.closerToCenterThan(vec3, this.path.getNodeCount() - this.path.getNextNodeIndex());
        } else {
            return false;
        }
    }

    public float getMaxDistanceToWaypoint() {
        return this.maxDistanceToWaypoint;
    }

    public boolean isStuck() {
        return this.isStuck;
    }

    public abstract boolean canNavigateGround();

    public void setCanOpenDoors(boolean canOpenDoors) {
        this.nodeEvaluator.setCanOpenDoors(canOpenDoors);
    }
}
