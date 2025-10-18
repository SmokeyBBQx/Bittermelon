package com.site21.bittermelon.common.content.blocks.electronics.securedoor;

import com.site21.bittermelon.common.content.blocks.electronics.slidingdoor.SlidingDoorBlock;
import com.site21.bittermelon.common.systems.electronics.ElectronicDevice;
import com.site21.bittermelon.common.systems.electronics.NetworkDevice;
import com.site21.bittermelon.common.content.blocks.electronics.ElectronicBlockEntity;
import com.site21.bittermelon.common.systems.electronics.PanelDevice;
import com.site21.bittermelon.common.systems.electronics.wiring.InputPort;
import com.site21.bittermelon.common.systems.electronics.wiring.OutputPort;
import com.site21.bittermelon.common.systems.electronics.wiring.Signal;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.SECURE_DOOR_BLOCK_ENTITY;

public class SecureDoorBlockEntity extends ElectronicBlockEntity implements ElectronicDevice, NetworkDevice, PanelDevice {
    private static final int LOCK_TICK_THRESHOLD = 80;

    private final Map<String, OutputPort> outputPorts;
    private final Map<String, InputPort> inputPorts;
    private boolean isLocked = true;
    private int lockTickCounter = 0;
    private String address;
    public boolean isPanelOpen = false;

    public SecureDoorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        address = generateAddress("DOOR");

        outputPorts = new LinkedHashMap<>(Map.of(
                "IS_LOCKED", new OutputPort("IS_LOCKED", this::isLocked, worldPosition),
                "MOTORS_ACTIVE", new OutputPort("MOTORS_ACTIVE", null, worldPosition)
        ));

        inputPorts = new LinkedHashMap<>(Map.of(
                "POWER_SUPPLY", new InputPort("POWER_SUPPLY", this::receivePower, worldPosition),
                "TOGGLE_LOCK", new InputPort("TOGGLE_LOCK", this::toggleLocked, worldPosition),
                "SET_LOCK", new InputPort("SET_LOCK", this::setLocked, worldPosition),
                "TOGGLE_MOTORS", new InputPort("TOGGLE_MOTORS", this::toggleMotors, worldPosition),
                "SET_MOTORS", new InputPort("SET_MOTORS", this::setMotors, worldPosition),
                "TOGGLE_OPEN", new InputPort("TOGGLE_OPEN", this::toggleOpen, worldPosition
        )));
    }

    public SecureDoorBlockEntity(BlockPos pos, BlockState state) {
        this(SECURE_DOOR_BLOCK_ENTITY.get(), pos, state);
    }

    public void tick() {
        if (level == null) return;

        if (!isLocked) {
            lockTickCounter++;
            if (lockTickCounter >= LOCK_TICK_THRESHOLD) {
                lockTickCounter = 0;
                setLocked(true);
                runForOtherHalf(otherHalf -> otherHalf.setLocked(true));
                setMotors(false);
                runForOtherHalf(SecureDoorBlockEntity::triggerMotorsActiveOutput);
                level.playSound(null, worldPosition, SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE.value(), SoundSource.BLOCKS);
            }
        }

        InputPort connectedPort = findOutputPort("IS_LOCKED").getConnectedPort(level);
        if (connectedPort != null) {
            connectedPort.receive(new Signal(isLocked));
        }
    }

    public float getIdleDraw() {
        return 15;
    }

    private void receivePower(Signal signal) {
        drawPower(draw);
    }

    private boolean isOpen(@NotNull BlockState blockState) {
        return blockState.getValue(DoorBlock.OPEN);
    }

    private void toggleMotors(@NotNull Signal signal) {
        if (signal.asBoolean()) {
            setMotors(!isOpen(getBlockState()));
        }
    }

    private void setMotors(@NotNull Signal signal) {
        setMotors(signal.asBoolean());
    }

    public void setMotors(boolean open) {
        if (level == null) return;

        drawPower(180);
        if (!isOn()) return;

        BlockState blockState = getBlockState();
        if (blockState.getBlock() instanceof SecureDoorBlock secureDoorBlock) {
            if (isLocked && !secureDoorBlock.isOpen(blockState)) return;
            secureDoorBlock.setOpen(null, level, blockState, worldPosition, open);
            triggerMotorsActiveOutput();
        }

        sleep();
    }

    private void triggerMotorsActiveOutput() {
        InputPort connectedPort = findOutputPort("MOTORS_ACTIVE").getConnectedPort(level);
        if (connectedPort != null) {
            connectedPort.receive(new Signal(true));
        }
    }

    private void toggleLocked(@NotNull Signal signal) {
        if (signal.asBoolean()) {
            setLocked(!isLocked);
            runForOtherHalf(otherHalf -> otherHalf.setLocked(isLocked));
        }
    }

    private void setLocked(@NotNull Signal signal) {
        setLocked(signal.asBoolean());
        runForOtherHalf(otherHalf -> otherHalf.setLocked(signal.asBoolean()));
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        drawPower(110);
        if (!isOn()) return;

        if (locked != isLocked) {
            lockTickCounter = 0;
            isLocked = locked;
            setChanged();
        }

        sleep();
    }

    private void toggleOpen(@NotNull Signal signal) {
        if (signal.asBoolean()) {
            setLocked(false);
            setMotors(!isOpen(getBlockState()));
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
    public boolean isPanelOpen() {
        return isPanelOpen;
    }

    @Override
    public void togglePanel() {
        isPanelOpen = !isPanelOpen;
        runForOtherHalf(otherHalf -> otherHalf.isPanelOpen = isPanelOpen);
        setChanged();
    }

    @Override
    public boolean canOpenPanel() {
        return getBlockState().getValue(SlidingDoorBlock.HALF) == DoubleBlockHalf.UPPER;
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);

        output.putBoolean("isLocked", isLocked);
        output.putString("address", address);
        output.putBoolean("isPanelOpen", isPanelOpen);
        saveInputPorts(output);
        saveOutputPorts(output);
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);

        isLocked = input.getBooleanOr("isLocked", true);
        address = input.getStringOr("address", generateAddress("DOOR"));
        isPanelOpen = input.getBooleanOr("isPanelOpen", false);
        loadInputPorts(input);
        loadOutputPorts(input);
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
}
