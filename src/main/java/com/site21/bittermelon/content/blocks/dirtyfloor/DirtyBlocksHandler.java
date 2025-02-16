package com.site21.bittermelon.content.blocks.dirtyfloor;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

import static com.site21.bittermelon.init.BitterBlocks.DIRTY_FLOOR;

@EventBusSubscriber(modid = Bittermelon.MOD_ID)
public class DirtyBlocksHandler {
    private static final float SPAWN_CHANCE = 0.001F;
    private static final Random random = new Random();

    @SubscribeEvent
    public static void onEntityTick(@NotNull EntityTickEvent.Post event) {
        if (!event.getEntity().level().isClientSide) {
            Level level = event.getEntity().level();
            BlockPos entityPos = event.getEntity().blockPosition();
            BlockPos belowPos = entityPos.below();
            if (random.nextFloat() < SPAWN_CHANCE) {
                BlockState blockState = level.getBlockState(entityPos);

                if (!level.getBlockState(belowPos).isAir()) {
                    if (blockState.isAir()) {
                        BlockState dirtyState = DIRTY_FLOOR.get().defaultBlockState()
                                .setValue(DirtyFloorBlock.DIRTINESS, 0)
                                .setValue(DirtyFloorBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(level.random));
                        level.setBlock(entityPos, dirtyState, 3);
                    } else if (blockState.getBlock() instanceof DirtyFloorBlock) {
                        IntegerProperty LEVEL = DirtyFloorBlock.DIRTINESS;
                        int dirtyLevel = blockState.getValue(LEVEL);
                        if (dirtyLevel < 2) {
                            level.setBlock(entityPos, blockState.setValue(LEVEL, dirtyLevel + 1), 3);
                        }
                    }
                }
            }
        }
    }
}
