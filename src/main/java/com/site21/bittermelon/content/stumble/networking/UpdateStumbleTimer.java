package com.site21.bittermelon.content.stumble.networking;

import com.site21.bittermelon.Bittermelon;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.STUMBLE_TICKS;

public record UpdateStumbleTimer(int ticks) implements CustomPacketPayload {
    public static final Type<UpdateStumbleTimer> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "update_stumble_timer"));

    public static final StreamCodec<ByteBuf, UpdateStumbleTimer> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            UpdateStumbleTimer::ticks,
            UpdateStumbleTimer::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(@NotNull IPayloadContext ctx) {
        ctx.player().setData(STUMBLE_TICKS, ticks);
    }
}
