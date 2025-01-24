package com.site21.bittermelon.entities.ai.behavior.movement;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FindDarkness<E extends PathfinderMob> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT),
            Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED));

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean doStartCheck(@NotNull ServerLevel level, E entity, long gameTime) {
        return level.isDay() && !isInDarkness(level, entity);
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull E entity, long gameTime) {
        BlockPos targetPos = findDarkArea(level, entity);
        if (targetPos != null) {
            BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(targetPos, 2.5F, 0));
        }
    }

    private boolean isInDarkness(@NotNull ServerLevel level, @NotNull E entity) {
        return level.getMaxLocalRawBrightness(new BlockPos((int) entity.getX(), (int) entity.getEyeY(),
                (int) entity.getZ())) < 8;
    }

    private @Nullable BlockPos findDarkArea(ServerLevel level, E entity) {
        for (var i = 0; i < 10; i++) {
            var x = (int) (entity.getX() + entity.getRandom().nextInt(20) - 10);
            var z = (int) (entity.getZ() + entity.getRandom().nextInt(20) - 10);
            var y = level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
            var pos = new BlockPos(x, y, z);
            if (level.getBlockState(pos).getBlock() == Blocks.AIR && level.getMaxLocalRawBrightness(pos) < 8) {
                return pos;
            }
        }
        return null;
    }
}
