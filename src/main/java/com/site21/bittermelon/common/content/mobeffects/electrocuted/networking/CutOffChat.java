package com.site21.bittermelon.common.content.mobeffects.electrocuted.networking;

import com.site21.bittermelon.Bittermelon;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record CutOffChat() implements CustomPacketPayload {
    public static final Type<CutOffChat> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "cut_off_chat"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, CutOffChat> STREAM_CODEC = StreamCodec.unit(new CutOffChat());

    public void handle(@NotNull IPayloadContext ctx) {
        if (Minecraft.getInstance().screen instanceof ChatScreen screen) {
            screen.handleChatInput(screen.input.getValue() + "-", true);
            screen.onClose();
        }
    }
}
