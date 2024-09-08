package net.smokeybbq.bittermelon.networking;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.smokeybbq.bittermelon.items.handlabeler.SetItemNamePacket;
import net.smokeybbq.bittermelon.items.radio.networking.RadioKeyC2SPacket;
import net.smokeybbq.bittermelon.items.radio.networking.*;
import net.smokeybbq.bittermelon.systems.throwing.ThrowItemC2SPacket;
import net.smokeybbq.bittermelon.util.ModLogger;

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
                TransferRateUpdateC2SPacket.class,
                TransferRateUpdateC2SPacket::encode,
                TransferRateUpdateC2SPacket::decode,
                TransferRateUpdateC2SPacket::handle
        );

        INSTANCE.registerMessage(packetId++,
                ThrowItemC2SPacket.class,
                ThrowItemC2SPacket::encode,
                ThrowItemC2SPacket::decode,
                ThrowItemC2SPacket::handle
        );

        INSTANCE.registerMessage(packetId++,
                SetItemNamePacket.class,
                SetItemNamePacket::encode,
                SetItemNamePacket::decode,
                SetItemNamePacket::handle
        );

        INSTANCE.registerMessage(packetId++,
                OpenChatS2CPacket.class,
                OpenChatS2CPacket::encode,
                OpenChatS2CPacket::decode,
                OpenChatS2CPacket::handle
        );

        INSTANCE.registerMessage(packetId++,
                RadioKeyC2SPacket.class,
                RadioKeyC2SPacket::encode,
                RadioKeyC2SPacket::decode,
                RadioKeyC2SPacket::handle
        );

        INSTANCE.registerMessage(packetId++,
                RadioSetActiveFrequencyC2SPacket.class,
                RadioSetActiveFrequencyC2SPacket::encode,
                RadioSetActiveFrequencyC2SPacket::decode,
                RadioSetActiveFrequencyC2SPacket::handle
        );

        INSTANCE.registerMessage(packetId++,
                RadioEditPresetC2SPacket.class,
                RadioEditPresetC2SPacket::encode,
                RadioEditPresetC2SPacket::decode,
                RadioEditPresetC2SPacket::handle
        );

        INSTANCE.registerMessage(packetId++,
                RadioAddPresetC2SPacket.class,
                RadioAddPresetC2SPacket::encode,
                RadioAddPresetC2SPacket::decode,
                RadioAddPresetC2SPacket::handle
        );

        INSTANCE.registerMessage(packetId++,
                RadioTogglePresetC2SPacket.class,
                RadioTogglePresetC2SPacket::encode,
                RadioTogglePresetC2SPacket::decode,
                RadioTogglePresetC2SPacket::handle
        );

        INSTANCE.registerMessage(packetId++,
                RadioUpdateClientScreenS2CPacket.class,
                RadioUpdateClientScreenS2CPacket::encode,
                RadioUpdateClientScreenS2CPacket::decode,
                RadioUpdateClientScreenS2CPacket::handle
        );

        INSTANCE.registerMessage(packetId++,
                RadioGetPresetsC2SPacket.class,
                RadioGetPresetsC2SPacket::encode,
                RadioGetPresetsC2SPacket::decode,
                RadioGetPresetsC2SPacket::handle
        );

        ModLogger.info("Network messages registered successfully");

    }
}
