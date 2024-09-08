package net.smokeybbq.bittermelon.systems.atmospherics;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.smokeybbq.bittermelon.Bittermelon;
import net.smokeybbq.bittermelon.util.ModLogger;

@Mod.EventBusSubscriber(modid = Bittermelon.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AtmosEventHandler {
    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (event.getChunk() instanceof LevelChunk chunk) {
            AtmosManager atmosManager = AtmosManager.getInstance();
            if (atmosManager != null) {
                atmosManager.onChunkLoad(chunk.getPos());
            }
        }
    }

    @SubscribeEvent
    public static void onChunkUnload(ChunkEvent.Unload event) {
        if (event.getChunk() instanceof LevelChunk chunk) {
            AtmosManager atmosManager = AtmosManager.getInstance();
            if (atmosManager != null) {
                atmosManager.onChunkUnload(chunk.getPos());
            }
        }
    }

    @SubscribeEvent
    public static void onBlockChange(BlockEvent.BreakEvent event) {
        handleBlockUpdate(event.getState(), event.getPos(), event.getLevel());
        ModLogger.debug("Block break event called!");
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        handleBlockUpdate(event.getState(), event.getPos(), event.getLevel());
        ModLogger.debug("Block place event called!");
    }

    @SubscribeEvent
    public static void onNeighborNotify(BlockEvent.NeighborNotifyEvent event) {
        // TODO: Find out why it only works if this event is called
//
//        handleBlockUpdate(event.getState(), event.getPos(), event.getLevel());
    }

    private static void handleBlockUpdate(BlockState state, BlockPos pos, LevelAccessor levelAccessor) {
        if (levelAccessor.isClientSide()) {
            ModLogger.debug("is client side!");
            return;
        }

        AtmosManager atmosManager = AtmosManager.getInstance();
        if (atmosManager == null) {
            ModLogger.warn("AtmosManager is null during block update at " + pos);
            return;
        }

        boolean isAir = state.isAir() || (state.getBlock() instanceof SimpleWaterloggedBlock) || state.canBeReplaced();
        atmosManager.onBlockChange(pos, isAir);
    }
}
