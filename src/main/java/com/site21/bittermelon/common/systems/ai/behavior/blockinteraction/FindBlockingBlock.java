package com.site21.bittermelon.common.systems.ai.behavior.blockinteraction;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.init.neoforge.BitterMemoryTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.object.MemoryTest;
import net.tslat.smartbrainlib.util.BrainUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FindBlockingBlock<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder()
            .hasMemory(MemoryModuleType.ATTACK_TARGET)
            .noMemory(BitterMemoryTypes.BREAK_TARGET.get());

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(@NotNull ServerLevel level, @NotNull E entity) {
//        System.out.println("Checking extra start conditions for FindBlockingBlock");
//        Path path = BrainUtil.getMemory(entity, MemoryModuleType.PATH);
//        return path != null && !path.isDone();
        return true;
    }

    @Override
    protected void start(E entity) {
        LivingEntity target = entity.getTarget();
        if (target == null) return;

        int entityHeight = (int) entity.getBbHeight() + 1;
        BlockPos closestBlock = null;
        double closestDistance = Double.MAX_VALUE;

        // Cast rays at different heights to find blocking blocks
        for (int i = 0; i < entityHeight; i++) {
            Vec3 startPos = entity.position().add(0.0D, i + 0.5D, 0.0D);
            Vec3 endPos = target.position().add(0.0D, i, 0.0D);

            BlockHitResult hitResult = entity.level().clip(new ClipContext(
                    startPos,
                    endPos,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    entity
            ));

            if (hitResult.getType() != HitResult.Type.MISS) {
                BlockPos hitPos = hitResult.getBlockPos();
                BlockState state = entity.level().getBlockState(hitPos);
                double distance = entity.position().distanceTo(Vec3.atCenterOf(hitPos));

                // Validate block is breakable and within reach
                if (!state.isAir() &&
                        distance <= 2.5D &&
                        state.getDestroySpeed(entity.level(), hitPos) >= 0.0F &&
                        distance < closestDistance) {
                    closestBlock = hitPos;
                    closestDistance = distance;
                }
            }
        }

        if (closestBlock != null) {
            System.out.println("Found blocking block at: " + closestBlock);
            BrainUtil.setMemory(entity, BitterMemoryTypes.BREAK_TARGET.get(), closestBlock);
        }
    }
}
