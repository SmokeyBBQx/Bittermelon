package com.site21.bittermelon.systems.syncsound;

import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;

public interface ISyncSoundListener {
    void onSyncSound(SyncSoundEvent event);
    default boolean canHearSound(@NotNull SyncSoundEvent event, @NotNull BlockPos listenerPos, int listeningRadius) {
        double distance = Math.sqrt(listenerPos.distSqr(event.getSource()));
        return distance <= event.getRadius() + listeningRadius;
    }
}
