package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.atmosphere.networking.*;
import com.site21.bittermelon.content.blocks.devices.implementations.containmentpanel.networking.OpenContainmentPanelScreen;
import com.site21.bittermelon.content.blocks.devices.implementations.intercom.networking.*;
import com.site21.bittermelon.content.character.networking.SyncCharacters;
import com.site21.bittermelon.content.economy.networking.OpenATMScreen;
import com.site21.bittermelon.content.items.wires.wire.networking.MakeWireConnection;
import com.site21.bittermelon.content.items.wires.wire.networking.OpenWiringScreen;
import com.site21.bittermelon.content.items.wires.wire.networking.WiringDataUpdate;
import com.site21.bittermelon.content.medical.client.screen.OpenHealthScreenC2S;
import com.site21.bittermelon.content.medical.client.screen.OpenHealthScreenS2C;
import com.site21.bittermelon.content.miscellaneous.stumble.networking.AttemptToRise;
import com.site21.bittermelon.content.telecomms.intercom.networking.SyncIntercomList;
import com.site21.bittermelon.networking.client.*;
import com.site21.bittermelon.content.blocks.devices.implementations.containmentpanel.networking.ContainmentNameUpdate;
import com.site21.bittermelon.content.items.containers.substance.networking.ReleasePressureUpdate;
import com.site21.bittermelon.content.throwing.ThrowItem;
import com.site21.bittermelon.content.items.containers.substance.networking.TransferRateUpdate;
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

        registrar.playToClient(
                OpenContainmentPanelScreen.TYPE,
                OpenContainmentPanelScreen.STREAM_CODEC,
                OpenContainmentPanelScreen::handle
        );

        registrar.playToServer(
                ContainmentNameUpdate.TYPE,
                ContainmentNameUpdate.STREAM_CODEC,
                ContainmentNameUpdate::handle
        );

        registrar.playToClient(
                AtmosChunkUpdate.TYPE,
                AtmosChunkUpdate.STREAM_CODEC,
                AtmosChunkUpdate::handle
        );

        registrar.playToServer(
                ReleasePressureUpdate.TYPE,
                ReleasePressureUpdate.STREAM_CODEC,
                ReleasePressureUpdate::handle
        );

        registrar.playToServer(
                IntercomIDUpdate.TYPE,
                IntercomIDUpdate.STREAM_CODEC,
                IntercomIDUpdate::handle
        );

        registrar.playToServer(
                IntercomMicUpdate.TYPE,
                IntercomMicUpdate.STREAM_CODEC,
                IntercomMicUpdate::handle
        );

        registrar.playToServer(
                IntercomSpeakerUpdate.TYPE,
                IntercomSpeakerUpdate.STREAM_CODEC,
                IntercomSpeakerUpdate::handle
        );

        registrar.playToServer(
                IntercomTargetUpdate.TYPE,
                IntercomTargetUpdate.STREAM_CODEC,
                IntercomTargetUpdate::handle
        );

        registrar.playToClient(
                SyncIntercomList.TYPE,
                SyncIntercomList.STREAM_CODEC,
                SyncIntercomList::handle
        );

        registrar.playToClient(
                SyncAtmosInstances.TYPE,
                SyncAtmosInstances.STREAM_CODEC,
                SyncAtmosInstances::handle
        );

        registrar.playToClient(
                SyncAtmosInstance.TYPE,
                SyncAtmosInstance.STREAM_CODEC,
                SyncAtmosInstance::handle
        );

        registrar.playToClient(
                SyncCharacters.TYPE,
                SyncCharacters.STREAM_CODEC,
                SyncCharacters::handle
        );

        registrar.playToClient(
                CreateAtmosInstance.TYPE,
                CreateAtmosInstance.STREAM_CODEC,
                CreateAtmosInstance::handle
        );

        registrar.playToClient(
                RemoveAtmosInstance.TYPE,
                RemoveAtmosInstance.STREAM_CODEC,
                RemoveAtmosInstance::handle
        );

        registrar.playToClient(
                UpdateAtmosTemperature.TYPE,
                UpdateAtmosTemperature.STREAM_CODEC,
                UpdateAtmosTemperature::handle
        );

        registrar.playToClient(
                UpdateAtmosBlocks.TYPE,
                UpdateAtmosBlocks.STREAM_CODEC,
                UpdateAtmosBlocks::handle
        );

        registrar.playToClient(
                UpdateAtmosGas.TYPE,
                UpdateAtmosGas.STREAM_CODEC,
                UpdateAtmosGas::handle
        );

        registrar.playToServer(
                WiringDataUpdate.TYPE,
                WiringDataUpdate.STREAM_CODEC,
                WiringDataUpdate::handle
        );

        registrar.playToServer(
                MakeWireConnection.TYPE,
                MakeWireConnection.STREAM_CODEC,
                MakeWireConnection::handle
        );

        registrar.playToServer(
                OpenHealthScreenC2S.TYPE,
                OpenHealthScreenC2S.STREAM_CODEC,
                OpenHealthScreenC2S::handle
        );

        registrar.playToClient(
                OpenHealthScreenS2C.TYPE,
                OpenHealthScreenS2C.STREAM_CODEC,
                OpenHealthScreenS2C::handle
        );

        registrar.playToClient(
                OpenIntercomScreen.TYPE,
                OpenIntercomScreen.STREAM_CODEC,
                OpenIntercomScreen::handle
        );

        registrar.playToClient(
                OpenWiringScreen.TYPE,
                OpenWiringScreen.STREAM_CODEC,
                OpenWiringScreen::handle
        );

        registrar.playToServer(
                AttemptToRise.TYPE,
                AttemptToRise.STREAM_CODEC,
                AttemptToRise::handle
        );
    }
}
