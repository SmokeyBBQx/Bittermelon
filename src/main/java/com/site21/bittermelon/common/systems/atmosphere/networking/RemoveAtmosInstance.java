package com.site21.bittermelon.common.systems.atmosphere.networking;


import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.atmosphere.data.AtmosInstancesData;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record RemoveAtmosInstance(UUID uuid) implements CustomPacketPayload {
    public static final Type<RemoveAtmosInstance> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "remove_atmos_instance"));

    public static final StreamCodec<RegistryFriendlyByteBuf, RemoveAtmosInstance> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            RemoveAtmosInstance::uuid,
            RemoveAtmosInstance::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
            AtmosInstancesData data = AtmosInstancesData.get(ctx.player().level());
            data.getAtmosInstances().remove(uuid);
    }

    @Override
    public @NotNull Type<?> type() {
        return TYPE;
    }
}
