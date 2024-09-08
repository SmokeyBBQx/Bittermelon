package net.smokeybbq.bittermelon.items.radio.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.smokeybbq.bittermelon.items.radio.RadioItem;
import net.smokeybbq.bittermelon.networking.PacketHandler;

import java.util.function.Supplier;

public record RadioEditPresetC2SPacket(float frequency, float oldFrequency, String presetName) {

    public static void encode(RadioEditPresetC2SPacket message, FriendlyByteBuf buffer) {
        buffer.writeFloat(message.frequency);
        buffer.writeFloat(message.oldFrequency);
        buffer.writeUtf(message.presetName);
    }

    public static RadioEditPresetC2SPacket decode(FriendlyByteBuf buffer) {
        return new RadioEditPresetC2SPacket(buffer.readFloat(), buffer.readFloat(), buffer.readUtf());
    }


    public static void handle(RadioEditPresetC2SPacket message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                ItemStack radioStack = RadioItem.findRadioStackInInventory(player).orElse(ItemStack.EMPTY);
                if (!radioStack.isEmpty()) {
                    RadioItem.editPreset(radioStack, message.oldFrequency(), message.presetName(), message.frequency());
                    PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new RadioUpdateClientScreenS2CPacket(RadioItem.getAllPresets(radioStack), RadioItem.getActiveChannel(radioStack)));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
