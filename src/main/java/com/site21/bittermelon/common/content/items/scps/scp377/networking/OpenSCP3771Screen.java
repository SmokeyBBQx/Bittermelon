package com.site21.bittermelon.common.content.items.scps.scp377.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.gui.ScreenSetter;
import com.site21.bittermelon.common.content.items.scps.scp377.Fortune;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record OpenSCP3771Screen(Fortune fortune) implements CustomPacketPayload {
    public static final Type<OpenSCP3771Screen> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "open_scp_377_1_screen"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, OpenSCP3771Screen> STREAM_CODEC = StreamCodec.composite(
            Fortune.STREAM_CODEC,
            OpenSCP3771Screen::fortune,
            OpenSCP3771Screen::new
    );

    public void handle(IPayloadContext ctx) {
        ScreenSetter.displaySCP3771Screen(fortune);
    }
}
