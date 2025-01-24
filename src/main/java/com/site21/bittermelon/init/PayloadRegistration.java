package com.site21.bittermelon.init;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.networking.client.*;
import com.site21.bittermelon.networking.server.TransferRateUpdate;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class PayloadRegistration {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1.0");

        registrar.playToServer(
                TransferRateUpdate.TYPE,
                TransferRateUpdate.STREAM_CODEC,
                TransferRateUpdate::handle
        );

        registrar.playToServer(
                ContainerDataUpdate.TYPE,
                ContainerDataUpdate.STREAM_CODEC,
                ContainerDataUpdate::handle
        );

        registrar.playToClient(
                S2CSetForcedPose.TYPE,
                S2CSetForcedPose.STREAM_CODEC,
                S2CSetForcedPose::handle
        );

        registrar.playToClient(
                S2CClearForcedPose.TYPE,
                S2CClearForcedPose.STREAM_CODEC,
                S2CClearForcedPose::handle
        );

        registrar.playToClient(
                OpenCPRScreen.TYPE,
                OpenCPRScreen.STREAM_CODEC,
                OpenCPRScreen::handle
        );
    }
}
