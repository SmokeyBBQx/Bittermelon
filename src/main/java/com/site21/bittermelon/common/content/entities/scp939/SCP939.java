package com.site21.bittermelon.common.content.entities.scp939;

import com.site21.bittermelon.common.systems.ai.base.BitterMob;
import com.site21.bittermelon.common.systems.ai.base.Need;
import com.site21.bittermelon.common.systems.ai.base.NeedInstance;
import com.site21.bittermelon.common.systems.ai.vibration.BitterVibrationListener;
import com.site21.bittermelon.common.systems.character.Character;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSource;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class SCP939 extends BitterMob<SCP939> {
    private static final int LISTENING_RADIUS = 16;
    private final DynamicGameEventListener<BitterVibrationListener> gameListener;

    public SCP939(EntityType entityType, Level level) {
        super(entityType, level);
        PositionSource positionSource = new EntityPositionSource(this, getEyeHeight());
        BitterVibrationListener listener = new BitterVibrationListener(positionSource, LISTENING_RADIUS, SCP939::onVibration);
        this.gameListener = new DynamicGameEventListener<>(listener);
    }

    @Override
    protected Character initializeCharacter() {
        return new Character(uuid, "SCP-939-" + getRandom().nextInt(1, 24));
    }

    @Override
    protected Map<Need, NeedInstance> initializeNeeds() {
        return Map.of();
    }

    @Override
    public List<? extends ExtendedSensor<?>> getSensors(SCP939 owner) {
        return List.of();
    }

    @Override
    public List<? extends BehaviorControl<?>> getAlwaysRunningBehaviours(SCP939 owner) {
        return List.of(
                new LookAtTarget<>(),
                new MoveToWalkTarget<>()
        );
    }

    @Override
    public List<? extends BehaviorControl<?>> getIdleBehaviours(SCP939 owner) {
        return super.getIdleBehaviours(owner);
    }

    @Override
    public List<? extends BehaviorControl<?>> getFightingBehaviours(SCP939 owner) {
        return super.getFightingBehaviours(owner);
    }

    public List<? extends BehaviorControl<?>> getHuntBehaviours(SCP939 ignoredOwner) {
        return List.of(
        );
    }

    public List<? extends BehaviorControl<?>> getListenBehaviours(SCP939 ignoredOwner) {
        return List.of(
        );
    }

    @Override
    public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> action) {
        if (level() instanceof ServerLevel level) {
            action.accept(gameListener, level);
        }
    }

    public static void onVibration(ServerLevel level, BlockPos sourcePos, Holder<GameEvent> event, @Nullable Object sourceEntity, double distance) {

    }
}
