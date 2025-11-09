package com.site21.bittermelon.common.systems.medical.client.screen.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.compartments.VisualData;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public record MoveCompartment(UUID characterID, UUID receiverID, UUID targetID, UUID senderID, int layer, VisualData visualData) implements CustomPacketPayload {
    public static final Type<MoveCompartment> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "move_compartment"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, MoveCompartment> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            MoveCompartment::characterID,
            UUIDUtil.STREAM_CODEC,
            MoveCompartment::receiverID,
            UUIDUtil.STREAM_CODEC,
            MoveCompartment::targetID,
            UUIDUtil.STREAM_CODEC,
            MoveCompartment::senderID,
            ByteBufCodecs.INT,
            MoveCompartment::layer,
            VisualData.STREAM_CODEC,
            MoveCompartment::visualData,
            MoveCompartment::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        CharacterManager characterManager = CharacterManager.get(ctx.player().level());
        Character character = characterManager.getCharacter(characterID);
        if (character == null) return;

        MedicalStats medicalStats = character.getMedicalStats();
        CompartmentInstance target = medicalStats.getCompartment(targetID);
        if (target == null) return;

        target.getVisualData().x(visualData().x).y(visualData().y).isHidden(false);

        CompartmentInstance receiver = medicalStats.getCompartment(receiverID);
        if (receiver == null) return;

        if (receiver.tryToInsert(layer, target)) {
            System.out.println("Insertion succeeded server-side");

            CompartmentInstance sender = medicalStats.getCompartment(senderID);
            if (sender != null && !(sender.equals(receiver))) {
                sender.removeCompartment(target);
            }

            PacketDistributor.sendToPlayersTrackingEntityAndSelf(medicalStats.getEntity(),
                    new UpdateCompartments(List.of(receiver, target, sender)));
        }
    }
}
