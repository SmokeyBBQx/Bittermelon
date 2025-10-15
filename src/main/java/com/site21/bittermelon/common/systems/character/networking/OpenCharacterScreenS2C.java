package com.site21.bittermelon.common.systems.character.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.ClientHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record OpenCharacterScreenS2C(int maxCharacters) implements CustomPacketPayload {
    public static final Type<OpenCharacterScreenS2C> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "open_character_screen_s2c"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, OpenCharacterScreenS2C> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            OpenCharacterScreenS2C::maxCharacters,
            OpenCharacterScreenS2C::new
    );

    public void handle(IPayloadContext ctx) {
        ClientHandler.displayCharacterScreen(maxCharacters);
    }
}
