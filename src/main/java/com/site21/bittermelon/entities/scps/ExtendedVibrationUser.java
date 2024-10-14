package com.site21.bittermelon.entities.scps;

import com.site21.bittermelon.entities.scps.SCP939.SCP939;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import org.jetbrains.annotations.Nullable;

public class ExtendedVibrationUser implements VibrationSystem.User {
    private final PositionSource positionSource;
    private final PathfinderMob entity;

    public ExtendedVibrationUser(PathfinderMob entity) {
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
    public boolean canReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> gameEvent, GameEvent.Context context) {
        return !entity.isDeadOrDying() && entity.level().getWorldBorder().isWithinBounds(
                pos) && !entity.isRemoved();
//        var sourceEntity = context.sourceEntity();
//        return !(sourceEntity instanceof LivingEntity) || entity.canTargetEntity(sourceEntity);
    }

    @Override
    public void onReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> gameEvent, @Nullable Entity vibrationEntity, @Nullable Entity playerEntity, float distance) {
        if (this.entity.isDeadOrDying()) return;
        if (this.entity instanceof SCP939 scp939) {
            SCP939.setDisturbanceLocation(pos, scp939);
        }
    }
}
