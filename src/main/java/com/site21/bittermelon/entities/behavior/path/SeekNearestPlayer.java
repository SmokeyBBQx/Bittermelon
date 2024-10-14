package com.site21.bittermelon.entities.behavior.path;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.entities.scps.SCP939.SCP939;
import com.site21.bittermelon.init.MemoryModuleTypeInit;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.player.Player;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SeekNearestPlayer<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder(1).noMemory(MemoryModuleType.WALK_TARGET);
    private final int DISTANCE_THRESHOLD = 15;
    private ServerPlayer nearestPlayer;

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of();
    }

    @Override
    protected void start(E entity) {
        List<Player> nearestPlayers = BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_PLAYERS);
        if (nearestPlayers != null && !nearestPlayers.isEmpty()) {
            nearestPlayer = (ServerPlayer) nearestPlayers.get(0);
        }

        BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(nearestPlayer.position(), 1.5f, DISTANCE_THRESHOLD));
    }
}
