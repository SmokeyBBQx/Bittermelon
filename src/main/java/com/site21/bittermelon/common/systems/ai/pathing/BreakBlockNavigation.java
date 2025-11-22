package com.site21.bittermelon.common.systems.ai.pathing;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.tslat.smartbrainlib.api.core.navigation.SmoothGroundNavigation;
import org.jetbrains.annotations.NotNull;

public class BreakBlockNavigation extends SmoothGroundNavigation {
    public BreakBlockNavigation(Mob mob, Level level) {
        super(mob, level);
        this.nodeEvaluator = new BreakBlockEvaluator();
    }

    @Override
    protected @NotNull PathFinder createPathFinder(int maxVisitedNodes) {
        this.nodeEvaluator = new BreakBlockEvaluator();
        this.nodeEvaluator.setCanPassDoors(true);

        return createSmoothPathFinder(this.nodeEvaluator, maxVisitedNodes);
    }
}
