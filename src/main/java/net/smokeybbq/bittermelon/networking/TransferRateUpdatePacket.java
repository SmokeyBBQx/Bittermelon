package net.smokeybbq.bittermelon.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.smokeybbq.bittermelon.items.substanceContainers.SubstanceContainerItem;

import java.util.Objects;
import java.util.function.Supplier;

public class TransferRateUpdatePacket {
    private final int newRate;

    public TransferRateUpdatePacket(int newRate) {
        this.newRate = newRate;
    }

    public static void encode(TransferRateUpdatePacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.newRate);
    }

    public static TransferRateUpdatePacket decode(FriendlyByteBuf buf) {
        return new TransferRateUpdatePacket(buf.readInt());
    }

    public static void handle(TransferRateUpdatePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            SubstanceContainerItem.handleTransferRateUpdate(Objects.requireNonNull(ctx.get().getSender()), msg.newRate);
        });
        ctx.get().setPacketHandled(true);
    }
}
