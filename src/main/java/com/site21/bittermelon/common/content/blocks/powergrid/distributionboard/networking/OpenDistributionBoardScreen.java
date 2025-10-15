package com.site21.bittermelon.common.content.blocks.powergrid.distributionboard.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.gui.ScreenHandler;
import com.site21.bittermelon.common.content.blocks.powergrid.distributionboard.DistributionBoardBlockEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record OpenDistributionBoardScreen(BlockPos pos) implements CustomPacketPayload {
    public static final Type<OpenDistributionBoardScreen> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "open_distribution_board_screen"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, OpenDistributionBoardScreen> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            OpenDistributionBoardScreen::pos,
            OpenDistributionBoardScreen::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        if (ctx.player().level().getBlockEntity(pos) instanceof DistributionBoardBlockEntity distributionBoard) {
            ScreenHandler.displayDistributionBoardScreen(distributionBoard);
        }
    }
}
