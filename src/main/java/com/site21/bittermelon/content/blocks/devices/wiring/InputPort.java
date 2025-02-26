package com.site21.bittermelon.content.blocks.devices.wiring;

import net.minecraft.core.BlockPos;

import java.util.function.Consumer;

public class InputPort {
    public final String id;
    public final Consumer<Signal> handler;
    public final BlockPos pos;
    public OutputPort connectedPort;

    public InputPort(String id, Consumer<Signal> handler, BlockPos pos) {
        this.id = id;
        this.handler = handler;
        this.pos = pos;
    }

    public void receive(Signal signal) {
        handler.accept(signal);
    }

    public void update() {
        if (connectedPort != null) {
            handler.accept(connectedPort.emit());
        }
    }
}
