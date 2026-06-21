package com.site21.bittermelon.client.render.screenshake;

import com.site21.bittermelon.Bittermelon;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record StartScreenshake(int duration, float intensity) implements CustomPacketPayload {
    public static final Type<StartScreenshake> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "start_screenshake"));

    public static final StreamCodec<ByteBuf, StartScreenshake> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            StartScreenshake::duration,
            ByteBufCodecs.FLOAT,
            StartScreenshake::intensity,
            StartScreenshake::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(@NotNull IPayloadContext ctx) {
        ScreenshakeRenderer.shakeData = new ScreenshakeRenderer.ScreenshakeData(duration, intensity);
    }
}
