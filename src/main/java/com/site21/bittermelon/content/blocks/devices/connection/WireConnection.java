package com.site21.bittermelon.content.blocks.devices.connection;

import com.site21.bittermelon.content.blocks.devices.IElectronic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record WireConnection(InputPort inputPort, OutputPort outputPort) {
    public void update() {
        inputPort().receive(outputPort.emit());
    }

    public @NotNull CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putLong("outputPos", outputPort.pos().asLong());
        tag.putLong("inputPos", inputPort.pos().asLong());

        tag.putString("outputID", outputPort.id());
        tag.putString("inputID", inputPort.id());

        return tag;
    }

    @Contract("_, _ -> new")
    public static @Nullable WireConnection load(@NotNull CompoundTag tag, @NotNull Level level) {
        return load(
                tag.getString("outputID"),
                tag.getString("inputID"),
                BlockPos.of(tag.getLong("outputPos")),
                BlockPos.of(tag.getLong("inputPos")),
                level
        );
    }

    public static @Nullable WireConnection load(String outputID, String inputID, BlockPos outputPos, BlockPos inputPos, @NotNull Level level) {
        OutputPort outputPort = null;
        InputPort inputPort = null;

        if (level.getBlockEntity(outputPos) instanceof IElectronic outputDevice) {
            outputPort = outputDevice.findOutputPort(outputID);
        }
        if (level.getBlockEntity(inputPos) instanceof IElectronic inputDevice) {
            inputPort = inputDevice.findInputPort(inputID);
        }

        if (outputPort == null || inputPort == null) {
            return null;
        }

        return new WireConnection(inputPort, outputPort);
    }
}
