package com.site21.bittermelon.content.atmosphere.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.atmosphere.AtmosInstance;
import com.site21.bittermelon.content.atmosphere.data.AtmosLevelData;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.site21.bittermelon.Bittermelon.LOGGER;

public record SyncAtmosInstances(Map<UUID, AtmosInstance> atmosInstances) implements CustomPacketPayload {
    public static final Type<SyncAtmosInstances> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "sync_atmos_instances"));

    public static final StreamCodec<RegistryFriendlyByteBuf, Map<UUID, AtmosInstance>> ATMOS_MAP_CODEC =
            ByteBufCodecs.map(
                    HashMap::new,
                    UUIDUtil.STREAM_CODEC,
                    AtmosInstance.STREAM_CODEC
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncAtmosInstances> STREAM_CODEC = StreamCodec.composite(
            ATMOS_MAP_CODEC,
            SyncAtmosInstances::atmosInstances,
            SyncAtmosInstances::new
    );


    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(@NotNull IPayloadContext ctx) {
        AtmosLevelData data = AtmosLevelData.get(ctx.player().level());
        try {
            data.getAtmosInstances().clear();
            data.getAtmosInstances().putAll(atmosInstances);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Failed to sync AtmosInstances: {}", e.getMessage());
        };
    }
}
