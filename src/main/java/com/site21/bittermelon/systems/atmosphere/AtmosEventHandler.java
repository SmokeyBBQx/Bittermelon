package com.site21.bittermelon.systems.atmosphere;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.systems.atmosphere.data.AtmosBlockData;
import com.site21.bittermelon.init.neoforge.BitterBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.ATMOSPHERE;

@EventBusSubscriber(modid = Bittermelon.MOD_ID)
public class AtmosEventHandler {

    @SubscribeEvent
    public static void onBlockUpdate(BlockEvent.@NotNull NeighborNotifyEvent event) {
        if (event.getLevel() instanceof Level level) {
            if (level.isClientSide) return;
            BlockPos pos = event.getPos();

            if (level.getBlockState(pos).is(BitterBlockTags.PASSES_ATMOS)) return;

            LevelChunk chunk = level.getChunkAt(pos);
            AtmosBlockData data = chunk.getData(ATMOSPHERE.get());

            if (!level.getBlockState(pos).canBeReplaced() && !level.getBlockState(pos).is(BitterBlockTags.PASSES_ATMOS)) {
                if (AtmosHandler.getAtmosInstanceAt(level, pos) != null) {
                    AtmosHandler.getAtmosInstanceAt(level, pos).removeBlock(pos.asLong(), level);
                }
                data.removeAtmosBlock(pos);
            }

            // TODO: Proper updating for doors
            AtmosHandler.updateAtmosphereAt(level, event.getPos());
        }
    }
}
