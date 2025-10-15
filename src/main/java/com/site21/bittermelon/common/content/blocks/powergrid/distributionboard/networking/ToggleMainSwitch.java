package com.site21.bittermelon.common.content.blocks.powergrid.distributionboard.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.blocks.powergrid.distributionboard.DistributionBoardBlockEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ToggleMainSwitch(BlockPos pos) implements CustomPacketPayload {
    public static final Type<ToggleMainSwitch> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "toggle_main_switch"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, ToggleMainSwitch> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            ToggleMainSwitch::pos,
            ToggleMainSwitch::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        if (ctx.player().level().getBlockEntity(pos) instanceof DistributionBoardBlockEntity board) {
            board.toggleMainSwitch();
        }
    }
}
