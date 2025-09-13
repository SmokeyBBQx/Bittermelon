package com.site21.bittermelon.content.blocks.devices.wiring;

import com.site21.bittermelon.content.blocks.devices.ElectronicDevice;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class OutputPort {
    public final String id;
    public final Supplier<?> supplier;
    public final BlockPos pos;
    public @Nullable BlockPos connectedPos;
    public @Nullable String connectedPortId;

    private @Nullable InputPort cachedConnectedPort;
    private boolean cacheValid = false;

    public OutputPort(String id, Supplier<?> supplier, BlockPos pos) {
        this.id = id;
        this.supplier = supplier;
        this.pos = pos;
    }

    @Contract(" -> new")
    public @NotNull Signal emit() {
        return new Signal(supplier.get());
    }

    public void update(Level level) {
        InputPort connectedPort = getConnectedPort(level);
        if (connectedPort != null) {
            connectedPort.receive(emit());
        }
    }

    public @Nullable InputPort getConnectedPort(Level level) {
        if (connectedPos == null || connectedPortId == null) {
            return null;
        }

        if (!cacheValid) {
            cachedConnectedPort = null;
            if (level.getBlockEntity(connectedPos) instanceof ElectronicDevice electronic) {
                cachedConnectedPort = electronic.findInputPort(connectedPortId);
            }
            cacheValid = true;
        }

        return cachedConnectedPort;
    }

    public void connectTo(@NotNull InputPort inputPort) {
        connectedPos = inputPort.pos;
        connectedPortId = inputPort.id;
        cachedConnectedPort = inputPort;
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
