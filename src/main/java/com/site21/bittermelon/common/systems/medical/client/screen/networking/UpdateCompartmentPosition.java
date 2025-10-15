package com.site21.bittermelon.common.systems.medical.client.screen.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.medical.client.screen.HealthScreenV2;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.compartment.VisualData;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record UpdateCompartmentPosition(UUID characterID, VisualData visualData, UUID senderID, UUID targetID,
                                        UUID receiverID, int layer) implements CustomPacketPayload {
    public static final Type<UpdateCompartmentPosition> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "update_compartment_position"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, UpdateCompartmentPosition> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            UpdateCompartmentPosition::characterID,
            VisualData.STREAM_CODEC,
            UpdateCompartmentPosition::visualData,
            UUIDUtil.STREAM_CODEC,
            UpdateCompartmentPosition::senderID,
            UUIDUtil.STREAM_CODEC,
            UpdateCompartmentPosition::targetID,
            UUIDUtil.STREAM_CODEC,
            UpdateCompartmentPosition::receiverID,
            ByteBufCodecs.INT,
            UpdateCompartmentPosition::layer,
            UpdateCompartmentPosition::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        CharacterManager characterManager = CharacterManager.get(ctx.player().level());
        Character character = characterManager.getCharacter(characterID);
        if (character == null) return;

        MedicalStats medicalStats = character.getMedicalStats();
        CompartmentInstance sender = medicalStats.getCompartment(senderID);
        CompartmentInstance target = medicalStats.getCompartment(targetID);
        CompartmentInstance receiver = medicalStats.getCompartment(receiverID);

        sender.removeCompartment(layer, target);
        target.getVisualData().x(visualData().x).y(visualData().y).isHidden(false);
        receiver.tryToInsert(layer, target);

        if (Minecraft.getInstance().screen instanceof HealthScreenV2 screen) {
            screen.refresh();
        }

        characterManager.setDirty();
    }
}
