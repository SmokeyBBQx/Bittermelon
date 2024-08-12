package net.smokeybbq.bittermelon.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.smokeybbq.bittermelon.util.ModLogger;

import java.util.function.Supplier;

import static net.smokeybbq.bittermelon.events.ThrowKeyHandler.throwItemAsProjectile;

public class ThrowItemPacket {
    public ThrowItemPacket() {

    }
    public static void handle(ThrowItemPacket message, Supplier<NetworkEvent.Context> ctx) {
        try {
            ModLogger.debug("ThrowItemPacket handle method called");
            ctx.get().enqueueWork(() -> {
                ModLogger.debug("Inside enqueueWork");
                ServerPlayer player = ctx.get().getSender();
                if (player != null) {
                    ModLogger.debug("Player is not null: " + player.getName().getString());
                    ItemStack heldItem = player.getMainHandItem();
                    if (!heldItem.isEmpty()) {
                        ModLogger.debug("Held item is not empty: " + heldItem.getItem().getDescriptionId());
                        throwItemAsProjectile(player);
                        ModLogger.debug(player.getName().getString() + " is shooting " + heldItem.getItem().getDescriptionId());
                    } else {
                        ModLogger.debug("Held item is empty");
                    }
                } else {
                    ModLogger.debug("Player is null");
                }
            });
            ctx.get().setPacketHandled(true);
        } catch (Exception e) {
            ModLogger.error("Error in ThrowItemPacket.handle: " + e.getMessage());
        }
    }

    public static ThrowItemPacket decode(FriendlyByteBuf friendlyByteBuf) {
        return new ThrowItemPacket();
    }

    public void encode(FriendlyByteBuf friendlyByteBuf) {
    }
}
