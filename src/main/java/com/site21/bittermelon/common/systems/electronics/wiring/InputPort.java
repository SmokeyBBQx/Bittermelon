package com.site21.bittermelon.common.systems.electronics.wiring;

import com.site21.bittermelon.common.systems.electronics.ElectronicDevice;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class InputPort extends Port<OutputPort> {
    public final Consumer<Signal> consumer;

    public InputPort(String id, Consumer<Signal> consumer, BlockPos pos) {
        super(id, pos);
        this.consumer = consumer;
    }

    public void receive(Signal signal) {
        consumer.accept(signal);
    }

    public @Nullable OutputPort getConnectedPort(Level level) {
        if (connectedPos == null || connectedPortId == null) {
            return null;
        }

        // If the cache is invalid, look up the connected port again
        if (!cacheValid) {
            cachedConnectedPort = null;
            if (level.getBlockEntity(connectedPos) instanceof ElectronicDevice electronic) {
                // If spliced, we need to find the output port and then get its connected port
                if (spliced) {
                    cachedConnectedPort = electronic.findInputPort(connectedPortId).getConnectedPort(level);
                } else {
                    cachedConnectedPort = electronic.findOutputPort(connectedPortId);
                }
            }
            cacheValid = true;
        }

        return cachedConnectedPort;
    }
}
