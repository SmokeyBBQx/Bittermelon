package com.site21.bittermelon.content.blocks.devices.wiring;

import com.site21.bittermelon.content.blocks.devices.ElectronicDevice;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PLC implements ElectronicDevice {
    private final BlockPos worldPosition;
    private final List<Instruction> instructions = new ArrayList<>();
    private final Map<String, InputPort> inputPorts;
    private final Map<String, OutputPort> outputPorts;

    public PLC(BlockPos worldPosition) {
        this.worldPosition = worldPosition;

        inputPorts = Map.of(
                "INPUT_1", new InputPort("INPUT_1", signal -> handleInput(signal, "INPUT_1"), worldPosition),
                "INPUT_2", new InputPort("INPUT_2", signal -> handleInput(signal, "INPUT_2"), worldPosition),
                "INPUT_3", new InputPort("INPUT_3", signal -> handleInput(signal, "INPUT_3"), worldPosition),
                "INPUT_4", new InputPort("INPUT_4", signal -> handleInput(signal, "INPUT_4"), worldPosition),
                "INPUT_5", new InputPort("INPUT_5", signal -> handleInput(signal, "INPUT_5"), worldPosition),
                "INPUT_6", new InputPort("INPUT_6", signal -> handleInput(signal, "INPUT_6"), worldPosition)
        );

        outputPorts = Map.of(
                "OUTPUT_1", new OutputPort("OUTPUT_1", null, worldPosition),
                "OUTPUT_2", new OutputPort("OUTPUT_2", null, worldPosition),
                "OUTPUT_3", new OutputPort("OUTPUT_3", null, worldPosition),
                "OUTPUT_4", new OutputPort("OUTPUT_4", null, worldPosition),
                "OUTPUT_5", new OutputPort("OUTPUT_5", null, worldPosition),
                "OUTPUT_6", new OutputPort("OUTPUT_6", null, worldPosition)
        );

    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void addInstruction(Instruction instruction) {
        instructions.add(instruction);
    }

    @Override
    public Map<String, OutputPort> getOutputPorts() {
        return outputPorts;
    }

    @Override
    public Map<String, InputPort> getInputPorts() {
        return inputPorts;
    }

    private void handleInput(Signal signal, String inputID) {
        for (Instruction instruction : instructions) {
            if (Objects.equals(instruction.inputID(), inputID)) {
                OutputPort outputPort = findOutputPort(instruction.outputID());
                if (outputPort.connectedPort != null) {
                    outputPort.connectedPort.receive(instruction.logicalOperator().apply(signal));
                }
            }
        }
    }

    public void save(@NotNull CompoundTag tag) {
        CompoundTag plcTag = new CompoundTag();
        saveInputPorts(plcTag);
        saveOutputPorts(plcTag);
        ListTag instructionList = new ListTag();
        for (Instruction instruction : instructions) {
            instructionList.add(instruction.save());
        }
        plcTag.put("instructions", instructionList);
        tag.put("plc", plcTag);
    }

    public void load(@NotNull CompoundTag tag, Level level) {
        CompoundTag plcTag = tag.getCompound("plc");
        loadInputPorts(plcTag, level);
        loadOutputPorts(plcTag, level);
        instructions.clear();
        ListTag instructionList = tag.getList("instructions", Tag.TAG_COMPOUND);
        for (int i = 0; i < instructionList.size(); i++) {
            CompoundTag instructionTag = instructionList.getCompound(i);
            instructions.add(Instruction.load(instructionTag));
        }
    }
}

