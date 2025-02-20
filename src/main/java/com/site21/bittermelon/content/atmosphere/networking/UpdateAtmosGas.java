package com.site21.bittermelon.content.atmosphere.networking;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.atmosphere.AtmosInstance;
import com.site21.bittermelon.content.atmosphere.data.AtmosLevelData;
import com.site21.bittermelon.content.substance.SubstanceStack;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record UpdateAtmosGas(UUID uuid, SubstanceStack gas) implements CustomPacketPayload {
    public static final Type<UpdateAtmosGas> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "update_atmos_gas"));

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateAtmosGas> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            UpdateAtmosGas::uuid,
            SubstanceStack.STREAM_CODEC,
            UpdateAtmosGas::gas,
            UpdateAtmosGas::new
    );

    @Override
    public @NotNull Type<?> type() {
        return TYPE;
    }

    public void handle(@NotNull IPayloadContext ctx) {
            AtmosLevelData data = AtmosLevelData.get(ctx.player().level());
            AtmosInstance instance = data.getAtmosInstance(uuid);
            if (instance != null) {
                instance.updateGas(gas, ctx.player().level());
            }
    }
}
