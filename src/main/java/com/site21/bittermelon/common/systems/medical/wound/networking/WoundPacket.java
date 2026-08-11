package com.site21.bittermelon.common.systems.medical.wound.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.medical.wound.Wound;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.HEALTH_CONTAINER;

public record WoundPacket(UUID uuid, Wound wound, int partIndex) implements CustomPacketPayload {
    public static final Type<WoundPacket> TYPE = new Type<>(Bittermelon.identifier("wound_packet"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, WoundPacket> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            WoundPacket::uuid,
            Wound.STREAM_CODEC,
            WoundPacket::wound,
            ByteBufCodecs.INT,
            WoundPacket::partIndex,
            WoundPacket::new
    );

    public void handle(IPayloadContext ctx) {
        ctx.player().level().getEntity(uuid).getData(HEALTH_CONTAINER).getParts().get(partIndex).getWounds().add(wound);
    }
}
