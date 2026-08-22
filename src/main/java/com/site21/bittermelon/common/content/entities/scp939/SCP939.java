package com.site21.bittermelon.common.content.entities.scp939;

import com.site21.bittermelon.common.content.entities.scp939.behavior.AttemptLure;
import com.site21.bittermelon.common.content.entities.scp939.lure.LureSystem;
import com.site21.bittermelon.common.systems.ai.base.BitterMob;
import com.site21.bittermelon.common.systems.ai.base.Need;
import com.site21.bittermelon.common.systems.ai.base.NeedInstance;
import com.site21.bittermelon.common.systems.ai.behavior.movement.SearchArea;
import com.site21.bittermelon.common.systems.ai.vibration.BitterAngerManagement;
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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.tslat.smartbrainlib.api.core.behaviour.base.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class SCP939 extends BitterMob<SCP939> {
    private static final int LISTENING_RADIUS = 16;
    private final DynamicGameEventListener<BitterVibrationListener> gameListener;
    private BitterAngerManagement angerManagement;
    private LureSystem lureSystem;

    public SCP939(EntityType entityType, Level level) {
        super(entityType, level);
        PositionSource positionSource = new EntityPositionSource(this, getEyeHeight());
        BitterVibrationListener listener = new BitterVibrationListener(positionSource, LISTENING_RADIUS, SCP939::onVibration);
        this.gameListener = new DynamicGameEventListener<>(listener);
        this.angerManagement = new BitterAngerManagement();
        this.lureSystem = new LureSystem();
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
        return List.of(
                new NearbyPlayersSensor<>()
        );
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
        return List.of(
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<>()
                                .setRadius(getRandom().nextInt(1, 10)),
                        new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))
                )
        );
    }

    @Override
    public List<? extends BehaviorControl<?>> getFightingBehaviours(SCP939 owner) {
        return super.getFightingBehaviours(owner);
    }

    public List<? extends BehaviorControl<?>> getHuntBehaviours(SCP939 ignoredOwner) {
        return List.of(
                new SearchArea<>(),
                new AttemptLure()
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

    @Override
    protected void customServerAiStep(ServerLevel level) {
        if (tickCount % 20 == 0) {
            angerManagement.tick(level);
        }
    }

    public LureSystem getLureSystem() {
        return lureSystem;
    }

    @Override
    protected void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.store("anger", BitterAngerManagement.CODEC, angerManagement);
        output.store("lures", LureSystem.CODEC, lureSystem);
    }

    @Override
    protected void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        angerManagement = input.read("anger", BitterAngerManagement.CODEC).orElse(new BitterAngerManagement());
        lureSystem = input.read("lures", LureSystem.CODEC).orElse(new LureSystem());
    }
}
