package com.site21.bittermelon.common.content.entities.cage;

import com.site21.bittermelon.common.content.entities.cage.client.BlockInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.*;

public class CageBlock extends Block {
    public CageBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!player.isShiftKeyDown()) return InteractionResult.FAIL;

        Cage cage = Cage.create(level, findCageBlocks(level, pos, 256));
        cage.setPos(pos.getX(), pos.getY(), pos.getZ());
        level.addFreshEntity(cage);

        return InteractionResult.SUCCESS;
    }

    public static List<BlockInfo> findCageBlocks(Level level, BlockPos origin, int maxBlocks) {
        List<BlockInfo> cageBlocks = new ArrayList<>();
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();

        queue.add(origin);

        while (!queue.isEmpty() && cageBlocks.size() < maxBlocks) {
            BlockPos currentPos = queue.poll();
            if (visited.contains(currentPos)) continue;
            visited.add(currentPos);

            BlockState state = level.getBlockState(currentPos);
            if (!state.isAir()) {
                Vec3i offset = currentPos.subtract(origin);
                offset = new Vec3i(Math.abs(offset.getX()), Math.abs(offset.getY()), Math.abs(offset.getZ()));
                cageBlocks.add(new BlockInfo(state, offset));

                for (Direction direction : Direction.values()) {
                    BlockPos neighborPos = currentPos.relative(direction);
                    if (!visited.contains(neighborPos)) {
                        queue.add(neighborPos);
                    }
                }

                level.removeBlock(currentPos, false);
            }
        }

        return cageBlocks;
    }
}
