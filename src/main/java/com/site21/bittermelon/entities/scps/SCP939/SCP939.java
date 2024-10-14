package com.site21.bittermelon.entities.scps.SCP939;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.character.CharacterManager;
import com.site21.bittermelon.entities.behavior.needs.Need;
import com.site21.bittermelon.entities.behavior.needs.ReevaluateDecision;
import com.site21.bittermelon.entities.behavior.path.SeekNearestPlayer;
import com.site21.bittermelon.entities.behavior.social.GenericSocialize;
import com.site21.bittermelon.entities.behavior.social.Socializable;
import com.site21.bittermelon.entities.behavior.needs.NeedsUser;
import com.site21.bittermelon.entities.scps.ExtendedVibrationUser;
import com.site21.bittermelon.entities.scps.SCP939.behavior.*;
import com.site21.bittermelon.init.ActivityInit;
import com.site21.bittermelon.init.MemoryModuleTypeInit;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.AllApplicableBehaviours;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.LeapAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@SuppressWarnings("unchecked")
public class SCP939 extends PathfinderMob implements NeedsUser, Socializable, VibrationSystem, SmartBrainOwner<SCP939> {
    private static final EntityDataAccessor<Float> BLOODLUST = SynchedEntityData.defineId(SCP939.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> SOCIALIZATION = SynchedEntityData.defineId(SCP939.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> PROCREATION = SynchedEntityData.defineId(SCP939.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> AMNESTICS = SynchedEntityData.defineId(SCP939.class, EntityDataSerializers.FLOAT);

    private float suspicion = 0;
    private final int BASE_LURE_COOLDOWN = 200;
    private final int BASE_LISTEN_COOLDOWN = 300;
    private final int BASE_SEARCH_RANGE = 32;
    private final int BASE_MIN_IDLE_TIME = 20;
    private final int BASE_MAX_IDLE_TIME = 40;
    private final float BLOODLUST_DECAY = -0.001f;
    private final float SOCIALIZATION_DECAY = -0.001f;
    private final float PROCREATION_DECAY = -0.0001f;

    private final DynamicGameEventListener<Listener> dynamicGameEventListener;
    private VibrationSystem.Data vibrationData;
    private VibrationSystem.User vibrationUser;

    public SCP939(EntityType<? extends Mob> entityType, Level level) {
        super((EntityType<? extends Monster>) entityType, level);
        this.vibrationUser = new ExtendedVibrationUser(this);
        this.vibrationData = new VibrationSystem.Data();
        this.dynamicGameEventListener = new DynamicGameEventListener<>(new VibrationSystem.Listener(this));
        this.xpReward = 5;
        this.getNavigation().setCanFloat(true);
        this.setPathfindingMalus(PathType.UNPASSABLE_RAIL, 0.0F);
        this.setPathfindingMalus(PathType.DAMAGE_OTHER, 8.0F);
        this.setPathfindingMalus(PathType.POWDER_SNOW, 8.0F);
        this.setPathfindingMalus(PathType.LAVA, 8.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, 0.0F);
        this.setPathfindingMalus(PathType.DANGER_FIRE, 0.0F);

        Character character = new Character(this.uuid, "SCP-939");
        CharacterManager.getInstance().addCharacter(character);
        CharacterManager.getInstance().setActiveCharacter(this.uuid, character);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 150.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5)
                .add(Attributes.ATTACK_DAMAGE, 30.0);
    }

    @Override
    public void updateDynamicGameEventListener(@NotNull BiConsumer<DynamicGameEventListener<?>, ServerLevel> listenerConsumer) {
        if (this.level() instanceof ServerLevel serverlevel) {
            listenerConsumer.accept(this.dynamicGameEventListener, serverlevel);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(BLOODLUST, 100.0f);
        builder.define(SOCIALIZATION, 100.0f);
        builder.define(PROCREATION, 100.0f);
        builder.define(AMNESTICS, 100.0f);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.modifyBloodlust(compound.getFloat("bloodlust"));
        this.modifySocialization(compound.getFloat("socialization"));
        this.modifyProcreation(compound.getFloat("procreation"));
        this.modifyAmnestics(compound.getFloat("amnestics"));
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("bloodlust", getBloodlust());
        compound.putFloat("socialization", getSocialization());
        compound.putFloat("procreation", getProcreation());
        compound.putFloat("amnestics", getAmnestics());
    }

    public void modifyBloodlust(float amount) {
        this.entityData.set(BLOODLUST, Math.min(100, Math.max(0, getBloodlust() + amount)));
    }

    public void modifySocialization(float amount) {
        this.entityData.set(SOCIALIZATION, Math.min(100, Math.max(0, getSocialization() + amount)));
    }

    public void modifyProcreation(float amount) {
        this.entityData.set(PROCREATION, Math.min(100, Math.max(0, getProcreation() + amount)));
    }

    public void modifyAmnestics(float amount) {
        this.entityData.set(AMNESTICS, Math.min(100, Math.max(0, getAmnestics() + amount)));
    }

    public void setBloodlust(float amount) {
        this.entityData.set(BLOODLUST, amount);
    }

    public void setSocialization(float amount) {
        this.entityData.set(SOCIALIZATION, amount);
    }

    public void setProcreation(float amount) {
        this.entityData.set(PROCREATION, amount);
    }

    public void setAmnestics(float amount) {
        this.entityData.set(AMNESTICS, amount);
    }

    public float getBloodlust() {
        return this.entityData.get(BLOODLUST);
    }

    public float getSocialization() {
        return this.entityData.get(SOCIALIZATION);
    }

    public float getProcreation() {
        return this.entityData.get(PROCREATION);
    }

    public float getAmnestics() {
        return this.entityData.get(AMNESTICS);
    }

    @javax.annotation.Nullable
    @Override
    public LivingEntity getTarget() {
        return this.getTargetFromBrain();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Contract("null->false")
    public boolean canTargetEntity(LivingEntity entity) {
        return entity instanceof LivingEntity livingentity
                && this.level() == entity.level()
                && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity)
                && !this.isAlliedTo(entity)
                && livingentity.getType() != EntityType.ARMOR_STAND
                && !livingentity.isInvulnerable()
                && !livingentity.isDeadOrDying()
                && this.level().getWorldBorder().isWithinBounds(livingentity.getBoundingBox());
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        boolean flag = super.hurt(source, amount);
        if (!this.level().isClientSide && !this.isNoAi()) {
            Entity entity = source.getEntity();
            if (this.brain.getMemory(MemoryModuleType.ATTACK_TARGET).isEmpty()
                    && entity instanceof LivingEntity livingEntity
                    && (source.isDirect() || this.closerThan(livingEntity, 5.0))) {
                this.setAttackTarget(livingEntity);
            }
        }

        return flag;
    }

    public void setAttackTarget(LivingEntity attackTarget) {
        this.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, attackTarget);
        this.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level() instanceof ServerLevel serverlevel) {
            VibrationSystem.Ticker.tick(serverlevel, this.vibrationData, this.vibrationUser);
        }
        this.modifySuspicion(-0.001f);

        modifyBloodlust(BLOODLUST_DECAY);
        modifySocialization(SOCIALIZATION_DECAY);
        modifyProcreation(PROCREATION_DECAY);
    }

    @Override
    protected void customServerAiStep() {
        tickBrain(this);
    }

    @Override
    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendEntityBrain(this);
    }


    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new GroundPathNavigation(this, level) {
            @Override
            protected @NotNull PathFinder createPathFinder(int i) {
                this.nodeEvaluator = new WalkNodeEvaluator();
                this.nodeEvaluator.setCanPassDoors(true);
                return new PathFinder(this.nodeEvaluator, i) {
                    @Override
                    protected float distance(@NotNull Node entity1, @NotNull Node entity2) {
                        return entity1.distanceToXZ(entity2);
                    }
                };
            }
        };
    }

    @Override
    protected Brain.@NotNull Provider<SCP939> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    public List<? extends ExtendedSensor<? extends SCP939>> getSensors() {
        return List.of(
                new NearbyPlayersSensor<SCP939>().setRadius(1000),
                new NearbyLivingEntitySensor<>(),
                new HurtBySensor<>()
        );
    }

    @Override
    public List<Activity> getActivityPriorities() {
        return ObjectArrayList.of(Activity.FIGHT);
    }

    @Override
    public Map<Activity, BrainActivityGroup<? extends SCP939>> getAdditionalTasks() {
        return Map.of(
                Activity.INVESTIGATE, getInvestigationTasks(),
                ActivityInit.HUNT.get(), getHuntTasks(),
                ActivityInit.PROCREATE.get(), getProcreateTasks(),
                Activity.REST, getRestTasks(),
                ActivityInit.SOCIALIZE.get(), getSocializeTasks()
        );
    }

    @Override
    public BrainActivityGroup<? extends SCP939> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
