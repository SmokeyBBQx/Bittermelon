package com.site21.bittermelon.common.content.entities.mimicold.navigation;

import com.site21.bittermelon.common.content.entities.mimicold.MimicPlayer;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.Target;

public abstract class NPCNodeEvaluator {
    protected NPCPathfindingContext currentContext;
    protected MimicPlayer mob;
    protected final Int2ObjectMap<Node> nodes = new Int2ObjectOpenHashMap<>();
    protected int entityWidth;
    protected int entityHeight;
    protected int entityDepth;
    protected boolean canPassDoors = true;
    protected boolean canOpenDoors;
    protected boolean canFloat;
    protected boolean canWalkOverFences;

    public void prepare(PathNavigationRegion level, MimicPlayer mob) {
        this.currentContext = new NPCPathfindingContext(level, mob);
        this.mob = mob;
        this.nodes.clear();
        this.entityWidth = Mth.floor(mob.getBbWidth() + 1.0F);
        this.entityHeight = Mth.floor(mob.getBbHeight() + 1.0F);
        this.entityDepth = Mth.floor(mob.getBbWidth() + 1.0F);
    }

    public void done() {
        this.currentContext = null;
        this.mob = null;
    }

    protected Node getNode(BlockPos pos) {
        return this.getNode(pos.getX(), pos.getY(), pos.getZ());
    }

    /**
     * Returns a mapped point or creates and adds one
     */
    protected Node getNode(int x, int y, int z) {
        return this.nodes.computeIfAbsent(Node.createHash(x, y, z), p_77332_ -> new Node(x, y, z));
    }

    public abstract Node getStart();

    public abstract Target getTarget(double x, double y, double z);

    protected Target getTargetNodeAt(double x, double y, double z) {
        return new Target(this.getNode(Mth.floor(x), Mth.floor(y), Mth.floor(z)));
    }

    public abstract int getNeighbors(Node[] outputArray, Node node);

    public abstract PathType getPathTypeOfMob(NPCPathfindingContext context, int x, int y, int z, MimicPlayer mob);

    public abstract PathType getPathType(NPCPathfindingContext context, int x, int y, int z);

    public PathType getPathType(MimicPlayer mob, BlockPos pos) {
        return this.getPathType(new NPCPathfindingContext(mob.level(), mob), pos.getX(), pos.getY(), pos.getZ());
    }

    public void setCanPassDoors(boolean canEnterDoors) {
        this.canPassDoors = canEnterDoors;
    }

    public void setCanOpenDoors(boolean canOpenDoors) {
        this.canOpenDoors = canOpenDoors;
    }

    public void setCanFloat(boolean canFloat) {
        this.canFloat = canFloat;
    }

    public void setCanWalkOverFences(boolean canWalkOverFences) {
        this.canWalkOverFences = canWalkOverFences;
    }

    public boolean canPassDoors() {
        return this.canPassDoors;
    }

    public boolean canOpenDoors() {
        return this.canOpenDoors;
    }

    public boolean canFloat() {
        return this.canFloat;
    }

    public boolean canWalkOverFences() {
        return this.canWalkOverFences;
    }

    public static boolean isBurningBlock(BlockState state) {
        return state.is(BlockTags.FIRE)
                || state.is(Blocks.LAVA)
                || state.is(Blocks.MAGMA_BLOCK)
                || CampfireBlock.isLitCampfire(state)
                || state.is(Blocks.LAVA_CAULDRON);
    }
}
