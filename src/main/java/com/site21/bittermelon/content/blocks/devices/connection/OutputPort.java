package com.site21.bittermelon.content.blocks.devices.connection;

import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public record OutputPort(String id, Supplier<?> supplier, BlockPos pos) {
    @Contract(" -> new")
    public @NotNull Signal emit() {
        return new Signal(supplier.get());
    }
}
