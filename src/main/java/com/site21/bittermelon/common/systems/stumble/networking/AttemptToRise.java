package com.site21.bittermelon.common.systems.stumble.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.stumble.StumbleHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record AttemptToRise(UUID uuid) implements CustomPacketPayload {
    public static final Type<AttemptToRise> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "attempt_to_rise"));

    public static final StreamCodec<ByteBuf, AttemptToRise> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            AttemptToRise::uuid,
            AttemptToRise::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(@NotNull IPayloadContext ctx) {
        if (ctx.player().level() instanceof ServerLevel level) {
            StumbleHandler.attemptToRise(uuid, level);
        }
    }
}
