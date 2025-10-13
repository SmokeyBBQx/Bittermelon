package com.site21.bittermelon.content.blocks.devices.wiring;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Port<T extends Port<? extends Port<?>>> {
    public final String id;
    public final BlockPos pos;
    public @Nullable BlockPos connectedPos;
    public @Nullable String connectedPortId;
    public boolean spliced;

    protected @Nullable T cachedConnectedPort;
    protected boolean cacheValid = false;

    protected Port(String id, BlockPos pos) {
        this.id = id;
        this.pos = pos;
    }

    public abstract @Nullable T getConnectedPort(Level level);

    public void connectTo(@NotNull Port<?> port, boolean spliced) {
        connectedPos = port.pos;
        connectedPortId = port.id;
        cachedConnectedPort = null;
        cacheValid = false;
        this.spliced = spliced;
    }

    public void connectTo(@NotNull T port) {
        connectedPos = port.pos;
        connectedPortId = port.id;
        cachedConnectedPort = port;
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
