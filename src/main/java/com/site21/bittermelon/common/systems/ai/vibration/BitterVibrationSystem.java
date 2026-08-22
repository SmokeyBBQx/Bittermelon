package com.site21.bittermelon.common.systems.ai.vibration;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;

public interface BitterVibrationSystem {
    public static class Listener implements GameEventListener {

        @Override
        public PositionSource getListenerSource() {
            return null;
        }

        @Override
        public int getListenerRadius() {
            return 0;
        }

        @Override
        public boolean handleGameEvent(ServerLevel level, Holder<GameEvent> event, GameEvent.Context context, Vec3 sourcePosition) {
            return false;
        }
    }
}