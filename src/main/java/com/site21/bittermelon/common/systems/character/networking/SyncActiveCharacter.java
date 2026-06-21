package com.site21.bittermelon.common.systems.character.networking;

import com.site21.bittermelon.Bittermelon;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.ACTIVE_CHARACTER;

public record SyncActiveCharacter(UUID activeCharacterUUID) implements CustomPacketPayload {
    public static final Type<SyncActiveCharacter> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "sync_active_character"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, SyncActiveCharacter> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            SyncActiveCharacter::activeCharacterUUID,
            SyncActiveCharacter::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        ctx.player().setData(ACTIVE_CHARACTER, activeCharacterUUID);
    }
}
