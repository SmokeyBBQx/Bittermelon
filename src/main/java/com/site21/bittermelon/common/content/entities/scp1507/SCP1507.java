package com.site21.bittermelon.common.content.entities.scp1507;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.common.content.entities.scp1507.behavior.StopMovingWhenLookedAt;
import com.site21.bittermelon.common.content.entities.scp1507.behavior.TryToBecomeActive;
import com.site21.bittermelon.common.systems.ai.base.BitterMob;
import com.site21.bittermelon.common.systems.ai.base.Need;
import com.site21.bittermelon.common.systems.ai.base.NeedInstance;
import com.site21.bittermelon.common.systems.ai.behavior.attack.CollectivePush;
import com.site21.bittermelon.common.systems.ai.behavior.blockinteraction.InvalidateBreakTarget;
import com.site21.bittermelon.common.systems.ai.behavior.herd.VerifyOrFindLeader;
import com.site21.bittermelon.common.systems.ai.behavior.blockinteraction.FindBlockingBlock;
import com.site21.bittermelon.common.systems.ai.behavior.blockinteraction.LeapAndHurtBlock;
import com.site21.bittermelon.common.systems.ai.behavior.blockinteraction.FindRandomBreakTarget;
import com.site21.bittermelon.common.systems.ai.behavior.target.InvalidateAttackTarget;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.medical.factory.Anatomy;
import com.site21.bittermelon.init.neoforge.BitterActivity;
import com.site21.bittermelon.init.neoforge.BitterMemoryTypes;
import com.site21.bittermelon.init.neoforge.BitterParticles;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.LeapAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.ReactToUnreachableTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowEntity;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.navigation.SmoothGroundNavigation;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.NearbyBlocksSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.util.BrainUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

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
                Need.ANGER, new NeedInstance(0f, value -> Math.pow(value, 2.0), Activity.FIGHT)
        );
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 5)
                .add(Attributes.MOVEMENT_SPEED, 0.23f)
                .add(Attributes.ATTACK_DAMAGE, 4.0f);
    }

    @Override
    public List<? extends ExtendedSensor<? extends SCP1507>> getSensors() {
        return List.of(
                new NearbyLivingEntitySensor<>(),
                new HurtBySensor<>(),
                new NearbyBlocksSensor<SCP1507>().setRadius(10, 2)
        );
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new SmoothGroundNavigation( this, level);
    }

    @Override
    public BrainActivityGroup<? extends SCP1507> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new LookAtTarget<>(),
                new MoveToWalkTarget<>(),
                new InvalidateBreakTarget<>(),
                new ReactToUnreachableTarget<>()
                        .reaction((entity, towering) -> new FindBlockingBlock<>()),
                new LeapAndHurtBlock<SCP1507>(0)
                        .whenStarting(SCP1507::resetAttackTime)
                        .startCondition((entity) ->
                                BrainUtil.getMemory(entity, BitterMemoryTypes.BREAK_TARGET.get()).distSqr(entity.getOnPos()) <= 4),
                new TargetOrRetaliate<>()
                        .attackablePredicate(target -> !(target instanceof SCP1507))
                        .alertAlliesWhen((owner, attacker) -> true)
        );
    }

    @Override
    public BrainActivityGroup<? extends SCP1507> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new VerifyOrFindLeader<>(),
                new TryToBecomeActive().cooldownForBetween(600, 1200),
                new StopMovingWhenLookedAt(),
                new FindRandomBreakTarget<>()
                        .cooldownForBetween(60, 120),
                new OneRandomBehaviour<>(
                        new FirstApplicableBehaviour<>(
                                new FollowEntity<>()
                                        .following((entity) -> BrainUtil.getMemory(entity, BitterMemoryTypes.LEADER.get()))
                                        .stopFollowingWithin((entity, target) -> getFollowDistance(entity)),
                                new SetRandomWalkTarget<>().startCondition((SCP1507::isActive))
                        ),
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
                        Pair.of(new LeapAtTarget<SCP1507>(0)
                                        .whenStarting(SCP1507::resetAttackTime)
                                        .startCondition((entity) ->
                                                BrainUtil.getTargetOfEntity(entity).distanceTo(entity) < 4),
                                10),
                        Pair.of(new CollectivePush<>(10, 5.0, 0), 1)
                ).whenStopping(SCP1507::setActive)
        );
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
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
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
            makeShatterParticles(30);
        }
    }

    @Override
    public void onDamageTaken(@NotNull DamageContainer damageContainer) {
        super.onDamageTaken(damageContainer);
        makeShatterParticles(Math.min(100, (int) (damageContainer.getNewDamage() * 5)));
    }

    private void makeShatterParticles(int count) {
        if (!(level() instanceof ServerLevel level)) return;
        level.sendParticles(
                ColorParticleOption.create(BitterParticles.PLASTIC.get(), 0xFFF8748D),
                getX(),
                getY() + 0.4,
                getZ(),
                count,
                0.2,
                0.1,
                0.2,
                0.2
        );
    }

//    private static int getBlockBreakTime(@NotNull SCP1507 entity) {
//        return entity.getMood() >=
//    }

    private static float getFollowDistance(LivingEntity entity) {
        return isActive(entity) ? 4.0f : (float) entity.getAttributeValue(Attributes.FOLLOW_RANGE) / 1.5f;
    }

    public static boolean isActive(LivingEntity entity) {
        return Boolean.TRUE.equals(BrainUtil.getMemory(entity, BitterMemoryTypes.ACTIVE.get()));
    }

    public static void setActive(LivingEntity entity) {
        BrainUtil.setForgettableMemory(entity, BitterMemoryTypes.ACTIVE.get(), true, 1200);
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.ZOMBIE_ATTACK_WOODEN_DOOR;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR;
    }

    @Override
    public float getVoicePitch() {
        return random.nextFloat() * 0.2f + 1.5f;
    }

    @Override
    protected float getSoundVolume() {
        return 0.2f;
    }
}
