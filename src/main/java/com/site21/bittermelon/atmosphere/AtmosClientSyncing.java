package com.site21.bittermelon.atmosphere;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.atmosphere.data.AtmosBlockData;
import com.site21.bittermelon.networking.client.AtmosChunkUpdate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.BitterAttachmentTypes.ATMOSPHERE;

@EventBusSubscriber(modid = Bittermelon.MOD_ID)
public class AtmosClientSyncing {

    @SubscribeEvent
    public static void onChunkWatch(ChunkWatchEvent.@NotNull Watch event) {
        if (event.getLevel().isClientSide) return;

        ChunkPos chunkPos = event.getPos();
        ServerPlayer player = event.getPlayer();
        AtmosBlockData data = event.getChunk().getData(ATMOSPHERE.get());

        PacketDistributor.sendToPlayer(player, new AtmosChunkUpdate(chunkPos, data));
    }
}
