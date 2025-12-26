package com.site21.bittermelon.common.systems.medical.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * <strong>Bidirectional</strong> <br>
 * Extracts a compartment from a parent compartment in a character's medical stats and removes it from the character.
 * @param characterId UUID of the character
 * @param parentId UUID of the parent compartment
 * @param toRemoveId UUID of the compartment to be removed
 * @param layer Layer from which to extract the compartment
 */
public record ExtractCompartment(UUID characterId, UUID parentId, UUID toRemoveId, int layer) implements CustomPacketPayload {
    public static final Type<ExtractCompartment> TYPE = new Type<>(Bittermelon.resource("extract_compartment"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, ExtractCompartment> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            ExtractCompartment::characterId,
            UUIDUtil.STREAM_CODEC,
            ExtractCompartment::parentId,
            UUIDUtil.STREAM_CODEC,
            ExtractCompartment::toRemoveId,
            ByteBufCodecs.INT,
            ExtractCompartment::layer,
            ExtractCompartment::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        Character character = CharacterManager.get(ctx.player().level()).getCharacter(characterId());
        if (character != null) {
            character.getMedicalStats().removeCompartment(toRemoveId());
            CompartmentInstance parent = character.getMedicalStats().getCompartment(parentId());
            if (parent != null) {
                CompartmentUtil.extractCompartment(parent, toRemoveId(), layer());
            }
        }

        if (ctx.flow().isServerbound()) {
            PacketDistributor.sendToAllPlayers(this);
        }
    }
}
