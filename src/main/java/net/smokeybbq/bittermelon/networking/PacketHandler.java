package net.smokeybbq.bittermelon.networking;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.smokeybbq.bittermelon.Bittermelon;
import net.smokeybbq.bittermelon.util.ModLogger;

import java.util.function.Supplier;

import static org.antlr.runtime.debug.DebugEventListener.PROTOCOL_VERSION;

public class PacketHandler {
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("bittermelon", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    public static void register() {
        ModLogger.info("Registering network messages");

        INSTANCE.registerMessage(packetId++,
                TransferRateUpdatePacket.class,
                TransferRateUpdatePacket::encode,
                TransferRateUpdatePacket::decode,
                TransferRateUpdatePacket::handle
                );

        INSTANCE.registerMessage(packetId++,
                ThrowItemPacket.class,
                ThrowItemPacket::encode,
                ThrowItemPacket::decode,
                ThrowItemPacket::handle
        );


        ModLogger.info("Network messages registered successfully");

    }
}
