package com.site21.bittermelon.common.systems.ai.vibration;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.GameEventTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;

public class BitterVibrationListener<E extends Entity> implements GameEventListener {
    private final PositionSource listenerSource;
    private final int listenerRadius;
    private final VibrationCallback<E> callback;
    private final Predicate<E> ignoreDampening;
    private final E entity;

    public BitterVibrationListener(PositionSource listenerSource, int listenerRadius, VibrationCallback<E> callback, Predicate<E> ignoreDampening, E entity) {
        this.listenerSource = listenerSource;
        this.listenerRadius = listenerRadius;
        this.callback = callback;
        this.ignoreDampening = ignoreDampening;
        this.entity = entity;
    }

    public BitterVibrationListener(PositionSource listenerSource, int listenerRadius, VibrationCallback<E> callback, E entity) {
        this(listenerSource, listenerRadius, callback, _ -> false, entity);
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
        if (!isValidVibration(event, context)) return false;

        Optional<Vec3> listenerPos = listenerSource.getPosition(level);
        if (listenerPos.isEmpty()) return false;

        double distance = sourcePosition.distanceTo(listenerPos.get());
        if (distance > listenerRadius) return false;

        callback.onVibration(entity, level, BlockPos.containing(sourcePosition), event, context.sourceEntity(), distance);
        return true;
    }

    protected boolean isValidVibration(Holder<GameEvent> event, GameEvent.Context context) {
        if (!event.is(GameEventTags.VIBRATIONS)) return false;

        boolean ignoresDampening = ignoreDampening.test(entity);

        Entity sourceEntity = context.sourceEntity();
        if (sourceEntity != null) {
            if (sourceEntity.isSpectator() || (sourceEntity instanceof Player player && player.isCreative())) return false;
            return ignoresDampening || !sourceEntity.dampensVibrations();
        }

        return ignoresDampening
                || context.affectedState() == null
                || !context.affectedState().is(BlockTags.DAMPENS_VIBRATIONS);
    }

    @FunctionalInterface
    public interface VibrationCallback<E extends Entity> {
        void onVibration(E entity, ServerLevel level, BlockPos sourcePos, Holder<GameEvent> event, @Nullable Entity sourceEntity, double distance);
    }
}