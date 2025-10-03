package com.site21.bittermelon.content.blocks.wallwriting.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.ClientHandler;
import com.site21.bittermelon.content.blocks.wallwriting.WallWritingBlockEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record OpenWallWritingScreen(BlockPos pos) implements CustomPacketPayload {
    public static final Type<OpenWallWritingScreen> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "open_wall_writing_screen"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, OpenWallWritingScreen> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            OpenWallWritingScreen::pos,
            OpenWallWritingScreen::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        if (ctx.player().level().getBlockEntity(pos) instanceof WallWritingBlockEntity wallWriting) {
            ClientHandler.displayWallWritingScreen(wallWriting);
        }
    }
}
