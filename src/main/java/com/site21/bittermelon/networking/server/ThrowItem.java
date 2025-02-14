package com.site21.bittermelon.networking.server;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.keybinds.ThrowKeyBind;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ThrowItem() implements CustomPacketPayload {
    public static final Type<ThrowItem> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "throw_item"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, ThrowItem> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull ThrowItem decode(@NotNull ByteBuf byteBuf) {
            return new ThrowItem();
        }

        @Override
        public void encode(@NotNull ByteBuf o, @NotNull ThrowItem throwItem) {

        }
    };

    public void handle(@NotNull IPayloadContext ctx) {
        ThrowKeyBind.throwItemAsProjectile(ctx.player());
    }
}
