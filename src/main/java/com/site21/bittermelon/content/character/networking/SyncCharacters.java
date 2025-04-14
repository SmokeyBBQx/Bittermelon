package com.site21.bittermelon.content.character.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.content.telecomms.intercom.IntercomManager;
import com.site21.bittermelon.content.telecomms.intercom.networking.SyncIntercomList;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record SyncCharacters(Map<UUID, Character> characters) implements CustomPacketPayload {
    public static final Type<SyncCharacters> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "sync_characters"));

    public static final StreamCodec<RegistryFriendlyByteBuf, Map<UUID, Character>> CHARACTER_MAP_CODEC =
            ByteBufCodecs.map(
                    HashMap::new,
                    UUIDUtil.STREAM_CODEC,
                    Character.STREAM_CODEC
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncCharacters> STREAM_CODEC = StreamCodec.composite(
            CHARACTER_MAP_CODEC,
            SyncCharacters::characters,
            SyncCharacters::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(@NotNull IPayloadContext ctx) {
        CharacterManager manager = CharacterManager.get(ctx.player().level());
        manager.getCharacters().clear();
        manager.getCharacters().putAll(characters);
    }
}
