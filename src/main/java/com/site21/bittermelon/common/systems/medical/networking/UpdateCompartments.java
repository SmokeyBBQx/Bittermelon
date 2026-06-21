package com.site21.bittermelon.common.systems.medical.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.MEDICAL_STATS;

/**
 * <strong>Bidirectional Packet</strong> <br>
 * Updates compartments in specified entity's medical stats by replacing them.
 * @param entityUUID
 * @param compartments
 */
public record UpdateCompartments(UUID entityUUID, List<CompartmentInstance> compartments) implements CustomPacketPayload {
    public static final Type<UpdateCompartments> TYPE = new Type<>(Bittermelon.identifier("update_compartments"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateCompartments> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            UpdateCompartments::entityUUID,
            CompartmentInstance.STREAM_CODEC.apply(ByteBufCodecs.list()),
            UpdateCompartments::compartments,
            UpdateCompartments::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        if (ctx.player().level().getEntity(entityUUID) instanceof LivingEntity entity) {
            MedicalStats medicalStats = entity.getData(MEDICAL_STATS);
            for (CompartmentInstance compartment : compartments()) {
                medicalStats.addCompartment(compartment);
            }

            entity.syncData(MEDICAL_STATS);
        }
    }
}
