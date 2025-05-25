package com.site21.bittermelon.content.blocks.devices.implementations.containmentalarm;

import com.site21.bittermelon.content.blocks.devices.ElectronicDevice;
import com.site21.bittermelon.content.blocks.devices.ElectronicBlockEntity;
import com.site21.bittermelon.content.blocks.devices.wiring.*;
import com.site21.bittermelon.content.syncsound.SyncSoundEvent;
import com.site21.bittermelon.content.syncsound.SyncSoundType;
import com.site21.bittermelon.init.neoforge.BitterSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.CONTAINMENT_ALARM_BLOCK_ENTITY;

public class ContainmentAlarmBlockEntity extends ElectronicBlockEntity implements ElectronicDevice, PLCUser {
    private final Map<String, OutputPort> outputPorts = new HashMap<>();
    private final Map<String, InputPort> inputPorts = new HashMap<>();
    private PLC plc = new PLC(worldPosition);
    private boolean isActive = false;
    private boolean isAlerted = false;
    private boolean isEmergency = false;
    private int alertSoundCounter = 0;
    private static final int ALERT_SOUND_INTERVAL = 40;

    public ContainmentAlarmBlockEntity(BlockPos pos, BlockState blockState) {
        super(CONTAINMENT_ALARM_BLOCK_ENTITY.get(), pos, blockState);
        initializePorts();
    }

    private void initializePorts() {
        InputPort SET_ALERT = new InputPort("SET_ALERT", this::setAlert, worldPosition);
        InputPort SET_EMERGENCY = new InputPort("SET_EMERGENCY", this::setEmergency, worldPosition);

        inputPorts.put(SET_ALERT.id, SET_ALERT);
        inputPorts.put(SET_EMERGENCY.id, SET_EMERGENCY);

        OutputPort IS_ALERTED = new OutputPort("IS_ALERTED", this::isAlerted, worldPosition);
        OutputPort IS_EMERGENCY = new OutputPort("IS_EMERGENCY", this::isEmergency, worldPosition);

        outputPorts.put(IS_ALERTED.id, IS_ALERTED);
        outputPorts.put(IS_EMERGENCY.id, IS_EMERGENCY);

        connectToOutputPort("OUTPUT_1", SET_ALERT);
        connectToOutputPort("OUTPUT_2", SET_EMERGENCY);

        connectToInputPort("INPUT_1", IS_ALERTED);
        connectToInputPort("INPUT_2", IS_EMERGENCY);
    }

    public void tick() {
        if (level == null || level.isClientSide) return;

        if (isAlerted) {
            alertSoundCounter++;
            if (alertSoundCounter >= ALERT_SOUND_INTERVAL) {
                level.playSound(null, worldPosition, BitterSounds.CONTAINMENT_ALERT.get(), SoundSource.NEUTRAL, 0.3f, 1);
                NeoForge.EVENT_BUS.post(new SyncSoundEvent(level, getBlockPos(), SyncSoundType.SPEAKER, Component.literal("(alarm)").withStyle(ChatFormatting.ITALIC).withColor(0xFF808080), 16));
                alertSoundCounter = 0;
            }
        }
    }

    @Override
    public Map<String, OutputPort> getOutputPorts() {
        return outputPorts;
    }

    @Override
    public Map<String, InputPort> getInputPorts() {
        return inputPorts;
    }

    public boolean isAlerted() {
        return isAlerted;
    }

    public boolean isEmergency() {
        return isEmergency;
    }

    public void setAlert(boolean value) {
        if (isAlerted != value) {
            isAlerted = value;
            setChanged();
        }
    }

    public void setAlert(@NotNull Signal signal) {
        setAlert(signal.asBoolean());
    }

    public void setEmergency(boolean value) {
        if (isEmergency != value) {
            isEmergency = value;
            setChanged();
        }
    }

    private void setEmergency(@NotNull Signal signal) {
        setEmergency(signal.asBoolean());
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("isActive", isActive);
        tag.putBoolean("isAlerted", isAlerted);
        tag.putBoolean("isEmergency", isEmergency);
        plc.save(tag);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        isActive = tag.getBoolean("isActive");
        isAlerted = tag.getBoolean("isAlerted");
        isEmergency = tag.getBoolean("isEmergency");
        plc.load(tag, level);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public PLC getPLC() {
        return plc;
    }
}
