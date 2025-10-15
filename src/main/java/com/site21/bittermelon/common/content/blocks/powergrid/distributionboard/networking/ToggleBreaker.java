package com.site21.bittermelon.common.content.blocks.powergrid.distributionboard.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.blocks.powergrid.distributionboard.DistributionBoardBlockEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ToggleBreaker(String breakerName, BlockPos boardPos) implements CustomPacketPayload {
    public static final Type<ToggleBreaker> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "toggle_breaker"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, ToggleBreaker> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            ToggleBreaker::breakerName,
            BlockPos.STREAM_CODEC,
            ToggleBreaker::boardPos,
            ToggleBreaker::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        if (ctx.player().level().getBlockEntity(boardPos) instanceof DistributionBoardBlockEntity boardBlock) {
            boardBlock.toggleBreaker(breakerName);
        }
    }
}
