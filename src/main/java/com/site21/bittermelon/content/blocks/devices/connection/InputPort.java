package com.site21.bittermelon.content.blocks.devices.connection;

import net.minecraft.core.BlockPos;

import java.util.function.Consumer;

public record InputPort(String id, Consumer<Signal> handler, BlockPos pos) {
    public void receive(Signal signal) {
        handler.accept(signal);
    }
}
