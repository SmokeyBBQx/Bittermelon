package com.site21.bittermelon.systems.throwing;

import com.site21.bittermelon.Bittermelon;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record ThrowItemPacket(UUID playerUUID) implements CustomPacketPayload {
    public static final Type<ThrowItemPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "throw_item"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, ThrowItemPacket> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            ThrowItemPacket::playerUUID,
            ThrowItemPacket::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        Level level = ctx.player().level();
        Player player = level.getPlayerByUUID(playerUUID);
        if (player == null) return;

        ThrowItem.throwItem(player);
    }
}
