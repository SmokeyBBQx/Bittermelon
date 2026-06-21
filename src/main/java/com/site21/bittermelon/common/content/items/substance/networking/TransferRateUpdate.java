package com.site21.bittermelon.common.content.items.substance.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.items.substance.FluidContainerItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.common.content.items.substance.FluidContainerItem.setTransferRate;

public record TransferRateUpdate(int newRate, InteractionHand hand) implements CustomPacketPayload {
    // TODO: Somehow replace this with ItemStack instead of hand

    public static final Type<TransferRateUpdate> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "transfer_rate_update"));

    public static final StreamCodec<ByteBuf, TransferRateUpdate> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            TransferRateUpdate::newRate,
            ByteBufCodecs.INT.map(
                    ordinal -> InteractionHand.values()[ordinal],
                    InteractionHand::ordinal
            ),
            TransferRateUpdate::hand,
            TransferRateUpdate::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(@NotNull IPayloadContext ctx) {
        ItemStack stack = ctx.player().getItemInHand(hand);

        if (stack.getItem() instanceof FluidContainerItem) {
            setTransferRate(stack, newRate);
        }
    }
}
