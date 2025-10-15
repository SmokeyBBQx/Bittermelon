package com.site21.bittermelon.common.content.items.substance.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.items.substance.GasContainerItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record ReleasePressureUpdate(int newReleasePressure, InteractionHand hand) implements CustomPacketPayload {
    public static final Type<ReleasePressureUpdate> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "release_pressure_update"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, ReleasePressureUpdate> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            ReleasePressureUpdate::newReleasePressure,
            ByteBufCodecs.INT.map(
                    ordinal -> InteractionHand.values()[ordinal],
                    InteractionHand::ordinal
            ),
            ReleasePressureUpdate::hand,
            ReleasePressureUpdate::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        ItemStack stack = ctx.player().getItemInHand(hand);

        if (stack.getItem() instanceof GasContainerItem item) {
            item.setReleasePressure(stack, newReleasePressure);
        }
    }
}
