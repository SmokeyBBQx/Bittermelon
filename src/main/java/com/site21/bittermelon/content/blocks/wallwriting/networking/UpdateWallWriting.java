package com.site21.bittermelon.content.blocks.wallwriting.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.wallwriting.WallWritingBlockEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record UpdateWallWriting(BlockPos pos, String[] text) implements CustomPacketPayload {
    public static final Type<UpdateWallWriting> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "update_wall_writing"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, UpdateWallWriting> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            UpdateWallWriting::pos,
            ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.list()).map(
                    list -> list.toArray(new String[0]),
                    List::of
            ),
            UpdateWallWriting::text,
            UpdateWallWriting::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        if (ctx.player().level().getBlockEntity(pos) instanceof WallWritingBlockEntity wallWriting) {
            wallWriting.updateText(text);
        }
    }
}
