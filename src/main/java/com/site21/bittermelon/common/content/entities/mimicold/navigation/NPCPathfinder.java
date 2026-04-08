package com.site21.bittermelon.common.content.entities.mimicold.navigation;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.site21.bittermelon.common.content.entities.mimicold.MimicPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.profiling.metrics.MetricCategory;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.BinaryHeap;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.Target;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NPCPathfinder {
    private static final float FUDGING = 1.5F;
    private final Node[] neighbors = new Node[32];
    private int maxVisitedNodes;
    private final NPCNodeEvaluator nodeEvaluator;
    private static final boolean DEBUG = false;
    private final BinaryHeap openSet = new BinaryHeap();

    public NPCPathfinder(NPCNodeEvaluator nodeEvaluator, int maxVisitedNodes) {
        this.nodeEvaluator = nodeEvaluator;
        this.maxVisitedNodes = maxVisitedNodes;
    }

    public void setMaxVisitedNodes(int maxVisitedNodes) {
        this.maxVisitedNodes = maxVisitedNodes;
    }

    /**
     * Finds a path to one of the specified positions and post-processes it or returns null if no path could be found within given accuracy
     */
    @Nullable
    public Path findPath(PathNavigationRegion region, MimicPlayer mob, Set<BlockPos> targetPositions, float maxRange, int accuracy, float searchDepthMultiplier) {
        this.openSet.clear();
        this.nodeEvaluator.prepare(region, mob);
        Node node = this.nodeEvaluator.getStart();
        if (node == null) {
            return null;
        } else {
            Map<Target, BlockPos> map = targetPositions.stream()
                    .collect(Collectors.toMap(p_326774_ -> this.nodeEvaluator.getTarget(p_326774_.getX(), p_326774_.getY(), p_326774_.getZ()), Function.identity()));
            Path path = this.findPath(node, map, maxRange, accuracy, searchDepthMultiplier);
            this.nodeEvaluator.done();
            return path;
        }
    }

    /**
     * Finds a path to one of the specified positions and post-processes it or returns null if no path could be found within given accuracy
     */
    @Nullable
    private Path findPath(Node p_node, Map<Target, BlockPos> targetPositions, float maxRange, int accuracy, float searchDepthMultiplier) {
        ProfilerFiller profilerfiller = Profiler.get();
        profilerfiller.push("find_path");
        profilerfiller.markForCharting(MetricCategory.PATH_FINDING);
        Set<Target> set = targetPositions.keySet();
        p_node.g = 0.0F;
        p_node.h = this.getBestH(p_node, set);
        p_node.f = p_node.h;
        this.openSet.clear();
        this.openSet.insert(p_node);
        Set<Node> set1 = ImmutableSet.of();
        int i = 0;
        Set<Target> set2 = Sets.newHashSetWithExpectedSize(set.size());
        int j = (int)(this.maxVisitedNodes * searchDepthMultiplier);

        while (!this.openSet.isEmpty()) {
            if (++i >= j) {
                break;
            }

            Node node = this.openSet.pop();
            node.closed = true;

            for (Target target : set) {
                if (node.distanceManhattan(target) <= accuracy) {
                    target.setReached();
                    set2.add(target);
                }
            }

            if (!set2.isEmpty()) {
                break;
            }

            if (!(node.distanceTo(p_node) >= maxRange)) {
                int k = this.nodeEvaluator.getNeighbors(this.neighbors, node);

                for (int l = 0; l < k; l++) {
                    Node node1 = this.neighbors[l];
                    float f = this.distance(node, node1);
                    node1.walkedDistance = node.walkedDistance + f;
                    float f1 = node.g + f + node1.costMalus;
                    if (node1.walkedDistance < maxRange && (!node1.inOpenSet() || f1 < node1.g)) {
                        node1.cameFrom = node;
                        node1.g = f1;
                        node1.h = this.getBestH(node1, set) * 1.5F;
                        if (node1.inOpenSet()) {
                            this.openSet.changeCost(node1, node1.g + node1.h);
                        } else {
                            node1.f = node1.g + node1.h;
                            this.openSet.insert(node1);
                        }
                    }
                }
            }
        }

        Optional<Path> optional = !set2.isEmpty()
                ? set2.stream()
                .map(p_77454_ -> this.reconstructPath(p_77454_.getBestNode(), targetPositions.get(p_77454_), true))
                .min(Comparator.comparingInt(Path::getNodeCount))
                : set.stream()
                .map(p_77451_ -> this.reconstructPath(p_77451_.getBestNode(), targetPositions.get(p_77451_), false))
                .min(Comparator.comparingDouble(Path::getDistToTarget).thenComparingInt(Path::getNodeCount));
        profilerfiller.pop();
        return optional.isEmpty() ? null : optional.get();
    }

    protected float distance(Node first, Node second) {
        return first.distanceTo(second);
    }

    private float getBestH(Node node, Set<Target> targets) {
        float f = Float.MAX_VALUE;

        for (Target target : targets) {
            float f1 = node.distanceTo(target);
            target.updateBest(f1, node);
            f = Math.min(f1, f);
        }

        return f;
    }

    /**
     * Converts a recursive path point structure into a path
     */
    private Path reconstructPath(Node point, BlockPos targetPos, boolean reachesTarget) {
        List<Node> list = Lists.newArrayList();
        Node node = point;
        list.add(0, point);

        while (node.cameFrom != null) {
            node = node.cameFrom;
            list.add(0, node);
        }

        return new Path(list, targetPos, reachesTarget);
    }
}