//                new AvoidSun<>(),
//                new EscapeSun<>().cooldownFor(entity -> 20),
//                TODO: new FleeFireTask<>
                new LookAtTarget<>(),
                new MoveToWalkTarget<>(),
                new ReevaluateDecision<>()
        );
    }

    @Override
    public BrainActivityGroup<? extends SCP939> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new FirstApplicableBehaviour<>(
                        new TargetOrRetaliate<>(),
                        new SetRandomLookTarget<>()
                ),
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<>(),
                        new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))
                )
        );
    }

    @Override
    public BrainActivityGroup<? extends SCP939> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>(),
                new SetWalkTargetToAttackTarget<>().speedMod((entity, target) -> 1.5f).stopIf(entity -> this.isDeadOrDying()),
                new AnimatableMeleeAttack<>(0),
                new LeapAtTarget<>(20)
        );
    }

    public BrainActivityGroup<? extends SCP939> getHuntTasks() {
        return new BrainActivityGroup<SCP939>(ActivityInit.HUNT.get()).behaviours(
                new SeekNearestPlayer<>()
                        .cooldownFor(entity -> 120),
                new OneRandomBehaviour<>(
                        new Lure<>(20)
                                .cooldownFor(entity -> getLureCooldown()),
                        new Amnesticize<>(60)
                                .cooldownFor(entity -> 200),
                        new Listen<>(100)
                                .cooldownFor(entity -> getListenCooldown())

                        // TODO: Cooldown as memory type?
                )
                        .startCondition(entity -> !BrainUtils.hasMemory(entity, MemoryModuleTypeInit.ACTION_COOLDOWN.get()))
                        .cooldownFor(entity -> 150)
                        .whenStopping(entity -> BrainUtils.setForgettableMemory(entity, MemoryModuleTypeInit.ACTION_COOLDOWN.get(), true, 150)),
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<>(),
                        new Idle<>().runFor(entity -> getIdleTime())
                )
        );
    }

    public BrainActivityGroup<? extends SCP939> getInvestigationTasks() {
        return new BrainActivityGroup<SCP939>(Activity.INVESTIGATE).requireAndWipeMemoriesOnUse(
                        MemoryModuleType.DISTURBANCE_LOCATION
                )
                .behaviours(
                        new FirstApplicableBehaviour<>(
                                new InvestigateTarget<>()
                                        .cooldownFor(entity -> 20),
                                new AllApplicableBehaviours<>(
                                        new MoveToWalkTarget<>(),
                                        new Idle<>().runFor(entity -> getIdleTime())
                                )
                        )
                );
    }

    public BrainActivityGroup<? extends SCP939> getProcreateTasks() {
        return new BrainActivityGroup<SCP939>(ActivityInit.PROCREATE.get()).behaviours(
                new Procreate<>(20)
                );
    }

    public BrainActivityGroup<? extends SCP939> getRestTasks() {
        return new BrainActivityGroup<SCP939>(Activity.REST).behaviours(
                new ReplenishAmnestics<>(100)
                );
    }

    public BrainActivityGroup<? extends SCP939> getSocializeTasks() {
        return new BrainActivityGroup<SCP939>(ActivityInit.SOCIALIZE.get()).behaviours(
                new GenericSocialize<>()
                        .closeEnoughDist((entity, partner) -> 5)
                        .messages(List.of(
                                        " flickers its bioluminescent spine lights at ",
                                        " makes a high-pitched tone towards "
                                )
                        )
        );
    }

    private int getLureCooldown() {
        return (int) (BASE_LURE_COOLDOWN * (1 + this.getSuspicion()));
    }

    private int getListenCooldown() {
        return (int) (BASE_LISTEN_COOLDOWN * (1 - this.getSuspicion() * 0.5f));
    }

    private double getSearchRange() {
        return (BASE_SEARCH_RANGE * (1 - this.getSuspicion() * 0.5f));
    }

    private int getIdleTime() {
        float idleAmplifier = 1 + this.getSuspicion();
        return (int) (this.getRandom().nextInt(BASE_MIN_IDLE_TIME, BASE_MAX_IDLE_TIME) * idleAmplifier);
    }

    public float getSuspicion() {
        return this.suspicion;
    }

    public void modifySuspicion(float amount) {
        this.suspicion = Math.min(1, Math.max(0, this.getSuspicion() + amount));
    }

    @Override
    public boolean dampensVibrations() {
        return true;
    }

    @Override
    public @NotNull Data getVibrationData() {
        return this.vibrationData;
    }

    @Override
    public @NotNull User getVibrationUser() {
        return this.vibrationUser;
    }

    public static void setDisturbanceLocation(BlockPos pos, SCP939 entity) {
        BrainUtils.setForgettableMemory(entity, MemoryModuleType.DISTURBANCE_LOCATION, pos, 600);
        BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(pos, 1, 1));
        entity.modifySuspicion(0.1f);
        System.out.println("Current suspicion" + entity.getSuspicion());
    }

    public Component getRandomLureLine() {
        return Component.literal("HELP");
    }

    @Override
    public List<Need> getNeeds() {
        return List.of(
                new Need(BLOODLUST, ActivityInit.HUNT.get(), value -> (float) Math.pow(100 - value, 1.2)),
                new Need(SOCIALIZATION, ActivityInit.SOCIALIZE.get(), value -> (float) Math.pow(100 - value, 1.5)),
                new Need(PROCREATION, ActivityInit.PROCREATE.get(), value -> 100 - value),
                new Need(AMNESTICS, Activity.REST, value -> (float) Math.pow(100 - value, 0.2))
        );
    }
}
