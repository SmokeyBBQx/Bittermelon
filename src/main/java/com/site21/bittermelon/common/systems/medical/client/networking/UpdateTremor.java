package com.site21.bittermelon.common.systems.medical.client.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.medical.client.minigame.IncisionMinigame;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record UpdateTremor(float tremor) implements CustomPacketPayload {
    public static final Type<UpdateTremor> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "update_tremor"));

    public static final StreamCodec<ByteBuf, UpdateTremor> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            UpdateTremor::tremor,
            UpdateTremor::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(@NotNull IPayloadContext ctx) {
        if (Minecraft.getInstance().screen instanceof IncisionMinigame incisionMinigame) {
            incisionMinigame.setTremor(tremor);
        }
    }
}
