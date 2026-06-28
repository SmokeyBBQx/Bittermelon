package com.site21.bittermelon.common.systems.atmosphere.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.atmosphere.AtmosInstance;
import com.site21.bittermelon.common.systems.atmosphere.data.AtmosInstancesData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record SyncAtmosInstance(AtmosInstance instance) implements CustomPacketPayload {
    public static final Type<SyncAtmosInstance> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "sync_atmos_instance"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncAtmosInstance> STREAM_CODEC = StreamCodec.composite(
            AtmosInstance.STREAM_CODEC,
            SyncAtmosInstance::instance,
            SyncAtmosInstance::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            AtmosInstancesData data = AtmosInstancesData.get(ctx.player().level());
            data.getAtmosInstances().put(instance.getUUID(), new AtmosInstance(instance.getTemperature(), instance.getGases(), instance.getUUID(), instance.getBlocks()));
        });
    }

    @Override
    public @NotNull Type<?> type() {
        return TYPE;
    }
}