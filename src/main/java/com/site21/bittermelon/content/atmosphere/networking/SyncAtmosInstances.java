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
import org.jetbrains.annotations.Contract;
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

//    public static final StreamCodec<RegistryFriendlyByteBuf, SyncAtmosInstances> STREAM_CODEC = StreamCodec.composite(
//            ATMOS_MAP_CODEC,
//            SyncAtmosInstances::atmosInstances,
//            SyncAtmosInstances::new
//    );

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncAtmosInstances> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public void encode(RegistryFriendlyByteBuf buf, @NotNull SyncAtmosInstances value) {
//            LOGGER.debug("Encoding {} atmos instances", value.atmosInstances().size());
            ATMOS_MAP_CODEC.encode(buf, value.atmosInstances());
//            LOGGER.debug("Encoded buffer size: {}", buf.writerIndex());
        }

        @Contract("_ -> new")
        @Override
        public @NotNull SyncAtmosInstances decode(@NotNull RegistryFriendlyByteBuf buf) {
//            LOGGER.debug("Starting decode with {} readable bytes", buf.readableBytes());
            Map<UUID, AtmosInstance> map = ATMOS_MAP_CODEC.decode(buf);
//            LOGGER.debug("Decoded {} instances, {} bytes remaining", map.size(), buf.readableBytes());
            return new SyncAtmosInstances(map);
        }
    };

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(@NotNull IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            AtmosLevelData data = AtmosLevelData.get(ctx.player().level());
            data.getAtmosInstances().clear();
            data.getAtmosInstances().putAll(atmosInstances);
        });
    }
}
