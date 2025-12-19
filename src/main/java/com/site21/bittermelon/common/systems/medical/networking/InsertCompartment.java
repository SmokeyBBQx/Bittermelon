package com.site21.bittermelon.common.systems.medical.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * <strong>Bidirectional Packet</strong> <br>
 * Insert a compartment into another compartment at a specified layer and position.
 * @param characterId UUID of the character
 * @param targetId UUID of the target compartment that will receive the inserted compartment
 * @param compartmentId UUID of the compartment to be inserted
 * @param layer Layer at which to insert the compartment
 * @param x X position within the target compartment
 * @param y Y position within the target compartment
 */
public record InsertCompartment(UUID characterId, UUID targetId, UUID compartmentId, int layer, int x, int y) implements CustomPacketPayload {
    public static final Type<InsertCompartment> TYPE = new Type<>(Bittermelon.resource("insert_compartment"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, InsertCompartment> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            InsertCompartment::characterId,
            UUIDUtil.STREAM_CODEC,
            InsertCompartment::targetId,
            UUIDUtil.STREAM_CODEC,
            InsertCompartment::compartmentId,
            ByteBufCodecs.INT,
            InsertCompartment::layer,
            ByteBufCodecs.INT,
            InsertCompartment::x,
            ByteBufCodecs.INT,
            InsertCompartment::y,
            InsertCompartment::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        Level level = ctx.player().level();
        Character character = CharacterManager.get(level).getCharacter(characterId());

        if (!ctx.flow().isServerbound()) System.out.println("InsertCompartment received on client for character " + characterId());

        if (character == null) System.out.println("Character not found for InsertCompartment: " + characterId());

        if (character != null) {
            CompartmentInstance target = character.getMedicalStats().getCompartment(targetId());
            CompartmentInstance compartment = character.getMedicalStats().getCompartment(compartmentId());
            if (target == null) System.out.println("Target compartment not found: " + targetId());
            if (compartment == null) System.out.println("Compartment to insert not found: " + compartmentId());

            if (target != null && compartment != null) {
                target.removeCompartment(layer(), compartment);
                target.tryToInsert(layer(), x(), y(), compartment);
            }
        }

        if (ctx.flow().isServerbound()) {
            PacketDistributor.sendToAllPlayers(this);
        }
    }
}
