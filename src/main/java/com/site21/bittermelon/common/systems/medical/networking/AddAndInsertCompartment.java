package com.site21.bittermelon.common.systems.medical.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentUtil;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * <strong>Bidirectional</strong> <br>
 * Adds a compartment to a character's medical stats and inserts it into a parent compartment at a specified layer and position.
 * @param characterId UUID of the character
 * @param parentId UUID of the target compartment that will receive the inserted compartment
 * @param compartment The compartment instance to be added and inserted
 * @param layer Layer at which to insert the compartment
 * @param x X position within the target compartment
 * @param y Y position within the target compartment
 */
public record AddAndInsertCompartment(UUID characterId, UUID parentId, CompartmentInstance compartment, int layer, int x, int y) implements CustomPacketPayload {
    public static final Type<AddAndInsertCompartment> TYPE = new Type<>(Bittermelon.resource("add_and_insert_compartment"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, AddAndInsertCompartment> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            AddAndInsertCompartment::characterId,
            UUIDUtil.STREAM_CODEC,
            AddAndInsertCompartment::parentId,
            CompartmentInstance.STREAM_CODEC,
            AddAndInsertCompartment::compartment,
            ByteBufCodecs.INT,
            AddAndInsertCompartment::layer,
            ByteBufCodecs.INT,
            AddAndInsertCompartment::x,
            ByteBufCodecs.INT,
            AddAndInsertCompartment::y,
            AddAndInsertCompartment::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        Level level = ctx.player().level();
        Character character = CharacterManager.get(level).getCharacter(characterId());

        if (character != null) {
            MedicalStats medicalStats = character.getMedicalStats();

            medicalStats.addCompartment(compartment());

            CompartmentInstance parent = medicalStats.getCompartment(parentId());
            if (parent != null) {
                CompartmentUtil.insertCompartment(parent, compartment(), layer(), x(), y());
            }
        }

        if (ctx.flow().isServerbound()) {
            PacketDistributor.sendToAllPlayers(new AddAndInsertCompartment(characterId(), parentId(), compartment(), layer(), x(), y()));
        }
    }
}
