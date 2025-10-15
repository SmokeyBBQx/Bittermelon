package com.site21.bittermelon.systems.electronics.wiring;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
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

    public void disconnect(Level level) {
        T other = getConnectedPort(level);
        if (other != null) {
            other.disconnect();
            if (level.getBlockEntity(connectedPos) instanceof BlockEntity be) {
                be.setChanged();
            }
        }
        connectedPos = null;
        connectedPortId = null;
        cachedConnectedPort = null;
        cacheValid = false;
    }

    public void invalidateCache() {
        cacheValid = false;
        cachedConnectedPort = null;
    }

    public boolean isConnected() {
        return connectedPos != null && connectedPortId != null;
    }
}
