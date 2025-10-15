package com.site21.bittermelon.systems.character.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.systems.character.Character;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record AddCharacterToClient(Character character) implements CustomPacketPayload {
    public static final Type<AddCharacterToClient> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "add_character_to_client"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, AddCharacterToClient> STREAM_CODEC = StreamCodec.composite(
            Character.STREAM_CODEC,
            AddCharacterToClient::character,
            AddCharacterToClient::new
    );
}
