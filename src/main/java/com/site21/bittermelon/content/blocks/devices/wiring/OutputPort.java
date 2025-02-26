package com.site21.bittermelon.content.blocks.devices.wiring;

import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class OutputPort {
    public final String id;
    public final Supplier<?> supplier;
    public final BlockPos pos;
    public @Nullable InputPort connectedPort;

    public OutputPort(String id, Supplier<?> supplier, BlockPos pos) {
        this.id = id;
        this.supplier = supplier;
        this.pos = pos;
    }

    @Contract(" -> new")
    public @NotNull Signal emit() {
        return new Signal(supplier.get());
    }

    public void update() {
        if (connectedPort != null) {
            connectedPort.receive(emit());
        }
    }
}
