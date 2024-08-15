package net.smokeybbq.bittermelon.items.handlabeler;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetItemNamePacket {
    private final String name;
    public SetItemNamePacket(String name) {
        this.name = name;
    }

    public static void encode(SetItemNamePacket msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.name);
    }

    public static SetItemNamePacket decode(FriendlyByteBuf buf) {
        return new SetItemNamePacket(buf.readUtf());
    }

    public static void handle(SetItemNamePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null && player.containerMenu instanceof HandLabelerMenu menu) {
                menu.setItemName(msg.name);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
