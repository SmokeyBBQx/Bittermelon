package com.site21.bittermelon.content.blocks.devices.wiring;

import com.site21.bittermelon.content.blocks.devices.ElectronicDevice;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class OutputPort extends Port<InputPort> {
    public final Supplier<?> supplier;

    public OutputPort(String id, Supplier<?> supplier, BlockPos pos) {
        super(id, pos);
        this.supplier = supplier;
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

        // If the cache is invalid, look up the connected port again
        if (!cacheValid) {
            cachedConnectedPort = null;
            if (level.getBlockEntity(connectedPos) instanceof ElectronicDevice electronic) {
                // If spliced, we need to find the output port and then get its connected port
                if (spliced) {
                    cachedConnectedPort = electronic.findOutputPort(connectedPortId).getConnectedPort(level);
                } else {
                    cachedConnectedPort = electronic.findInputPort(connectedPortId);
                }
            }
            cacheValid = true;
        }

        return cachedConnectedPort;
    }

}
