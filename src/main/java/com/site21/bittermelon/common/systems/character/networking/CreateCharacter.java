package com.site21.bittermelon.common.systems.character.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record CreateCharacter(UUID id, String name, String description, int emoteColor) implements CustomPacketPayload {
    public static final Type<CreateCharacter> TYPE = new Type<>(Bittermelon.identifier("create_character"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, CreateCharacter> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            CreateCharacter::id,
            ByteBufCodecs.STRING_UTF8,
            CreateCharacter::name,
            ByteBufCodecs.STRING_UTF8,
            CreateCharacter::description,
            ByteBufCodecs.INT,
            CreateCharacter::emoteColor,
            CreateCharacter::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        Character character = new Character(id, name, description, emoteColor);
        CharacterManager manager = CharacterManager.get(ctx.player().level());
        manager.addCharacter(character);

        PacketDistributor.sendToAllPlayers(new UpdateCharacter(character));
    }
}
