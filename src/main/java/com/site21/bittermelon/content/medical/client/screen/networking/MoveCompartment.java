package com.site21.bittermelon.content.medical.client.screen.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.content.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.content.medical.compartments.VisualData;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record MoveCompartment(UUID instanceID, int layerIndex, UUID targetCompartmentID, UUID characterID, int newX,
                              int newY) implements CustomPacketPayload {
    public static final Type<MoveCompartment> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "move_compartment"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, MoveCompartment> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            MoveCompartment::instanceID,
            ByteBufCodecs.INT,
            MoveCompartment::layerIndex,
            UUIDUtil.STREAM_CODEC,
            MoveCompartment::targetCompartmentID,
            UUIDUtil.STREAM_CODEC,
            MoveCompartment::characterID,
            ByteBufCodecs.INT,
            MoveCompartment::newX,
            ByteBufCodecs.INT,
            MoveCompartment::newY,
            MoveCompartment::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        Character character = CharacterManager.get(ctx.player().level()).getCharacter(characterID);
        if (character == null) return;

        MedicalStats medicalStats = character.getMedicalStats();
        CompartmentInstance instance = medicalStats.getCompartment(instanceID);
        if (instance == null) return;

        VisualData visualData = instance.getVisualData();
        visualData.x = newX;
        visualData.y = newY;
        visualData.isHidden = false;

        CompartmentInstance targetCompartment = medicalStats.getCompartment(targetCompartmentID);
        if (targetCompartment == null) return;

        if (targetCompartment.tryToInsert(layerIndex, instance)) {
            CompartmentInstance oldParent = medicalStats.getParent(instance);
            if (oldParent != null) {
                oldParent.removeCompartment(layerIndex, instance);
            }
        }
    }
}
