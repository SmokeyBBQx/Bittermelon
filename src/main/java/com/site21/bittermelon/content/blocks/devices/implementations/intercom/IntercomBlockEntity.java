package com.site21.bittermelon.content.blocks.devices.implementations.intercom;

import com.site21.bittermelon.content.blocks.devices.IElectronic;
import com.site21.bittermelon.content.blocks.devices.wiring.InputPort;
import com.site21.bittermelon.content.blocks.devices.wiring.OutputPort;
import com.site21.bittermelon.content.blocks.devices.wiring.Signal;
import com.site21.bittermelon.content.syncsound.ISyncSoundListener;
import com.site21.bittermelon.content.syncsound.SyncSoundEvent;
import com.site21.bittermelon.content.syncsound.SyncSoundType;
import com.site21.bittermelon.content.telecomms.intercom.IntercomManager;
import com.site21.bittermelon.util.LocalMessageHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.INTERCOM_BLOCK_ENTITY;

public class IntercomBlockEntity extends BlockEntity implements ISyncSoundListener, IElectronic {
    private static final int LISTENING_RADIUS = 8;
    private int speakerRadius = 8;
    private String intercomID = "";
    private String targetID = "";
    private boolean speakerOn = true;
    private boolean micOn = true;
    private boolean phonePickedUp = false;
    private final Map<String, OutputPort> outputPorts;

    public IntercomBlockEntity(BlockPos pos, BlockState blockState) {
        super(INTERCOM_BLOCK_ENTITY.get(), pos, blockState);

        outputPorts = Map.of(
                "SOUND", new OutputPort("SOUND", null, worldPosition)
        );
    }

    @Override
    public void onSyncSound(@NotNull SyncSoundEvent event) {
        if (!micOn || event.getSoundType() == SyncSoundType.SPEAKER) return;

        if (level == null || level.isClientSide) return;

        if (canHearSound(event, getBlockPos(), isPhonePickedUp() ? 1 : LISTENING_RADIUS)) {
            IntercomManager.get(level).transmitMessage(event, targetID, level);
        }
    }

    public void transmitMessage(SyncSoundEvent event) {
        if (!speakerOn || level == null || level.isClientSide) return;

        Component intercomMessage = Component.literal("[INTERCOM]: ").append(event.getSoundDescription());

        NeoForge.EVENT_BUS.post(new SyncSoundEvent(level, getBlockPos(), SyncSoundType.SPEAKER, intercomMessage, isPhonePickedUp() ? 1 : speakerRadius, event.getSoundEvent()));
        if (event.getSoundEvent() != null) {
            level.playSound(null, worldPosition, event.getSoundEvent(), SoundSource.NEUTRAL, 0.05f, 1);
        }
        LocalMessageHelper.sendLocalMessage(level, getBlockPos(), isPhonePickedUp() ? 1 : speakerRadius, intercomMessage);
        InputPort connectedPort = findOutputPort("SOUND").connectedPort;
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
            IntercomManager.get(level).removeIntercom(worldPosition);
            NeoForge.EVENT_BUS.unregister(this);
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);

        tag.putString("intercomID", intercomID);
        tag.putString("targetID", targetID);
        tag.putBoolean("speakerOn", speakerOn);
        tag.putBoolean("micOn", micOn);
        tag.putBoolean("phonePickedUp", phonePickedUp);
        tag.putInt("speakerRadius", speakerRadius);
        saveOutputPorts(tag);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);

        intercomID = tag.getString("intercomID");
        targetID = tag.getString("targetID");
        speakerOn = tag.getBoolean("speakerOn");
        micOn = tag.getBoolean("micOn");
        phonePickedUp = tag.getBoolean("phonePickedUp");
        speakerRadius = tag.getInt("speakerRadius");
        loadOutputPorts(tag, level);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        return this.saveCustomOnly(registries);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        syncToClient();
    }

    public void syncToClient() {
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public String getAddress() {
        return "";
    }
}
