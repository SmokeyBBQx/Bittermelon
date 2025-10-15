package com.site21.bittermelon.common.systems.component;

import com.mojang.serialization.Codec;
import com.site21.bittermelon.common.content.items.wirecutters.networking.OpenWireCutterScreen;
import com.site21.bittermelon.common.systems.electronics.ElectronicDevice;
import com.site21.bittermelon.common.systems.electronics.PanelDevice;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.PacketDistributor;

public record WireCutter(float efficiency) {
    public static final Codec<WireCutter> CODEC;
    public static final StreamCodec<ByteBuf, WireCutter> STREAM_CODEC;

    public boolean useOn(BlockEntity blockEntity, Player player) {
        if (blockEntity instanceof ElectronicDevice) {
            if (blockEntity instanceof PanelDevice panel && !panel.isPanelOpen()) return false;
            if (player instanceof ServerPlayer serverPlayer) {
                PacketDistributor.sendToPlayer(serverPlayer, new OpenWireCutterScreen(blockEntity.getBlockPos()));
            }
            return true;
        }
        return false;
    }

    static {
        CODEC = Codec.FLOAT.fieldOf("efficiency").xmap(WireCutter::new, WireCutter::efficiency).codec();
        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.FLOAT,
                WireCutter::efficiency,
                WireCutter::new
        );
    }
}
