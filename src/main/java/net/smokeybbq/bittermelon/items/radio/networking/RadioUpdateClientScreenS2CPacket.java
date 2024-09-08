package net.smokeybbq.bittermelon.items.radio.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.smokeybbq.bittermelon.items.radio.RadioItem;
import net.smokeybbq.bittermelon.items.radio.RadioScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class RadioUpdateClientScreenS2CPacket {
    List<RadioItem.Preset> presets;
    String activeChannel;
    public RadioUpdateClientScreenS2CPacket(List<RadioItem.Preset> presets, String activeChannel) {
        this.presets = presets;
        this.activeChannel = activeChannel;
    }

    public static void encode(RadioUpdateClientScreenS2CPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.presets.size());
        for (RadioItem.Preset preset : msg.presets) {
            buf.writeFloat(preset.frequency);
            buf.writeUtf(preset.name);
            buf.writeBoolean(preset.active);
        }
        buf.writeUtf(msg.activeChannel);
    }

    public static RadioUpdateClientScreenS2CPacket decode(FriendlyByteBuf buf) {
        int size = buf.readInt();
        List<RadioItem.Preset> presets = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            float frequency = buf.readFloat();
            String name = buf.readUtf();
            boolean active = buf.readBoolean();
            presets.add(new RadioItem.Preset(name, frequency, active));
        }
        String activeChannel = buf.readUtf();
        return new RadioUpdateClientScreenS2CPacket(presets, activeChannel);
    }

    public List<RadioItem.Preset> getPresets() {
        return presets;
    }

    public String getActiveChannel() {
        return activeChannel;
    }

    public static void handle(RadioUpdateClientScreenS2CPacket message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.screen instanceof RadioScreen) {
                ((RadioScreen) mc.screen).updateRendering(message.getPresets(), message.getActiveChannel());
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
