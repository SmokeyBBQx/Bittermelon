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
 * <strong>Bidirectional Packet</strong> <br>
 * Insert a compartment into another compartment at a specified layer and position.
 * @param entityUUID UUID of the entity
 * @param parentId UUID of the target compartment that will receive the inserted compartment
 * @param childId UUID of the compartment to be inserted
 * @param layer Layer at which to insert the compartment
 * @param x X position within the target compartment
 * @param y Y position within the target compartment
 */
public record  InsertCompartment(UUID entityUUID, UUID parentId, UUID childId, int layer, int x, int y, int z) implements CustomPacketPayload {
    public static final Type<InsertCompartment> TYPE = new Type<>(Bittermelon.identifier("insert_compartment"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, InsertCompartment> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            InsertCompartment::entityUUID,
            UUIDUtil.STREAM_CODEC,
            InsertCompartment::parentId,
            UUIDUtil.STREAM_CODEC,
            InsertCompartment::childId,
            ByteBufCodecs.INT,
            InsertCompartment::layer,
            ByteBufCodecs.INT,
            InsertCompartment::x,
            ByteBufCodecs.INT,
            InsertCompartment::y,
            ByteBufCodecs.INT,
            InsertCompartment::z,
            InsertCompartment::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        if (ctx.player().level().getEntity(entityUUID) instanceof LivingEntity entity) {
            MedicalStats medicalStats = entity.getData(BitterAttachmentTypes.MEDICAL_STATS);

            CompartmentInstance parent = medicalStats.getCompartment(parentId());
            CompartmentInstance child = medicalStats.getCompartment(childId());

            if (parent != null && child != null) {
                CompartmentUtil.insertCompartment(parent, child, layer(), x(), y(), z());
            }

            entity.syncData(BitterAttachmentTypes.MEDICAL_STATS);
        }
    }
}
