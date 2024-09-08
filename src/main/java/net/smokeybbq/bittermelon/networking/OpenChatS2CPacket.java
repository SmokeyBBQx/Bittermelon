package net.smokeybbq.bittermelon.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class OpenChatS2CPacket {
    public OpenChatS2CPacket() {}

    public void encode(FriendlyByteBuf buf) {}

    public static OpenChatS2CPacket decode(FriendlyByteBuf buf) {
        return new OpenChatS2CPacket();
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft.getInstance().setScreen(new ChatScreen(""));
        });
        ctx.get().setPacketHandled(true);
    }
}
