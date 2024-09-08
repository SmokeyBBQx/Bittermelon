package net.smokeybbq.bittermelon.items.radio.networking;

import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.smokeybbq.bittermelon.items.radio.RadioItem;
import net.smokeybbq.bittermelon.util.ModLogger;

import java.util.function.Supplier;

public class RadioKeyC2SPacket {
    public RadioKeyC2SPacket() {
    }

    public static void encode(RadioKeyC2SPacket message, FriendlyByteBuf buffer) {
    }

    public static RadioKeyC2SPacket decode(FriendlyByteBuf buffer) {
        return new RadioKeyC2SPacket();
    }

    public static void handle(RadioKeyC2SPacket message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                ModLogger.debug("Player found");
                if (!RadioItem.talkIntoRadio(player)) {
                    player.sendSystemMessage(Component.literal("You could find no radio.").withStyle(ChatFormatting.RED));
                }
            } else {
                ModLogger.debug("Player is null");
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
