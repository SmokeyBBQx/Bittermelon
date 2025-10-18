package com.site21.bittermelon.common.content.blocks.electronics.intercom;

import com.site21.bittermelon.common.systems.electronics.ElectronicDevice;
import com.site21.bittermelon.common.content.blocks.electronics.ElectronicBlockEntity;
import com.site21.bittermelon.common.systems.electronics.wiring.InputPort;
import com.site21.bittermelon.common.systems.electronics.wiring.OutputPort;
import com.site21.bittermelon.common.systems.electronics.wiring.Signal;
import com.site21.bittermelon.common.content.blocks.powergrid.distributionboard.DistributionBoardBlockEntity;
import com.site21.bittermelon.common.systems.syncsound.ISyncSoundListener;
import com.site21.bittermelon.common.systems.syncsound.SyncSoundEvent;
import com.site21.bittermelon.common.systems.syncsound.SyncSoundType;
import com.site21.bittermelon.common.systems.telecomms.intercom.IntercomManager;
import com.site21.bittermelon.util.LocalMessageHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.INTERCOM_BLOCK_ENTITY;

public class IntercomBlockEntity extends ElectronicBlockEntity implements ISyncSoundListener, ElectronicDevice {
    private static final int LISTENING_RADIUS = 8;
    private int speakerRadius = 8;
    private String intercomID = "";
    private String targetID = "";
    private boolean speakerOn = true;
    private boolean micOn = true;
    private boolean phonePickedUp = false;
    private Player phoneUser;
    private final Map<String, OutputPort> outputPorts;
    private final Map<String, InputPort> inputPorts;
    private float draw = 255;
    private float supply = 0;

    public IntercomBlockEntity(BlockPos pos, BlockState blockState) {
        super(INTERCOM_BLOCK_ENTITY.get(), pos, blockState);

        outputPorts = Map.of(
                "SOUND", new OutputPort("SOUND", null, worldPosition)
        );

        inputPorts = Map.of(
                "POWER_SUPPLY", new InputPort("POWER_SUPPLY", this::receivePower, worldPosition)
        );
    }

    private void receivePower(Signal signal) {
        updatePowerConsumption();
    }

    private void updatePowerConsumption() {
        OutputPort connectedPort = inputPorts.get("POWER_SUPPLY").getConnectedPort(level);
        if (connectedPort == null) return;
        if (level == null) return;
        if (level.getBlockEntity(connectedPort.pos) instanceof DistributionBoardBlockEntity DB) {
            supply = DB.drawPower(connectedPort.id, draw);
        }
    }

    @Override
    public void onSyncSound(@NotNull SyncSoundEvent event) {
        if (!isOn() || !micOn || event.getSoundType() == SyncSoundType.SPEAKER) return;

        if (level == null || level.isClientSide) return;

        if (canHearSound(event, getBlockPos(), isPhonePickedUp() ? 1 : LISTENING_RADIUS)) {
            IntercomManager.get(level).transmitMessage(event, targetID, level);
        }
    }

    public void transmitMessage(SyncSoundEvent event) {
        if (!isOn() || !speakerOn || level == null || level.isClientSide) return;

        Component intercomMessage = Component.literal("[INTERCOM]: ").append(event.getSoundDescription());

        NeoForge.EVENT_BUS.post(new SyncSoundEvent(level, getBlockPos(), SyncSoundType.SPEAKER, intercomMessage, isPhonePickedUp() ? 1 : speakerRadius, event.getSoundEvent()));
        if (event.getSoundEvent() != null) {
            level.playSound(null, worldPosition, event.getSoundEvent(), SoundSource.NEUTRAL, 0.05f, 1);
        }
        LocalMessageHelper.sendLocalMessage(level, getBlockPos(), isPhonePickedUp() ? 1 : speakerRadius, intercomMessage);
        InputPort connectedPort = findOutputPort("SOUND").getConnectedPort(level);
        if (connectedPort != null) {
            connectedPort.receive(new Signal(event));
        }
    }

    public String getIntercomID() {
        return intercomID;
    }

    public void setIntercomID(String intercomID) {
        this.intercomID = intercomID;
        if (level != null) {
            IntercomManager.get(level).addIntercom(getBlockPos(), intercomID);
        }
        setChanged();
    }

    public String getTargetID() {
        return targetID;
    }

    public void setTargetID(String targetID) {
        this.targetID = targetID;
        setChanged();
    }

    public boolean isSpeakerOn() {
        return speakerOn;
    }

    public void setSpeakerOn(boolean speakerOn) {
        this.speakerOn = speakerOn;
        setChanged();
    }

    public boolean isMicOn() {
        return micOn;
    }

    public void setMicOn(boolean micOn) {
        this.micOn = micOn;
        setChanged();
    }

    public boolean isPhonePickedUp() {
        return phonePickedUp;
    }

    public void setPhonePickedUp(boolean phonePickedUp) {
        this.phonePickedUp = phonePickedUp;
        setChanged();
    }

    public Player getPhoneUser() {
        return phoneUser;
    }

    public void setPhoneUser(Player player) {
        this.phoneUser = player;
        setChanged();
    }

    @Override
    public Map<String, OutputPort> getOutputPorts() {
        return outputPorts;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (level != null && !level.isClientSide()) {
            NeoForge.EVENT_BUS.addListener(this::onSyncSound);
        }
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (level != null && !level.isClientSide()) {
            NeoForge.EVENT_BUS.unregister(this);
        }
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);

        output.putString("intercomID", intercomID);
        output.putString("targetID", targetID);
        output.putBoolean("speakerOn", speakerOn);
        output.putBoolean("micOn", micOn);
        output.putBoolean("phonePickedUp", phonePickedUp);
        output.putInt("speakerRadius", speakerRadius);
        if (phoneUser != null) {
            output.store("phoneUser", UUIDUtil.CODEC, phoneUser.getUUID());
        }
        saveOutputPorts(output);
        saveInputPorts(output);
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);

        intercomID = input.getStringOr("intercomID", "");
        targetID = input.getStringOr("targetID", "");
        speakerOn = input.getBooleanOr("speakerOn", true);
        micOn = input.getBooleanOr("micOn", true);
        phonePickedUp = input.getBooleanOr("phonePickedUp", false);
        speakerRadius = input.getIntOr("speakerRadius", 8);
        if (level == null) return;
        input.read("phoneUser", UUIDUtil.CODEC).ifPresent(uuid -> {
            if (level == null) return;
            Player loadedPhoneUser = level.getPlayerByUUID(uuid);
            if (loadedPhoneUser != null) {
                phoneUser = loadedPhoneUser;
            }
        });
    }
}
