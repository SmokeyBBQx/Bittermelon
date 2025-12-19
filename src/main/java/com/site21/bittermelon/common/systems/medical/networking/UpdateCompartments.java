package com.site21.bittermelon.common.systems.medical.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * <strong>Bidirectional Packet</strong> <br>
 * Updates compartments in specified character's medical stats by replacing them.
 * @param characterId
 * @param compartments
 */
public record UpdateCompartments(UUID characterId, List<CompartmentInstance> compartments) implements CustomPacketPayload {
    public static final Type<UpdateCompartments> TYPE = new Type<>(Bittermelon.resource("update_compartments"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateCompartments> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            UpdateCompartments::characterId,
            CompartmentInstance.STREAM_CODEC.apply(ByteBufCodecs.list()),
            UpdateCompartments::compartments,
            UpdateCompartments::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        Level level = ctx.player().level();
        Character character = CharacterManager.get(level).getCharacter(characterId());

        if (character != null) {
            MedicalStats medicalStats = character.getMedicalStats();
            for (CompartmentInstance compartment : compartments()) {
                medicalStats.addCompartment(compartment);
            }
        }
    }
}
