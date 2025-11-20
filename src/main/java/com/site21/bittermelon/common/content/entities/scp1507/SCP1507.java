package com.site21.bittermelon.common.content.entities.scp1507;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.common.systems.ai.behavior.attack.CollectivePush;
import com.site21.bittermelon.common.systems.ai.behavior.herd.VerifyOrFindLeader;
import com.site21.bittermelon.common.systems.ai.behavior.target.InvalidateAttackTarget;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.ai.base.BitterMob;
import com.site21.bittermelon.common.systems.ai.base.Need;
import com.site21.bittermelon.common.systems.ai.base.NeedInstance;
import com.site21.bittermelon.common.systems.medical.factory.Anatomy;
import com.site21.bittermelon.init.neoforge.BitterActivity;
import com.site21.bittermelon.init.neoforge.BitterMemoryTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.AllApplicableBehaviours;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.LeapAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowEntity;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.util.BrainUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SCP1507 extends BitterMob<SCP1507> implements SmartBrainOwner<SCP1507> {
    private static final EntityDataAccessor<Integer> ATTACK_TIME = SynchedEntityData.defineId(SCP1507.class, EntityDataSerializers.INT);

    public SCP1507(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected Character initializeCharacter() {
        return new Character(uuid, "SCP-1507-" + getRandom().nextInt(1, 24), Anatomy.HUMAN);
    }

    @Override
    protected Map<Need, NeedInstance> initializeNeeds() {
        return Map.of(
                Need.SOCIALIZATION, new NeedInstance(0.001f, value -> Math.pow(value, 1.2), BitterActivity.SOCIALIZE.get()),
//                Need.MOVEMENT, new NeedInstance(0.001f, value -> Math.pow(value, 1.5), BitterActivity.EXPLORE.get()),
                Need.STRESS, new NeedInstance(-0.0005f, value -> Math.pow(value, 0.8), BitterActivity.MENTAL_BREAK.get()),
                Need.ANGER, new NeedInstance(0f, value -> Math.pow(value, 2.0), Activity.FIGHT)
        );
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20).add(Attributes.MOVEMENT_SPEED, 0.23f).add(Attributes.ATTACK_DAMAGE, 4.0f);
    }

    @Override
    public List<? extends ExtendedSensor<? extends SCP1507>> getSensors() {
        return List.of(
                new NearbyLivingEntitySensor<>(),
                new HurtBySensor<>()
        );
    }

    public Map<Activity, BrainActivityGroup<? extends SCP1507>> getAdditionalTasks() {
        Map<Activity, BrainActivityGroup<? extends SCP1507>> tasks = new HashMap<>();
        tasks.put(BitterActivity.EXPLORE.get(), getExploreTasks());
        return tasks;
    }

    @Override
    public BrainActivityGroup<? extends SCP1507> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new LookAtTarget<>(),
                new MoveToWalkTarget<>(),
                new TargetOrRetaliate<>()
                        .attackablePredicate(target -> !(target instanceof SCP1507))
                        .alertAlliesWhen((owner, attacker) -> true)
                        .whenStarting((entity) -> BrainUtil.setForgettableMemory(entity, BitterMemoryTypes.ACTIVE.get(), true, 240))
        );
    }

    @Override
    public BrainActivityGroup<? extends SCP1507> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new VerifyOrFindLeader<>(),
                new OneRandomBehaviour<>(
                        new FirstApplicableBehaviour<>(
                                new FollowEntity<>()
                                        .following((entity) -> BrainUtil.getMemory(entity, BitterMemoryTypes.LEADER.get()))
                                        .stopFollowingWithin(5),
                                new SetRandomWalkTarget<>()
                        ).startCondition((entity) -> Boolean.TRUE.equals(BrainUtil.getMemory(entity, BitterMemoryTypes.ACTIVE.get()))),
                        new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))
                )
        );
    }

    @Override
    public BrainActivityGroup<? extends SCP1507> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>(),
                new SetWalkTargetToAttackTarget<>(),
                new OneRandomBehaviour<>(
                        Pair.of(new FirstApplicableBehaviour<>(
                                new LeapAtTarget<SCP1507>(0)
                                        .whenStarting(SCP1507::resetAttackTime)
                                        .startCondition((entity) -> BrainUtil.getTargetOfEntity(entity).distanceTo( entity) < 4)
                        ), 10),
                        Pair.of(new CollectivePush<>(10, 5.0, 0), 1)
                )
        );
    }

    public BrainActivityGroup<? extends SCP1507> getExploreTasks() {
        return new BrainActivityGroup<SCP1507>(BitterActivity.EXPLORE.get()).behaviours(
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<>()
                                .setRadius(getRandom().nextInt(5, 15)),
                        new Idle<>().runFor(entity -> 30)
                ).whenStarting(entity -> {
                    if (entity instanceof SCP1507 scp1507) {
                        scp1507.modifyNeed(Need.MOVEMENT, -10.0f);
                    }
                })
        );
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        int currentAttackTime = getAttackTime();
        if (currentAttackTime > 0) {
            entityData.set(ATTACK_TIME, currentAttackTime - 1);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ATTACK_TIME, 0);
    }

    public int getAttackTime() {
        return entityData.get(ATTACK_TIME);
    }

    public static void resetAttackTime(@NotNull SCP1507 entity) {
        entity.entityData.set(ATTACK_TIME, 20);
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        if (tickCount <= 1) {
            makeDeathParticles();
        }
    }

    private void makeDeathParticles() {
        if (!(level() instanceof ServerLevel level)) return;
        level.sendParticles(
                ParticleTypes.CHERRY_LEAVES,
                getX(),
                getY(),
                getZ(),
                30,
                0.4,
                0.5,
                0.4,
                5
        );
    }

    @Override
    public void onDamageTaken(@NotNull DamageContainer damageContainer) {
        super.onDamageTaken(damageContainer);
        makeDeathParticles();
    }
}
