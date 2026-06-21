package com.site21.bittermelon.common.systems.character.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record SwitchCharacter(UUID entityUUID, UUID characterUUID) implements CustomPacketPayload {
    public static final Type<SwitchCharacter> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "switch_character"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, SwitchCharacter> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            SwitchCharacter::entityUUID,
            UUIDUtil.STREAM_CODEC,
            SwitchCharacter::characterUUID,
            SwitchCharacter::new
    );

    public void handle(@NotNull IPayloadContext context) {
        Player player = context.player().level().getPlayerByUUID(entityUUID);
        if (player != null) {
            CharacterManager manager = CharacterManager.get(context.player().level());
            manager.switchCharacter(player, manager.getActiveCharacter(player), manager.getCharacter(characterUUID));
        }
    }
}
