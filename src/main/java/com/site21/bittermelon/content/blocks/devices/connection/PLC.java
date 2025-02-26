package com.site21.bittermelon.content.blocks.devices.connection;

import com.site21.bittermelon.content.blocks.devices.IElectronic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PLC implements IElectronic {
    private final BlockPos worldPosition;
    private final List<Instruction> instructions = new ArrayList<>();

    public PLC(BlockPos worldPosition) {
        this.worldPosition = worldPosition;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void addInstruction(Instruction instruction) {
        instructions.add(instruction);
    }

    @Override
    public String getAddress() {
        return "";
    }

    @Override
    public Map<String, OutputPort> getOutputPorts() {
        return Map.of(
                "output_1", new OutputPort("output_1", null, worldPosition),
                "output_2", new OutputPort("output_2", null, worldPosition),
                "output_3", new OutputPort("output_3", null, worldPosition),
                "output_4", new OutputPort("output_4", null, worldPosition),
                "output_5", new OutputPort("output_5", null, worldPosition),
                "output_6", new OutputPort("output_6", null, worldPosition)
        );
    }

    @Override
    public Map<String, InputPort> getInputPorts() {
        return Map.of(
                "input_1", new InputPort("input_1", signal -> handleInput(signal, "input_1"), worldPosition),
                "input_2", new InputPort("input_2", signal -> handleInput(signal, "input_2"), worldPosition),
                "input_3", new InputPort("input_3", signal -> handleInput(signal, "input_3"), worldPosition),
                "input_4", new InputPort("input_4", signal -> handleInput(signal, "input_4"), worldPosition),
                "input_5", new InputPort("input_5", signal -> handleInput(signal, "input_5"), worldPosition),
                "input_6", new InputPort("input_6", signal -> handleInput(signal, "input_6"), worldPosition)
        );
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

