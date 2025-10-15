package com.site21.bittermelon.common.content.entities.scp650.behavior;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.common.content.entities.scp650.SCP650;
import com.site21.bittermelon.common.content.entities.scp650.networking.SetEntityPos;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.site21.bittermelon.init.neoforge.BitterMemoryTypes.*;

public class TeleportBehindRandomTarget<E extends SCP650> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(SCARE_TARGET.get(), MemoryStatus.VALUE_ABSENT),
            Pair.of(OBSERVERS.get(), MemoryStatus.VALUE_ABSENT)
    );

    ServerPlayer randomPlayer;

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull E entity) {
        randomPlayer = level.getRandomPlayer();
        return randomPlayer != null;
    }

    @Override
    protected void start(@NotNull E entity) {
        Vec3 playerPos = randomPlayer.getPosition(1f);
        Vec3 lookAngle = randomPlayer.getLookAngle();

        // Move entity one block's distance in the other direction.
        Vec3 teleportPos = playerPos.subtract(lookAngle.x, 0, lookAngle.z);

        if (!isSafeTeleportLocation(entity.level(), teleportPos, entity)) return;

        // Calculate the y-rotation/yaw from teleport position to player position.
        Vec3 directionToPlayer = playerPos.subtract(teleportPos).normalize();
        float yaw = (float) (Mth.atan2(-directionToPlayer.x, directionToPlayer.z) * (180D / Math.PI));

        // Send a packet to clientside to avoid tweening when teleporting.
        PacketDistributor.sendToPlayersTrackingEntity(entity, new SetEntityPos(teleportPos.toVector3f(), entity.getId(), yaw));
        entity.setPos(teleportPos);
        entity.setYRot(yaw);

        BrainUtil.setForgettableMemory(entity, SCARE_TARGET.get(), randomPlayer, 2400);

        // REPEAT TORMENTING LOGIC
        Map<UUID, Integer> timesScared = BrainUtil.memoryOrDefault(entity, TIMES_TARGET_SCARED.get(), HashMap::new);
        timesScared.compute(randomPlayer.getUUID(), (k, v) -> (v == null ? 0 : v) + 1);
        BrainUtil.setMemory(entity, TIMES_TARGET_SCARED.get(), timesScared);

        // If the player has been scared 10 times, it'll get guilt poses instead.
        entity.setPose(entity.getRandomPose(timesScared.getOrDefault(randomPlayer.getUUID(), 0) >= 10));
    }

    private boolean isSafeTeleportLocation(@NotNull Level level, Vec3 pos, Entity entity) {
        BlockState groundBlock = level.getBlockState(BlockPos.containing(pos).below());

        if (!groundBlock.isSolid() || groundBlock.isAir()) {
            return false;
        }

        AABB entityBounds = entity.getBoundingBox().move(pos.subtract(entity.position()));
        return level.noCollision(entity, entityBounds);
    }
}
