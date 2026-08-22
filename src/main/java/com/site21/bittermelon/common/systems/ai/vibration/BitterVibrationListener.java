package com.site21.bittermelon.common.systems.ai.vibration;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BitterVibrationListener implements GameEventListener {
    private final PositionSource listenerSource;
    private final int listenerRadius;
    private final VibrationCallback callback;

    public BitterVibrationListener(PositionSource listenerSource, int listenerRadius, VibrationCallback callback) {
        this.listenerSource = listenerSource;
        this.listenerRadius = listenerRadius;
        this.callback = callback;
    }

    @Override
    public PositionSource getListenerSource() {
        return listenerSource;
    }

    @Override
    public int getListenerRadius() {
        return listenerRadius;
    }

    @Override
    public boolean handleGameEvent(ServerLevel level, Holder<GameEvent> event, GameEvent.Context context, Vec3 sourcePosition) {
        Optional<Vec3> listenerPos = listenerSource.getPosition(level);
        if (listenerPos.isEmpty()) return false;

        double distance = sourcePosition.distanceTo(listenerPos.get());
        if (distance > listenerRadius) return false;

        callback.onVibration(level, BlockPos.containing(sourcePosition), event, context.sourceEntity(), distance);
        return true;
    }

    @FunctionalInterface
    public interface VibrationCallback {
        void onVibration(ServerLevel level, BlockPos sourcePos, Holder<GameEvent> event, @Nullable Object sourceEntity, double distance);
    }
}