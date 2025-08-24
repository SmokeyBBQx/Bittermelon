package com.site21.bittermelon.networking.server;

import com.site21.bittermelon.Bittermelon;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.LAST_TYPING_TIME;

public record SetLastTypingTime(UUID playerUUID, long lastTypingTime) implements CustomPacketPayload {
    public static final Type<SetLastTypingTime> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "set_last_typing_time"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, SetLastTypingTime> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            SetLastTypingTime::playerUUID,
            ByteBufCodecs.VAR_LONG,
            SetLastTypingTime::lastTypingTime,
            SetLastTypingTime::new
    );

    public void handle (@NotNull IPayloadContext ctx) {
        Player player = ctx.player().level().getPlayerByUUID(playerUUID);
        if (player == null) return;

        if (ctx.flow().isServerbound()) {
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(player, new SetLastTypingTime(playerUUID, lastTypingTime));
        }

        if (lastTypingTime == -1) {
            player.removeData(LAST_TYPING_TIME);
            return;
        }

        player.setData(LAST_TYPING_TIME, lastTypingTime);
    }
}
