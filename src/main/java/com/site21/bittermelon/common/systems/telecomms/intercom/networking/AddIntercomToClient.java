package com.site21.bittermelon.common.systems.telecomms.intercom.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.telecomms.intercom.IntercomManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record AddIntercomToClient(BlockPos pos, String id) implements CustomPacketPayload {
    public static final Type<AddIntercomToClient> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "add_intercom_to_client"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, AddIntercomToClient> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            AddIntercomToClient::pos,
            ByteBufCodecs.STRING_UTF8, AddIntercomToClient::id,
            AddIntercomToClient::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        IntercomManager.get(ctx.player().level()).addIntercomFromServer(pos(), id());
    }
}
