package com.site21.bittermelon.common.systems.character.networking;

import com.site21.bittermelon.Bittermelon;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record OpenCharacterScreenC2S(UUID playerUUID) implements CustomPacketPayload {
    public static final Type<OpenCharacterScreenC2S> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "open_character_screen_c2s"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, OpenCharacterScreenC2S> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            OpenCharacterScreenC2S::playerUUID,
            OpenCharacterScreenC2S::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        if (ctx.player().level().getPlayerByUUID(playerUUID) instanceof ServerPlayer player) {
            int playTime = player.getStats().getValue(Stats.CUSTOM.get(Stats.PLAY_TIME));
            int playTimeHours = playTime / 72000;
            int maxCharacters = 1 + (playTimeHours / 20);

            PacketDistributor.sendToPlayer(player, new OpenCharacterScreenS2C(maxCharacters));
        }
    }
}
