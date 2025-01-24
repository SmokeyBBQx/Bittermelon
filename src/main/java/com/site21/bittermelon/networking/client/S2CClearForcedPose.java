package com.site21.bittermelon.networking.client;

import com.site21.bittermelon.Bittermelon;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record S2CClearForcedPose(UUID uuid) implements CustomPacketPayload {
    public static final Type<S2CClearForcedPose> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "clear_forced_pose"));

    public static final StreamCodec<ByteBuf, S2CClearForcedPose> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            S2CClearForcedPose::uuid,
            S2CClearForcedPose::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(@NotNull IPayloadContext ctx) {
        Player player = ctx.player().level().getPlayerByUUID(uuid());
        if (player != null) {
            player.setForcedPose(null);
        }
    }
}
