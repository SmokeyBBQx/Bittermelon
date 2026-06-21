package com.site21.bittermelon.networking.client;

import com.site21.bittermelon.Bittermelon;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record SetForcedPose(UUID uuid, Pose pose) implements CustomPacketPayload {
    public static final Type<SetForcedPose> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "set_forced_pose"));

    public static final StreamCodec<ByteBuf, SetForcedPose> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            SetForcedPose::uuid,
            Pose.STREAM_CODEC,
            SetForcedPose::pose,
            SetForcedPose::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(@NotNull IPayloadContext ctx) {
        Player player = ctx.player().level().getPlayerByUUID(uuid());
        if (player != null) {
            player.setForcedPose(pose);
        }
    }
}
