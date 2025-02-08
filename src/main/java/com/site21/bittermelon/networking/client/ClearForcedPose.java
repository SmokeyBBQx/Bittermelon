package com.site21.bittermelon.networking.client;

import com.site21.bittermelon.Bittermelon;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record ClearForcedPose(UUID uuid) implements CustomPacketPayload {
    public static final Type<ClearForcedPose> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "clear_forced_pose"));

    public static final StreamCodec<ByteBuf, ClearForcedPose> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            ClearForcedPose::uuid,
            ClearForcedPose::new
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
