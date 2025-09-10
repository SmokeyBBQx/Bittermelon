package com.site21.bittermelon.content.blocks.devices.implementations.securedoor;

import com.site21.bittermelon.content.blocks.devices.ElectronicDevice;
import com.site21.bittermelon.content.blocks.devices.NetworkDevice;
import com.site21.bittermelon.content.blocks.devices.ElectronicBlockEntity;
import com.site21.bittermelon.content.blocks.devices.wiring.InputPort;
import com.site21.bittermelon.content.blocks.devices.wiring.OutputPort;
import com.site21.bittermelon.content.blocks.devices.wiring.Signal;
import com.site21.bittermelon.content.blocks.powergrid.distributionboard.DistributionBoardBlockEntity;
import com.site21.bittermelon.init.neoforge.BitterSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.SECURE_DOOR_BLOCK_ENTITY;

public class SecureDoorBlockEntity extends ElectronicBlockEntity implements ElectronicDevice, NetworkDevice {
    private final Map<String, OutputPort> outputPorts;
    private final Map<String, InputPort> inputPorts;
    private boolean isLocked = true;
    private int lockTickCounter = 0;
    private static final int LOCK_TICK_THRESHOLD = 80;
    private String address;
    private float draw = 255;
    private float supply = 0;

    public SecureDoorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        address = generateAddress("DOOR");

        outputPorts = Map.of(
                "IS_LOCKED", new OutputPort("IS_LOCKED", this::isLocked, worldPosition),
                "MOTORS_ACTIVE", new OutputPort("MOTORS_ACTIVE", null, worldPosition)
        );

        inputPorts = Map.of(
                "POWER_SUPPLY", new InputPort("POWER_SUPPLY", this::receivePower, worldPosition),
                "TOGGLE_LOCK", new InputPort("TOGGLE_LOCK", this::toggleLocked, worldPosition),
                "SET_LOCK", new InputPort("SET_LOCK", this::setLocked, worldPosition),
                "TOGGLE_MOTORS", new InputPort("TOGGLE_MOTORS", this::toggleMotors, worldPosition),
                "SET_MOTORS", new InputPort("SET_MOTORS", this::setMotors, worldPosition)
        );
    }

    public SecureDoorBlockEntity(BlockPos pos, BlockState state) {
        this(SECURE_DOOR_BLOCK_ENTITY.get(), pos, state);
    }

    public void tick() {
        if (!isLocked) {
            lockTickCounter++;
            if (lockTickCounter >= LOCK_TICK_THRESHOLD) {
                lockTickCounter = 0;
                setLocked(true);
                runForOtherHalf(otherHalf -> otherHalf.setLocked(true));
                setMotors(false);
                runForOtherHalf(SecureDoorBlockEntity::triggerMotorsActiveOutput);
                if (level == null) return;
                level.playSound(null, worldPosition, SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE.value(), SoundSource.BLOCKS);
            }
        }

        InputPort connectedPort = findOutputPort("IS_LOCKED").connectedPort;
        if (connectedPort != null) {
            connectedPort.receive(new Signal(isLocked));
        }
    }

    private void receivePower(Signal signal) {
        updatePowerConsumption();
    }

    private void updatePowerConsumption() {
        OutputPort connectedPort = inputPorts.get("POWER_SUPPLY").connectedPort;
        if (connectedPort == null) return;
        if (level == null) return;
        if (level.getBlockEntity(connectedPort.pos) instanceof DistributionBoardBlockEntity DB) {
            supply = DB.drawPower(connectedPort.id, draw);
        }
    }

    private boolean isOn() {
        if (supply >= draw) return true;
        if (supply <= 0 || level == null) return false;
        float random = level.getRandom().nextFloat();

        if (random < (supply / draw)) {
            return true;
        } else {
            level.playSound(null, worldPosition, BitterSounds.SPARKS.get(), SoundSource.BLOCKS, 1, 1);
            return false;
        }
    }

    private void toggleMotors(@NotNull Signal signal) {
        if (!isOn()) return;

        if (signal.asBoolean()) {
            BlockState blockState = getBlockState();
            if (blockState.getBlock() instanceof SecureDoorBlock secureDoorBlock) {
                if (isLocked && !secureDoorBlock.isOpen(blockState)) return;
                setMotors(!secureDoorBlock.isOpen(blockState));
                runForOtherHalf(SecureDoorBlockEntity::triggerMotorsActiveOutput);
            }
        }
    }

    private void setMotors(@NotNull Signal signal) {
        if (!isOn()) return;

        setMotors(signal.asBoolean());
        runForOtherHalf(SecureDoorBlockEntity::triggerMotorsActiveOutput);
    }

    public void setMotors(boolean open) {
        if (level == null) return;
        BlockState blockState = getBlockState();
        if (blockState.getBlock() instanceof SecureDoorBlock secureDoorBlock) {
            if (isLocked && !secureDoorBlock.isOpen(blockState)) return;
            secureDoorBlock.setOpen(null, level, blockState, worldPosition, open);
            triggerMotorsActiveOutput();
        }
    }

    private void triggerMotorsActiveOutput() {
        InputPort connectedPort = findOutputPort("MOTORS_ACTIVE").connectedPort;
        if (connectedPort != null) {
            connectedPort.receive(new Signal(true));
        }
    }

    private void toggleLocked(@NotNull Signal signal) {
        if (!isOn()) return;

        if (signal.asBoolean()) {
            setLocked(!isLocked);
            runForOtherHalf(otherHalf -> otherHalf.setLocked(isLocked));
        }
    }

    private void setLocked(@NotNull Signal signal) {
        if (!isOn()) return;

        setLocked(signal.asBoolean());
        runForOtherHalf(otherHalf -> otherHalf.setLocked(signal.asBoolean()));
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        if (!isOn()) return;

        if (locked != isLocked) {
            lockTickCounter = 0;
            isLocked = locked;
            setChanged();
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

    @Override
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        setChanged();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("isLocked", isLocked);
        saveInputPorts(tag);
        saveOutputPorts(tag);
        tag.putString("address", address);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        isLocked = tag.getBoolean("isLocked");
        loadInputPorts(tag, level);
        loadOutputPorts(tag, level);

        address = tag.getString("address");
    }

    public void runForOtherHalf(Consumer<SecureDoorBlockEntity> action) {
        BlockEntity otherBlockEntity = null;

        if (level == null) return;

        if (getBlockState().getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER) {
            otherBlockEntity = level.getBlockEntity(worldPosition.above());
        } else if (getBlockState().getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER) {
            otherBlockEntity = level.getBlockEntity(worldPosition.below());
        }

        if (otherBlockEntity instanceof SecureDoorBlockEntity otherHalf) {
            action.accept(otherHalf);
        }
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
