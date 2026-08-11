package com.site21.bittermelon.common.systems.medical.legacy.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.medical.legacy.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.legacy.compartment.CompartmentUtil;
import com.site21.bittermelon.common.systems.medical.legacy.medicalstats.MedicalStats;
import com.site21.bittermelon.init.neoforge.BitterAttachmentTypes;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
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
public record RemoveCompartment(UUID entityUUID, UUID parentId, UUID toRemoveId) implements CustomPacketPayload {
    public static final Type<RemoveCompartment> TYPE = new Type<>(Bittermelon.identifier("remove_compartment"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, RemoveCompartment> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            RemoveCompartment::entityUUID,
            UUIDUtil.STREAM_CODEC,
            RemoveCompartment::parentId,
            UUIDUtil.STREAM_CODEC,
            RemoveCompartment::toRemoveId,
            RemoveCompartment::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        if (ctx.player().level().getEntity(entityUUID) instanceof LivingEntity entity) {
            MedicalStats medicalStats = entity.getData(BitterAttachmentTypes.MEDICAL_STATS);
            medicalStats.removeCompartment(toRemoveId());

            CompartmentInstance parent = medicalStats.getCompartment(parentId());

            if (parent != null) {
                CompartmentUtil.removeCompartment(parent, toRemoveId());
            }

            entity.syncData(BitterAttachmentTypes.MEDICAL_STATS);
        }
    }
}
