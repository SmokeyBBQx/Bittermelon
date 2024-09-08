package net.smokeybbq.bittermelon.items.radio.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.smokeybbq.bittermelon.items.radio.RadioItem;
import net.smokeybbq.bittermelon.networking.PacketHandler;

import java.util.function.Supplier;

public class RadioGetPresetsC2SPacket {

    public RadioGetPresetsC2SPacket() {
    }

    public void encode(FriendlyByteBuf friendlyByteBuf) {
    }

    public static RadioGetPresetsC2SPacket decode(FriendlyByteBuf friendlyByteBuf) {
        return new RadioGetPresetsC2SPacket();
    }

    public static void handle(RadioGetPresetsC2SPacket message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                ItemStack radioStack = RadioItem.findRadioStackInInventory(player).orElse(ItemStack.EMPTY);
                if (!radioStack.isEmpty()) {
                    PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new RadioUpdateClientScreenS2CPacket(RadioItem.getAllPresets(radioStack), RadioItem.getActiveChannel(radioStack)));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
