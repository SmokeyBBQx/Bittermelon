package net.smokeybbq.bittermelon.items.radio.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.smokeybbq.bittermelon.items.radio.RadioItem;
import net.smokeybbq.bittermelon.networking.PacketHandler;

import java.util.function.Supplier;

public record RadioTogglePresetC2SPacket(float frequency) {

    public static void encode(RadioTogglePresetC2SPacket message, FriendlyByteBuf buffer) {
        buffer.writeFloat(message.frequency);
    }

    public static RadioTogglePresetC2SPacket decode(FriendlyByteBuf buffer) {
        return new RadioTogglePresetC2SPacket((buffer.readFloat()));
    }


    public static void handle(RadioTogglePresetC2SPacket message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                ItemStack radioStack = RadioItem.findRadioStackInInventory(player).orElse(ItemStack.EMPTY);
                if (!radioStack.isEmpty()) {
                    RadioItem.togglePreset(radioStack, message.frequency(), player);
                    PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new RadioUpdateClientScreenS2CPacket(RadioItem.getAllPresets(radioStack), RadioItem.getActiveChannel(radioStack)));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}

