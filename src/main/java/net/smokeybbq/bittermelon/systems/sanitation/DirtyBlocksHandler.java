package net.smokeybbq.bittermelon.systems.sanitation;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.smokeybbq.bittermelon.blocks.DirtyDecalBlock;

import java.util.Random;

import static net.smokeybbq.bittermelon.init.BlockInit.DIRTY;

public class DirtyBlocksHandler {
    private static final float SPAWN_CHANCE = 0.001F;
    private static final Random random = new Random();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.player.level().isClientSide) {
            Level level = event.player.level();
            BlockPos playerPos = event.player.blockPosition();
            BlockPos belowPos = playerPos.below();
            if (random.nextFloat() < SPAWN_CHANCE) {
                BlockState blockState = level.getBlockState(playerPos);

                if (!level.getBlockState(belowPos).isAir()) {
                    if (blockState.isAir()) {
                        BlockState dirtyState = DIRTY.get().defaultBlockState()
                                .setValue(DirtyDecalBlock.LEVEL, 0)
                                .setValue(DirtyDecalBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(level.random));
                        level.setBlock(playerPos, dirtyState, 3);
                    } else if (blockState.getBlock() instanceof DirtyDecalBlock) {
                        IntegerProperty LEVEL = DirtyDecalBlock.LEVEL;
                        int dirtyLevel = blockState.getValue(LEVEL);
                        if (dirtyLevel < 2) {
                            level.setBlock(playerPos, blockState.setValue(LEVEL, dirtyLevel + 1), 3);
                        }
                    }
                }
            }
        }
    }
}
