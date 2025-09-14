package com.site21.bittermelon.content.blocks.devices.wiring;

import com.site21.bittermelon.content.blocks.devices.ElectronicDevice;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
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

        if (!cacheValid) {
            cachedConnectedPort = null;
            if (level.getBlockEntity(connectedPos) instanceof ElectronicDevice electronic) {
                cachedConnectedPort = electronic.findOutputPort(connectedPortId);
            }
            cacheValid = true;
        }

        return cachedConnectedPort;
    }
}
