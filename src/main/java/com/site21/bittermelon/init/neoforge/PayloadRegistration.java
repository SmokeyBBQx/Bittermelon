package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.visualeffects.screenshake.StartScreenshake;
import com.site21.bittermelon.content.atmosphere.networking.*;
import com.site21.bittermelon.content.blocks.devices.implementations.containmentpanel.networking.ContainmentNameUpdate;
import com.site21.bittermelon.content.blocks.devices.implementations.containmentpanel.networking.OpenContainmentPanelScreen;
import com.site21.bittermelon.content.blocks.devices.implementations.intercom.networking.*;
import com.site21.bittermelon.content.blocks.devices.privilege.networking.OpenPrivilegeEditorScreen;
import com.site21.bittermelon.content.blocks.devices.privilege.networking.RemovePrivilegeForBE;
import com.site21.bittermelon.content.blocks.devices.privilege.networking.SetPrivilegeForBE;
import com.site21.bittermelon.content.character.networking.*;
import com.site21.bittermelon.content.items.scps.scp377.networking.OpenSCP3771Screen;
import com.site21.bittermelon.content.personnel.privilege.networking.*;
import com.site21.bittermelon.content.personnel.registry.networking.AddPersonnelEntry;
import com.site21.bittermelon.content.blocks.devices.implementations.slidingdoor.networking.PlaySlidingDoorStuckSound;
import com.site21.bittermelon.content.blocks.devices.implementations.slidingdoor.networking.UpdateSlidingDoorProgress;
import com.site21.bittermelon.content.blocks.devices.implementations.slidingdoor.networking.UpdateSlidingDoorState;
import com.site21.bittermelon.content.economy.networking.OpenATMScreen;
import com.site21.bittermelon.content.mobeffects.electrocuted.networking.CutOffChat;
import com.site21.bittermelon.content.entities.implementations.scp650.networking.SetEntityPos;
import com.site21.bittermelon.content.items.substance.networking.ReleasePressureUpdate;
import com.site21.bittermelon.content.items.substance.networking.TransferRateUpdate;
import com.site21.bittermelon.content.items.wires.wire.networking.MakeWireConnection;
import com.site21.bittermelon.content.items.wires.wire.networking.OpenWiringScreen;
import com.site21.bittermelon.content.items.wires.wire.networking.RemoveWiringData;
import com.site21.bittermelon.content.items.wires.wire.networking.WiringDataUpdate;
import com.site21.bittermelon.content.items.writablepaper.client.OpenPaperEditScreen;
import com.site21.bittermelon.content.medical.client.screen.networking.*;
import com.site21.bittermelon.content.blocks.devices.implementations.personnelterminal.networking.OpenPersonnelScreen;
import com.site21.bittermelon.content.personnel.registry.networking.RemovePersonnelEntry;
import com.site21.bittermelon.content.personnel.registry.networking.SyncPersonnelRegistry;
import com.site21.bittermelon.content.personnel.registry.networking.UpdatePersonnelEntry;
import com.site21.bittermelon.content.roles.networking.AddRole;
import com.site21.bittermelon.content.stumble.networking.AttemptToRise;
import com.site21.bittermelon.content.telecomms.intercom.networking.AddIntercomToClient;
import com.site21.bittermelon.content.telecomms.intercom.networking.RemoveIntercomFromClient;
import com.site21.bittermelon.content.telecomms.intercom.networking.SyncIntercomList;
import com.site21.bittermelon.content.throwing.ThrowItem;
import com.site21.bittermelon.networking.client.ClearForcedPose;
import com.site21.bittermelon.networking.client.ContainerDataUpdate;
import com.site21.bittermelon.networking.client.OpenCPRScreen;
import com.site21.bittermelon.networking.client.SetForcedPose;
import com.site21.bittermelon.networking.server.AddEffect;
import com.site21.bittermelon.networking.server.SetLastTypingTime;
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

        registrar.playToClient(
                StartScreenshake.TYPE,
                StartScreenshake.STREAM_CODEC,
                StartScreenshake::handle
        );

        registrar.playToServer(
                CompleteMinigame.TYPE,
                CompleteMinigame.STREAM_CODEC,
                CompleteMinigame::handle
        );

        registrar.playToServer(
                ExtractCompartment.TYPE,
                ExtractCompartment.STREAM_CODEC,
                ExtractCompartment::handle
        );

        registrar.playToClient(
                UpdateHealthScreen.TYPE,
                UpdateHealthScreen.STREAM_CODEC,
                UpdateHealthScreen::handle
        );

        registrar.playToClient(
                UpdateCompartmentHealth.TYPE,
                UpdateCompartmentHealth.STREAM_CODEC,
                UpdateCompartmentHealth::handle
        );

        registrar.playToClient(
                UpdateTremor.TYPE,
                UpdateTremor.STREAM_CODEC,
                UpdateTremor::handle
        );

        registrar.playToServer(
                RemoveWiringData.TYPE,
                RemoveWiringData.STREAM_CODEC,
                RemoveWiringData::handle
        );

        registrar.playToServer(
                UpdateSlidingDoorState.TYPE,
                UpdateSlidingDoorState.STREAM_CODEC,
                UpdateSlidingDoorState::handle
        );

        registrar.playToServer(
                PlaySlidingDoorStuckSound.TYPE,
                PlaySlidingDoorStuckSound.STREAM_CODEC,
                PlaySlidingDoorStuckSound::handle
        );

        registrar.playToServer(
                UpdateSlidingDoorProgress.TYPE,
                UpdateSlidingDoorProgress.STREAM_CODEC,
                UpdateSlidingDoorProgress::handle
        );

        registrar.playToClient(
                SetEntityPos.TYPE,
                SetEntityPos.STREAM_CODEC,
                SetEntityPos::handle
        );

        registrar.playToClient(
                OpenPaperEditScreen.TYPE,
                OpenPaperEditScreen.STREAM_CODEC,
                OpenPaperEditScreen::handle
        );

        registrar.playToServer(
                MoveCompartment.TYPE,
                MoveCompartment.STREAM_CODEC,
                MoveCompartment::handle
        );

        registrar.playToClient(
                CutOffChat.TYPE,
                CutOffChat.STREAM_CODEC,
                CutOffChat::handle
        );

        registrar.playToClient(
                OpenPersonnelScreen.TYPE,
                OpenPersonnelScreen.STREAM_CODEC,
                OpenPersonnelScreen::handle
        );

        registrar.playToClient(
                AddIntercomToClient.TYPE,
                AddIntercomToClient.STREAM_CODEC,
                AddIntercomToClient::handle
        );

        registrar.playToClient(
                RemoveIntercomFromClient.TYPE,
                RemoveIntercomFromClient.STREAM_CODEC,
                RemoveIntercomFromClient::handle
        );

        registrar.commonBidirectional(
                AddPersonnelEntry.TYPE,
                AddPersonnelEntry.STREAM_CODEC,
                AddPersonnelEntry::handle
        );

        registrar.playToClient(
                SyncPersonnelRegistry.TYPE,
                SyncPersonnelRegistry.STREAM_CODEC,
                SyncPersonnelRegistry::handle
        );

        registrar.playToClient(
                SyncPrivileges.TYPE,
                SyncPrivileges.STREAM_CODEC,
                SyncPrivileges::handle
        );

        registrar.commonBidirectional(
                AddPrivilege.TYPE,
                AddPrivilege.STREAM_CODEC,
                AddPrivilege::handle
        );

        registrar.commonBidirectional(
                SetPrivilegeForGroup.TYPE,
                SetPrivilegeForGroup.STREAM_CODEC,
                SetPrivilegeForGroup::handle
        );

        registrar.commonBidirectional(
                RemovePrivilegeForGroup.TYPE,
                RemovePrivilegeForGroup.STREAM_CODEC,
                RemovePrivilegeForGroup::handle
        );

        registrar.commonBidirectional(
                SetPrivilegeForEntry.TYPE,
                SetPrivilegeForEntry.STREAM_CODEC,
                SetPrivilegeForEntry::handle
        );

        registrar.commonBidirectional(
                RemovePrivilegeForEntry.TYPE,
                RemovePrivilegeForEntry.STREAM_CODEC,
                RemovePrivilegeForEntry::handle
        );

        registrar.commonBidirectional(
                RemovePrivilege.TYPE,
                RemovePrivilege.STREAM_CODEC,
                RemovePrivilege::handle
        );

        registrar.commonBidirectional(
                AddPrivilegeGroup.TYPE,
                AddPrivilegeGroup.STREAM_CODEC,
                AddPrivilegeGroup::handle
        );

        registrar.commonBidirectional(
                RemovePrivilegeGroup.TYPE,
                RemovePrivilegeGroup.STREAM_CODEC,
                RemovePrivilegeGroup::handle
        );

        registrar.commonBidirectional(
                RemovePersonnelEntry.TYPE,
                RemovePersonnelEntry.STREAM_CODEC,
                RemovePersonnelEntry::handle
        );

        registrar.commonBidirectional(
                UpdatePersonnelEntry.TYPE,
                UpdatePersonnelEntry.STREAM_CODEC,
                UpdatePersonnelEntry::handle
        );

        registrar.playToServer(
                SwitchCharacter.TYPE,
                SwitchCharacter.STREAM_CODEC,
                SwitchCharacter::handle
        );

        registrar.playToServer(
                AddEffect.TYPE,
                AddEffect.STREAM_CODEC,
                AddEffect::handle
        );

        registrar.playToServer(
                UpdateCharacter.TYPE,
                UpdateCharacter.STREAM_CODEC,
                UpdateCharacter::handle
        );

        registrar.playToServer(
                OpenCharacterScreenC2S.TYPE,
                OpenCharacterScreenC2S.STREAM_CODEC,
                OpenCharacterScreenC2S::handle
        );

        registrar.playToClient(
                OpenCharacterScreenS2C.TYPE,
                OpenCharacterScreenS2C.STREAM_CODEC,
                OpenCharacterScreenS2C::handle
        );

        registrar.playToServer(
                AddRole.TYPE,
                AddRole.STREAM_CODEC,
                AddRole::handle
        );

        registrar.playToClient(
                SyncActiveCharacter.TYPE,
                SyncActiveCharacter.STREAM_CODEC,
                SyncActiveCharacter::handle
        );

        registrar.playBidirectional(
                SetLastTypingTime.TYPE,
                SetLastTypingTime.STREAM_CODEC,
                SetLastTypingTime::handle
        );

        registrar.playToClient(
                OpenSCP3771Screen.TYPE,
                OpenSCP3771Screen.STREAM_CODEC,
                OpenSCP3771Screen::handle
        );

        registrar.playToServer(
                SetPrivilegeForBE.TYPE,
                SetPrivilegeForBE.STREAM_CODEC,
                SetPrivilegeForBE::handle
        );

        registrar.playToServer(
                RemovePrivilegeForBE.TYPE,
                RemovePrivilegeForBE.STREAM_CODEC,
                RemovePrivilegeForBE::handle
        );

        registrar.playToClient(
                OpenPrivilegeEditorScreen.TYPE,
                OpenPrivilegeEditorScreen.STREAM_CODEC,
                OpenPrivilegeEditorScreen::handle
        );
    }
}
