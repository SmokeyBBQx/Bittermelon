package com.site21.bittermelon.content.personnel.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.ClientHandler;
import com.site21.bittermelon.content.personnel.PersonnelRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record OpenPersonnelScreen() implements CustomPacketPayload {
    public static final Type<OpenPersonnelScreen> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "open_personnel_screen"));

    public static final StreamCodec<ByteBuf, OpenPersonnelScreen> STREAM_CODEC =
            StreamCodec.of((buf, packet) -> {
            }, buf -> new OpenPersonnelScreen());

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(@NotNull IPayloadContext ctx) {
        ClientHandler.displayPersonnelScreen(PersonnelRegistry.get(ctx.player().level()));
    }
}