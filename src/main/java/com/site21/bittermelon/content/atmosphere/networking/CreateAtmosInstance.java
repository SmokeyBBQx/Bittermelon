package com.site21.bittermelon.content.atmosphere.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.atmosphere.AtmosInstance;
import com.site21.bittermelon.content.atmosphere.data.AtmosLevelData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record CreateAtmosInstance(AtmosInstance instance) implements CustomPacketPayload {
    public static final Type<CreateAtmosInstance> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "create_atmos_instance"));

    public static final StreamCodec<RegistryFriendlyByteBuf, CreateAtmosInstance> STREAM_CODEC = StreamCodec.composite(
            AtmosInstance.STREAM_CODEC,
            CreateAtmosInstance::instance,
            CreateAtmosInstance::new
    );

    @Override
    public @NotNull Type<?> type() {
        return TYPE;
    }

    public void handle(@NotNull IPayloadContext ctx) {
            AtmosLevelData data = AtmosLevelData.get(ctx.player().level());
            data.getAtmosInstances().put(instance.getUuid(), instance);
    }
}