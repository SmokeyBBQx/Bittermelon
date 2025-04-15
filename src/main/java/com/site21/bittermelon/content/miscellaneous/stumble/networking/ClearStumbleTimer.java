package com.site21.bittermelon.content.miscellaneous.stumble.networking;

import com.site21.bittermelon.Bittermelon;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.STUMBLE_TICKS;

public class ClearStumbleTimer implements CustomPacketPayload {
    public static final Type<ClearStumbleTimer> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "clear_stumble_timer"));

    public static final StreamCodec<ByteBuf, ClearStumbleTimer> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ClearStumbleTimer decode(@NotNull ByteBuf byteBuf) {
            return new ClearStumbleTimer();
        }

        @Override
        public void encode(@NotNull ByteBuf o, @NotNull ClearStumbleTimer clearStumbleTimer) {

        }
    };

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(@NotNull IPayloadContext ctx) {
        ctx.player().removeData(STUMBLE_TICKS);
    }
}
