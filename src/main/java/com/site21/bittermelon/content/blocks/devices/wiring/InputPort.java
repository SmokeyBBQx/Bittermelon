package com.site21.bittermelon.content.blocks.devices.wiring;

import com.site21.bittermelon.content.blocks.devices.ElectronicDevice;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class InputPort {
    public final String id;
    public final Consumer<Signal> consumer;
    public final BlockPos pos;
    public @Nullable BlockPos connectedPos;
    public @Nullable String connectedPortId;

    private @Nullable OutputPort cachedConnectedPort;
    private boolean cacheValid = false;

    public InputPort(String id, Consumer<Signal> consumer, BlockPos pos) {
        this.id = id;
        this.consumer = consumer;
        this.pos = pos;
    }

    public void receive(Signal signal) {
        consumer.accept(signal);
    }

    public @Nullable OutputPort getConnectedPort(Level level) {
        if (connectedPos == null || connectedPortId == null) {
            return null;
        }

        if (!cacheValid) {
            cachedConnectedPort = null;
            if (level.getBlockEntity(connectedPos) instanceof ElectronicDevice electronic) {
                cachedConnectedPort = electronic.findOutputPort(connectedPortId);
            }
            cacheValid = true;
        }

        return cachedConnectedPort;
    }

    public void connectTo(@NotNull OutputPort outputPort) {
        connectedPos = outputPort.pos;
        connectedPortId = outputPort.id;
        cachedConnectedPort = outputPort;
        cacheValid = true;
    }

    public void disconnect() {
        connectedPos = null;
        connectedPortId = null;
        cachedConnectedPort = null;
        cacheValid = false;
    }

    public void invalidateCache() {
        cacheValid = false;
        cachedConnectedPort = null;
    }
}
