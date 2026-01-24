package com.site21.bittermelon.common.systems.carry;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.init.neoforge.BitterAttachmentTypes;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record ThrowCarriedEntity(UUID uuid, UUID carriedId) implements CustomPacketPayload {
    public static final Type<ThrowCarriedEntity> TYPE = new Type<>(Bittermelon.resource("throw_carried_entity"));

    public static final StreamCodec<ByteBuf, ThrowCarriedEntity> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            ThrowCarriedEntity::uuid,
            UUIDUtil.STREAM_CODEC,
            ThrowCarriedEntity::carriedId,
            ThrowCarriedEntity::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(@NotNull IPayloadContext ctx) {
        Player player = ctx.player().level().getPlayerByUUID(uuid);
        assert player != null;

        Entity carriedEntity = CarryHandler.getCarried(player);
        if (carriedEntity == null) return;

        carriedEntity.stopRiding();
        player.removeData(BitterAttachmentTypes.CARRIED_PASSENGER);

        EntityDimensions entityDimensions = carriedEntity.getDimensions(carriedEntity.getPose());
        float volume = 2 * entityDimensions.width() * entityDimensions.height();

        float pushMultiplier = Math.min(1.0f / volume, 1.0f);

        carriedEntity.push(
                player.getLookAngle().x * pushMultiplier,
                player.getLookAngle().y * pushMultiplier,
                player.getLookAngle().z * pushMultiplier
        );
    }
}
