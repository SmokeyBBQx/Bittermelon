package com.site21.bittermelon.common.content.entities.scp548;

import com.site21.bittermelon.common.content.entities.scp548.behavior.FindOrMakeBurrow;
import com.site21.bittermelon.common.systems.ai.base.BitterMob;
import com.site21.bittermelon.common.systems.ai.base.Need;
import com.site21.bittermelon.common.systems.ai.base.NeedInstance;
import com.site21.bittermelon.common.systems.character.Character;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.core.behaviour.base.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.AvoidEntity;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.navigation.SmoothWallClimberNavigation;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.NearbyBlocksSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class SCP548 extends BitterMob<SCP548> {
    public SCP548(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected Character initializeCharacter() {
        return new Character(uuid, "SCP-548");
    }

    @Override
    protected Map<Need, NeedInstance> initializeNeeds() {
        return Map.of();
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new SmoothWallClimberNavigation( this, level);
    }

    @Override
    public List<? extends ExtendedSensor<?>> getSensors(SCP548 owner) {
        return List.of(
                new NearbyLivingEntitySensor<>(),
                new NearbyBlocksSensor<SCP548>().detectionRadius(4, 1)
        );
    }

    @Override
    public List<? extends BehaviorControl<?>> getAlwaysRunningBehaviours(SCP548 owner) {
        return List.of(
                new LookAtTarget<>(),
                new MoveToWalkTarget<>(),
                new AvoidEntity<>().avoiding(LivingEntity.class)
        );
    }

    @Override
    public List<? extends BehaviorControl<?>> getIdleBehaviours(SCP548 owner) {
        return List.of(
                new OneRandomBehaviour<>(
                        new SetRandomLookTarget<>(),
                        new SetPlayerLookTarget<>()
                ),
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<>()
                                .setRadius(getRandom().nextInt(1, 10)),
                        new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60)),
                        new FindOrMakeBurrow()
                )
        );
    }
}
