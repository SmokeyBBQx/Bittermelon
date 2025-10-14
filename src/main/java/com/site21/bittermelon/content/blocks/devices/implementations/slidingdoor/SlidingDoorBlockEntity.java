package com.site21.bittermelon.content.blocks.devices.implementations.slidingdoor;

import com.site21.bittermelon.client.animation.LerpedFloat;
import com.site21.bittermelon.content.blocks.devices.ElectronicBlockEntity;
import com.site21.bittermelon.content.blocks.devices.PanelDevice;
import com.site21.bittermelon.content.blocks.devices.wiring.InputPort;
import com.site21.bittermelon.content.blocks.devices.wiring.OutputPort;
import com.site21.bittermelon.content.blocks.devices.wiring.Signal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.site21.bittermelon.content.blocks.devices.implementations.slidingdoor.SlidingDoorBlock.OPEN;
import static com.site21.bittermelon.content.blocks.devices.implementations.slidingdoor.SlidingDoorBlock.VISIBLE;
import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.SLIDING_DOOR_BLOCK_ENTITY;

public class SlidingDoorBlockEntity extends ElectronicBlockEntity implements PanelDevice {
    private final LerpedFloat animation;
    private final Map<String, OutputPort> outputPorts;
    private final Map<String, InputPort> inputPorts;
    private boolean isPanelOpen = false;

    public SlidingDoorBlockEntity(BlockPos pos, BlockState blockState) {
        super(SLIDING_DOOR_BLOCK_ENTITY.get(), pos, blockState);

        animation = LerpedFloat.linear().startWithValue(blockState.getValue(OPEN) ? 1 : 0);

        outputPorts = new LinkedHashMap<>(Map.of(
                "MOTORS_ACTIVE", new OutputPort("MOTORS_ACTIVE", null, worldPosition)
        ));

        inputPorts = new LinkedHashMap<>(Map.of(
                "POWER_SUPPLY", new InputPort("POWER_SUPPLY", null, worldPosition),
                "TOGGLE_MOTORS", new InputPort("TOGGLE_MOTORS", this::toggleMotors, worldPosition),
                "SET_MOTORS", new InputPort("SET_MOTORS", this::setMotors, worldPosition)
        ));
    }

    private static void tick(Level level, BlockPos pos, @NotNull BlockState state, @NotNull SlidingDoorBlockEntity door) {
        if (state.getValue(VISIBLE)) return;

        boolean open = state.getValue(OPEN);
        door.animation.animate(open ? 0.9f : 0, 0.15f, LerpedFloat.EasingFunction.LINEAR);
        door.animation.tick();

        if (!open && door.animation.finished()) {
            level.setBlock(pos, state.setValue(SlidingDoorBlock.VISIBLE, true), Block.UPDATE_ALL);
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, SlidingDoorBlockEntity blockEntity) {
        tick(level, pos, state, blockEntity);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, SlidingDoorBlockEntity blockEntity) {
        tick(level, pos, state, blockEntity);
    }

    public float getAnimationProgress(float partialTick) {
        return animation.getLerped(partialTick);
    }

    @Override
    public Map<String, InputPort> getInputPorts() {
        return inputPorts;
    }

    @Override
    public Map<String, OutputPort> getOutputPorts() {
        return outputPorts;
    }

    public float getIdleDraw() {
        return 15;
    }

    private boolean isOpen(@NotNull BlockState blockState) {
        return blockState.getValue(OPEN);
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
        drawPower(180);
        if (!isOn()) return;

        BlockState blockState = getBlockState();
        if (blockState.getBlock() instanceof SlidingDoorBlock slidingDoorBlock) {
            slidingDoorBlock.setOpen(level, worldPosition, open);
            triggerMotorsActiveOutput();
            runForOtherHalf(SlidingDoorBlockEntity::triggerMotorsActiveOutput);
        }

        sleep();
    }

    private void triggerMotorsActiveOutput() {
        InputPort connectedPort = findOutputPort("MOTORS_ACTIVE").getConnectedPort(level);
        if (connectedPort != null) {
            connectedPort.receive(new Signal(true));
        }
    }

    public void runForOtherHalf(Consumer<SlidingDoorBlockEntity> action) {
        BlockEntity otherBlockEntity = null;

        if (level == null) return;

        if (getBlockState().getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER) {
            otherBlockEntity = level.getBlockEntity(worldPosition.above());
        } else if (getBlockState().getValue(DoorBlock.HALF) == DoubleBlockHalf.UPPER) {
            otherBlockEntity = level.getBlockEntity(worldPosition.below());
        }

        if (otherBlockEntity instanceof SlidingDoorBlockEntity otherHalf) {
            action.accept(otherHalf);
        }
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
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);

        output.putBoolean("isPanelOpen", isPanelOpen);
        saveInputPorts(output);
        saveOutputPorts(output);
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);

        isPanelOpen = input.getBooleanOr("isPanelOpen", false);
        loadInputPorts(input);
        loadOutputPorts(input);
    }
}
