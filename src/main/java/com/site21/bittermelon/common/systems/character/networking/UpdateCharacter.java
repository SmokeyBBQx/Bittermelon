package com.site21.bittermelon.common.systems.character.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.character.PlayerInfo;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record UpdateCharacter(Character character) implements CustomPacketPayload {
    public static final Type<UpdateCharacter> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "update_character"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateCharacter> STREAM_CODEC = StreamCodec.composite(
            Character.STREAM_CODEC,
            UpdateCharacter::character,
            UpdateCharacter::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        CharacterManager manager = CharacterManager.get(ctx.player().level());
        Character existingCharacter = manager.getCharacter(character.getId());

        if (existingCharacter != null) {
            existingCharacter.setName(character.getName());
            existingCharacter.setEmoteColor(character.getEmoteColor());
            existingCharacter.setDescription(character.getDescription());
            character.getPlayerInfo().ifPresent(info ->
                    existingCharacter.setPlayerInfo(new PlayerInfo(info.getSkinURL(), info.getModel())));
        } else {
            manager.getCharacters().put(character.getId(), character);
        }

        if (ctx.flow().isServerbound()) manager.setDirty();
    }
}
