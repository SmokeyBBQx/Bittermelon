package net.smokeybbq.bittermelon.items.radio.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.smokeybbq.bittermelon.items.radio.RadioItem;
import net.smokeybbq.bittermelon.networking.PacketHandler;

import java.util.function.Supplier;

public record RadioSetActiveFrequencyC2SPacket(float frequency) {

    public static void encode(RadioSetActiveFrequencyC2SPacket message, FriendlyByteBuf buffer) {
        buffer.writeFloat(message.frequency);
    }

    public static RadioSetActiveFrequencyC2SPacket decode(FriendlyByteBuf buffer) {
        return new RadioSetActiveFrequencyC2SPacket((buffer.readFloat()));
    }


    public static void handle(RadioSetActiveFrequencyC2SPacket message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                ItemStack radioStack = RadioItem.findRadioStackInInventory(player).orElse(ItemStack.EMPTY);
                if (!radioStack.isEmpty()) {
                    RadioItem.setActiveChannel(radioStack, message.frequency());
                    PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new RadioUpdateClientScreenS2CPacket(RadioItem.getAllPresets(radioStack), RadioItem.getActiveChannel(radioStack)));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
