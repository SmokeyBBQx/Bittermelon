package com.site21.bittermelon.content.entities.ai.vibration;

import com.site21.bittermelon.content.entities.implementations.scp939.SCP939;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSource;
import net.tslat.smartbrainlib.util.BrainUtil;
import org.jetbrains.annotations.Nullable;

public class BitterVibrationUser implements BitterVibrationSystem.User {
    private final PositionSource positionSource;
    private final PathfinderMob entity;

    public BitterVibrationUser(PathfinderMob entity) {
        this.positionSource = new EntityPositionSource(entity, entity.getEyeHeight());
        this.entity = entity;
    }

    @Override
    public int getListenerRadius() {
        return 32;
    }

    @Override
    public PositionSource getPositionSource() {
        return this.positionSource;
    }
    @Override
    public boolean canTriggerAvoidVibration() {
        return true;
    }

    @Override
    public boolean canReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> gameEvent, GameEvent.Context context) {
        return !entity.isDeadOrDying() && entity.level().getWorldBorder().isWithinBounds(
                pos) && !entity.isRemoved();
    }

    @Override
    public void onReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> gameEvent, @Nullable Entity vibrationEntity, @Nullable Entity playerEntity, float distance) {
        if (this.entity.isDeadOrDying()) return;
        if (this.entity.isVehicle()) return;
        if (this.entity instanceof SCP939 scp939) {
            if (vibrationEntity != null) {
                if (this.entity.closerThan(vibrationEntity, 30)) {
                    scp939.increaseAngerAt(vibrationEntity);
                }
            }
            if (!scp939.getAngerLevel().isAngry() && BrainUtil.getTargetOfEntity(scp939) == null) {
                SCP939.setDisturbanceLocation(pos, scp939);
            }
        }
    }
}
