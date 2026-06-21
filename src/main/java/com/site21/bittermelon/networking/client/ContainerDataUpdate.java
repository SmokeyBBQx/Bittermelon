package com.site21.bittermelon.networking.client;

import com.site21.bittermelon.Bittermelon;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public record ContainerDataUpdate(Map<Integer, Set<Integer>> translations) implements CustomPacketPayload {
    public static final Type<ContainerDataUpdate> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "container_data_update"));

    public static final StreamCodec<ByteBuf, ContainerDataUpdate> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.map(
                    HashMap::new,
                    ByteBufCodecs.INT,
                    ByteBufCodecs.collection(
                            HashSet::new,
                            ByteBufCodecs.INT
                    )
            ),
            ContainerDataUpdate::translations,
            ContainerDataUpdate::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext ctx) {
    }
}
