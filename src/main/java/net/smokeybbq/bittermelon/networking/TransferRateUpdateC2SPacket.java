package net.smokeybbq.bittermelon.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.smokeybbq.bittermelon.items.substancecontainers.SubstanceContainerItem;

import java.util.Objects;
import java.util.function.Supplier;

public class TransferRateUpdateC2SPacket {
    private final int newRate;

    public TransferRateUpdateC2SPacket(int newRate) {
        this.newRate = newRate;
    }

    public static void encode(TransferRateUpdateC2SPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.newRate);
    }

    public static TransferRateUpdateC2SPacket decode(FriendlyByteBuf buf) {
        return new TransferRateUpdateC2SPacket(buf.readInt());
    }

    public static void handle(TransferRateUpdateC2SPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            SubstanceContainerItem.handleTransferRateUpdate(Objects.requireNonNull(ctx.get().getSender()), msg.newRate);
        });
        ctx.get().setPacketHandled(true);
    }
}
