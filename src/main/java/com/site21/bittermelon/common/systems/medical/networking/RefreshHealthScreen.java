package com.site21.bittermelon.common.systems.medical.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.medical.client.HealthScreen;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record RefreshHealthScreen() implements CustomPacketPayload {
    public static final Type<RefreshHealthScreen> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "refresh_health_screen"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, RefreshHealthScreen> STREAM_CODEC = StreamCodec.unit(new RefreshHealthScreen());

    public void handle(@NotNull IPayloadContext ctx) {
        if (Minecraft.getInstance().screen instanceof HealthScreen healthScreen) {
            // TODO: If needed, refreshing stale medical stats can be implemented here
        }
    }
}
