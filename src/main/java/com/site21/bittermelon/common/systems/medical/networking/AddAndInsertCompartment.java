package com.site21.bittermelon.common.systems.medical.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentUtil;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.MEDICAL_STATS;

/**
 * <strong>Bidirectional</strong> <br>
 * Adds a compartment to a entity's medical stats and inserts it into a parent compartment at a specified layer and position.
 * @param entityUUID UUID of the entity
 * @param parentId UUID of the target compartment that will receive the inserted compartment
 * @param compartment The compartment instance to be added and inserted
 * @param layer Layer at which to insert the compartment
 * @param x X position within the target compartment
 * @param y Y position within the target compartment
 */
public record AddAndInsertCompartment(UUID entityUUID, UUID parentId, CompartmentInstance compartment, int layer, int x, int y) implements CustomPacketPayload {
    public static final Type<AddAndInsertCompartment> TYPE = new Type<>(Bittermelon.resource("add_and_insert_compartment"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, AddAndInsertCompartment> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            AddAndInsertCompartment::entityUUID,
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
        if (ctx.player().level().getEntity(entityUUID) instanceof LivingEntity entity) {
            MedicalStats medicalStats = entity.getData(MEDICAL_STATS);
            medicalStats.addCompartment(compartment());

            CompartmentInstance parent = medicalStats.getCompartment(parentId());
            if (parent != null) {
                CompartmentUtil.insertCompartment(parent, compartment(), layer(), x(), y());
            }

            entity.syncData(MEDICAL_STATS);
        }
    }
}
