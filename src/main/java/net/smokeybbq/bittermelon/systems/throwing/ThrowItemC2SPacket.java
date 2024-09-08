package net.smokeybbq.bittermelon.systems.throwing;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.smokeybbq.bittermelon.util.ModLogger;

import java.util.function.Supplier;

import static net.smokeybbq.bittermelon.systems.throwing.ThrowKeyHandler.throwItemAsProjectile;

public class ThrowItemC2SPacket {
    public ThrowItemC2SPacket() {

    }
    public static void handle(ThrowItemC2SPacket message, Supplier<NetworkEvent.Context> ctx) {
        try {
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                if (player != null) {
                    ItemStack heldItem = player.getMainHandItem();
                    if (!heldItem.isEmpty()) {
                        throwItemAsProjectile(player);
                    }
                }
            });
            ctx.get().setPacketHandled(true);
        } catch (Exception e) {
            ModLogger.error("Error in ThrowItemC2SPacket.handle: " + e.getMessage());
        }
    }

    public static ThrowItemC2SPacket decode(FriendlyByteBuf friendlyByteBuf) {
        return new ThrowItemC2SPacket();
    }

    public void encode(FriendlyByteBuf friendlyByteBuf) {
    }
}
