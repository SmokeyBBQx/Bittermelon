package com.site21.bittermelon.content.atmosphere.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.atmosphere.data.AtmosBlockData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.ATMOSPHERE;

public record AtmosChunkUpdate(ChunkPos pos, AtmosBlockData data) implements CustomPacketPayload {
    public static final Type<AtmosChunkUpdate> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "atmos_chunk_update"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<FriendlyByteBuf, AtmosChunkUpdate> STREAM_CODEC = StreamCodec.composite(
            NeoForgeStreamCodecs.CHUNK_POS,
            AtmosChunkUpdate::pos,
            AtmosBlockData.STREAM_CODEC,
            AtmosChunkUpdate::data,
            AtmosChunkUpdate::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        if (Minecraft.getInstance().player == null) return;
        Level level = Minecraft.getInstance().player.level();

        level.getChunk(pos.x, pos.z).setData(ATMOSPHERE.get(), data());
        level.getChunk(pos.x, pos.z).setUnsaved(true);
    }
}
