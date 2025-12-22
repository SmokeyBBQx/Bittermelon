package com.site21.bittermelon.common.systems.character.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

/**
    * <strong>C2S</strong> <br>
    * Notify the Character Manager that the characters have changed and mark dirty for saving.
 */
public record SetCharactersChanged() implements CustomPacketPayload {
    public static final Type<SetCharactersChanged> TYPE = new Type<>(Bittermelon.resource("set_characters_changed"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, SetCharactersChanged> STREAM_CODEC = StreamCodec.unit(new SetCharactersChanged());

    public void handle(@NotNull IPayloadContext ctx) {
        CharacterManager.get(ctx.player().level()).setDirty();
    }
}
