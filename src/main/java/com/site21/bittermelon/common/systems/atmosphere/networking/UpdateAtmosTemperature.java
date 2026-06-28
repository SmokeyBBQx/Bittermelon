package com.site21.bittermelon.common.systems.atmosphere.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.atmosphere.AtmosInstance;
import com.site21.bittermelon.common.systems.atmosphere.data.AtmosInstancesData;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record UpdateAtmosTemperature(UUID uuid, float temperature) implements CustomPacketPayload {
    public static final Type<UpdateAtmosTemperature> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "update_atmos_temperature"));

    public static final StreamCodec<ByteBuf, UpdateAtmosTemperature> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            UpdateAtmosTemperature::uuid,
            ByteBufCodecs.FLOAT,
            UpdateAtmosTemperature::temperature,
            UpdateAtmosTemperature::new
    );

    @Override
    public @NotNull Type<?> type() {
        return TYPE;
    }

    public void handle(@NotNull IPayloadContext ctx) {
            AtmosInstancesData data = AtmosInstancesData.get(ctx.player().level());
            AtmosInstance instance = data.getAtmosInstance(uuid);
            if (instance != null) {
                instance.setTemperature(temperature, ctx.player().level());
            }
    }
}
