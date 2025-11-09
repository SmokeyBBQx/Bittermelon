package com.site21.bittermelon.common.systems.telecomms.intercom.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.telecomms.intercom.IntercomManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record RemoveIntercomFromClient(BlockPos pos) implements CustomPacketPayload {
    public static final Type<RemoveIntercomFromClient> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "remove_intercom_from_client"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, RemoveIntercomFromClient> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            RemoveIntercomFromClient::pos,
            RemoveIntercomFromClient::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        IntercomManager.get(ctx.player().level()).removeIntercomFromServer(pos());
    }
}
