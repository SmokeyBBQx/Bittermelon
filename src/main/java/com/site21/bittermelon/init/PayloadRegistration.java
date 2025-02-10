package com.site21.bittermelon.init;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.networking.client.*;
import com.site21.bittermelon.networking.server.ThrowItem;
import com.site21.bittermelon.networking.server.TransferRateUpdate;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class PayloadRegistration {
    @SubscribeEvent
    public static void register(final @NotNull RegisterPayloadHandlersEvent event) {
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
                SetForcedPose.TYPE,
                SetForcedPose.STREAM_CODEC,
                SetForcedPose::handle
        );

        registrar.playToClient(
                ClearForcedPose.TYPE,
                ClearForcedPose.STREAM_CODEC,
                ClearForcedPose::handle
        );

        registrar.playToClient(
                OpenCPRScreen.TYPE,
                OpenCPRScreen.STREAM_CODEC,
                OpenCPRScreen::handle
        );

        registrar.playToServer(
                ThrowItem.TYPE,
                ThrowItem.STREAM_CODEC,
                ThrowItem::handle
        );

        registrar.playToClient(
                OpenATMScreen.TYPE,
                OpenATMScreen.STREAM_CODEC,
                OpenATMScreen::handle
        );
    }
}
