package com.site21.bittermelon.content.entities.ai.behavior.movement;

import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.player.Player;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SeekNearestPlayer<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private int distanceThreshold = 50;
    private double searchDistance = 200;

    public SeekNearestPlayer<E> distanceThreshold(int distanceThreshold) {
        this.distanceThreshold = distanceThreshold;
        return this;
    }

    public SeekNearestPlayer<E> searchDistance(double searchDistance) {
        this.searchDistance = searchDistance;
        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of();
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull E entity) {
        Player nearestPlayer = level.getNearestPlayer(entity, searchDistance);
        return nearestPlayer != null && nearestPlayer.distanceTo(entity) > distanceThreshold;
    }

    @Override
    protected void start(@NotNull E entity) {
        Player nearestPlayer = entity.level().getNearestPlayer(entity, searchDistance);

        if (nearestPlayer == null) return;
        BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(nearestPlayer.getOnPos(), 1.5f, distanceThreshold));
    }
}
