package com.site21.bittermelon.common.systems.medical.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentUtil;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.init.neoforge.BitterAttachmentTypes;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * <strong>Bidirectional</strong> <br>
 * Extracts a compartment from a parent compartment in a character's medical stats and removes it from the character.
 * @param entityUUID UUID of the character
 * @param parentId UUID of the parent compartment
 * @param toRemoveId UUID of the compartment to be removed
 * @param layer Layer from which to extract the compartment
 */
public record ExtractCompartment(UUID entityUUID, UUID parentId, UUID toRemoveId, int layer) implements CustomPacketPayload {
    public static final Type<ExtractCompartment> TYPE = new Type<>(Bittermelon.resource("extract_compartment"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, ExtractCompartment> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            ExtractCompartment::entityUUID,
            UUIDUtil.STREAM_CODEC,
            ExtractCompartment::parentId,
            UUIDUtil.STREAM_CODEC,
            ExtractCompartment::toRemoveId,
            ByteBufCodecs.INT,
            ExtractCompartment::layer,
            ExtractCompartment::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        if (ctx.player().level().getEntity(entityUUID) instanceof LivingEntity entity) {
            MedicalStats medicalStats = entity.getData(BitterAttachmentTypes.MEDICAL_STATS);
            medicalStats.removeCompartment(toRemoveId());

            CompartmentInstance parent = medicalStats.getCompartment(parentId());

            if (parent != null) {
                CompartmentUtil.extractCompartment(parent, toRemoveId(), layer());
            }

            entity.syncData(BitterAttachmentTypes.MEDICAL_STATS);
        }
    }
}
