package com.site21.bittermelon.common.content.blocks.dirtyfloor;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.init.neoforge.BitterGameRules;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterBlocks.DIRTY_FLOOR;

@EventBusSubscriber(modid = Bittermelon.MOD_ID)
public class DirtyBlocksHandler {
    private static final float SPAWN_CHANCE = 0.001F;

    @SubscribeEvent
    public static void onEntityTick(@NotNull EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) return;
        if (entity.level() instanceof ServerLevel level) {
            if (!level.getGameRules().getBoolean(BitterGameRules.ENTITIES_MAKE_FLOORS_DIRTY_RULE)) return;
            if (entity instanceof Player player) {
                if (player.isCreative() || player.isSpectator()) return;
            }

            BlockPos entityPos = entity.blockPosition();
            BlockState blockState = level.getBlockState(entityPos);
            if (level.random.nextFloat() > SPAWN_CHANCE) return;

            BlockState stateBelow = level.getBlockState(entityPos.below());
            if (!stateBelow.canBeReplaced() && stateBelow.isCollisionShapeFullBlock(level, entityPos.below())) {
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
