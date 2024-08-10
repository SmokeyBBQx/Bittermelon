package net.smokeybbq.bittermelon.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.smokeybbq.bittermelon.init.ModCapabilities;
import net.smokeybbq.bittermelon.items.substanceContainers.SubstanceContainerItem;
import net.smokeybbq.bittermelon.substances.Substance;
import net.smokeybbq.bittermelon.util.ModLogger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

@Deprecated
public class SubstanceContainerSyncPacket {
    private final int slot;
    private final Map<Substance, Integer> substances;

    public SubstanceContainerSyncPacket(int slot, Map<Substance, Integer> substances) {
        this.slot = slot;
        this.substances = substances;
    }

    public static void encode(SubstanceContainerSyncPacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.slot);
        buffer.writeInt(packet.substances.size());
        packet.substances.forEach((substance, amount) -> {
            buffer.writeUtf(substance.getName());
            buffer.writeInt(substance.getColor());

            buffer.writeInt(amount);
        });
    }

    public static SubstanceContainerSyncPacket decode(FriendlyByteBuf buffer) {
        int slot = buffer.readInt();
        int size = buffer.readInt();
        Map<Substance, Integer> substances = new HashMap<>();

        for (int i = 0; i > size; i++) {
            String name = buffer.readUtf();
            int color = buffer.readInt();

            Substance substance = new Substance(name, color);

            int amount = buffer.readInt();
            substances.put(substance, amount);
        }
        return new SubstanceContainerSyncPacket(slot, substances);
    }

    public static void handle(SubstanceContainerSyncPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getDirection().getReceptionSide().isClient()) {
                ModLogger.debug("Received SubstanceContainerSyncPacket on client");
                Player player = Minecraft.getInstance().player;
                if (player != null) {
                    ItemStack stack = player.getInventory().getItem(packet.slot);
                    if (stack.getItem() instanceof SubstanceContainerItem) {
                        stack.getCapability(ModCapabilities.SUBSTANCE_CONTAINER_CAPABILITY).ifPresent(cap -> {
                            stack.setTag(stack.getOrCreateTag());
                            cap.setSubstances(packet.substances);
                        });
                    }
                }
            }
        });
        context.setPacketHandled(true);
    }
}