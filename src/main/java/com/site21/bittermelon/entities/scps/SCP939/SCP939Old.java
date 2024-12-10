//package com.site21.bittermelon.entities.scps.SCP939;
//
//import com.mojang.logging.LogUtils;
//import com.mojang.serialization.Dynamic;
//import com.site21.bittermelon.character.Character;
//import com.site21.bittermelon.character.CharacterManager;
//import com.site21.bittermelon.entities.BitterVibrationSystem;
//import com.site21.bittermelon.entities.behavior.mood.mentalbreak.MurderousRage;
//import com.site21.bittermelon.entities.behavior.mood.mentalbreak.WarnHighStress;
//import com.site21.bittermelon.entities.behavior.needs.Need;
//import com.site21.bittermelon.entities.behavior.needs.ReevaluateDecision;
//import com.site21.bittermelon.entities.behavior.social.Relationship;
//import com.site21.bittermelon.entities.behavior.social.interactions.GenericInteraction;
//import com.site21.bittermelon.entities.behavior.social.Socializable;
//import com.site21.bittermelon.entities.behavior.needs.NeedsUser;
//import com.site21.bittermelon.entities.scps.BitterVibrationUser;
//import com.site21.bittermelon.entities.scps.SCP939.behavior.*;
//import com.site21.bittermelon.init.BitterActivity;
//import com.site21.bittermelon.init.BitterMemoryModuleType;
//import it.unimi.dsi.fastutil.objects.ObjectArrayList;
//import net.minecraft.core.BlockPos;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.NbtOps;
//import net.minecraft.network.chat.Component;
//import net.minecraft.network.protocol.game.DebugPackets;
//import net.minecraft.network.syncher.EntityDataAccessor;
//import net.minecraft.network.syncher.EntityDataSerializers;
//import net.minecraft.network.syncher.SynchedEntityData;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.util.Unit;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.entity.*;
//import net.minecraft.world.entity.ai.Brain;
//import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
//import net.minecraft.world.entity.ai.attributes.Attributes;
//import net.minecraft.world.entity.ai.memory.MemoryModuleType;
//import net.minecraft.world.entity.ai.memory.WalkTarget;
//import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
//import net.minecraft.world.entity.ai.navigation.PathNavigation;
//import net.minecraft.world.entity.ai.targeting.TargetingConditions;
//import net.minecraft.world.entity.monster.Monster;
//import net.minecraft.world.entity.monster.warden.AngerLevel;
//import net.minecraft.world.entity.monster.warden.AngerManagement;
//import net.minecraft.world.entity.monster.warden.WardenAi;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.entity.schedule.Activity;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.gameevent.DynamicGameEventListener;
//import net.minecraft.world.level.pathfinder.Node;
//import net.minecraft.world.level.pathfinder.PathFinder;
//import net.minecraft.world.level.pathfinder.PathType;
//import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
//import net.tslat.smartbrainlib.api.SmartBrainOwner;
//import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
//import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
//import net.tslat.smartbrainlib.api.core.behaviour.AllApplicableBehaviours;
//import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
//import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
//import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
//import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
//import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
//import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
//import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
//import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
//import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
//import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
//import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
//import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
//import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
//import net.tslat.smartbrainlib.util.BrainUtils;
//import org.jetbrains.annotations.Contract;
//import org.jetbrains.annotations.NotNull;
//import org.slf4j.Logger;
//
//import javax.annotation.Nullable;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.function.BiConsumer;
//
//@SuppressWarnings("unchecked")
//public class SCP939Old extends PathfinderMob implements NeedsUser<SCP939>, Socializable, BitterVibrationSystem, SmartBrainOwner<SCP939> {
//    private static final EntityDataAccessor<Float> BLOODLUST = SynchedEntityData.defineId(SCP939.class, EntityDataSerializers.FLOAT);
//    private static final EntityDataAccessor<Float> SOCIALIZATION = SynchedEntityData.defineId(SCP939.class, EntityDataSerializers.FLOAT);
//    private static final EntityDataAccessor<Float> PROCREATION = SynchedEntityData.defineId(SCP939.class, EntityDataSerializers.FLOAT);
//    private static final EntityDataAccessor<Float> REST = SynchedEntityData.defineId(SCP939.class, EntityDataSerializers.FLOAT);
//    private static final EntityDataAccessor<Float> STRESS = SynchedEntityData.defineId(SCP939.class, EntityDataSerializers.FLOAT);
//    private static final EntityDataAccessor<Integer> CLIENT_ANGER_LEVEL = SynchedEntityData.defineId(SCP939.class, EntityDataSerializers.INT);
//
//    private final Map<Character, Relationship> relationships = new HashMap<>();
//
//    private final int BASE_LURE_COOLDOWN = 200;
//    private final int BASE_LISTEN_COOLDOWN = 300;
//    private final int BASE_SEARCH_RANGE = 32;
//    private final int BASE_MIN_IDLE_TIME = 20;
//    private final int BASE_MAX_IDLE_TIME = 40;
//    private final float BLOODLUST_DECAY = -0.001f;
//    private final float SOCIALIZATION_DECAY = -0.001f;
//    private final float PROCREATION_DECAY = -0.0001f;
//    private final float STRESS_REGEN = 0.0005f;
//
//    private static final Logger LOGGER = LogUtils.getLogger();
//    private final DynamicGameEventListener<Listener> dynamicGameEventListener;
//    private BitterVibrationSystem.Data vibrationData;
//    private final BitterVibrationSystem.User vibrationUser;
//    private AngerManagement angerManagement = new AngerManagement(this::canTargetEntity, Collections.emptyList());
//
//    public SCP939Old(EntityType<? extends Mob> entityType, Level level) {
//        super((EntityType<? extends Monster>) entityType, level);
//        this.vibrationUser = new BitterVibrationUser(this);
//        this.vibrationData = new BitterVibrationSystem.Data();
//        this.dynamicGameEventListener = new DynamicGameEventListener<>(new BitterVibrationSystem.Listener(this));
//        this.xpReward = 5;
//        this.getNavigation().setCanFloat(true);
//        this.setPathfindingMalus(PathType.UNPASSABLE_RAIL, 0.0F);
//        this.setPathfindingMalus(PathType.DAMAGE_OTHER, 8.0F);
//        this.setPathfindingMalus(PathType.POWDER_SNOW, 8.0F);
//        this.setPathfindingMalus(PathType.LAVA, 8.0F);
//        this.setPathfindingMalus(PathType.DAMAGE_FIRE, 0.0F);
//        this.setPathfindingMalus(PathType.DANGER_FIRE, 0.0F);
//
//        Character character = new Character(this.uuid, "SCP-939");
//        CharacterManager.getInstance().addCharacter(character);
//        CharacterManager.getInstance().setActiveCharacter(this.uuid, character);
//    }
//
//    public static AttributeSupplier.Builder createAttributes() {
//        return Monster.createMonsterAttributes()
//                .add(Attributes.MAX_HEALTH, 150.0)
//                .add(Attributes.MOVEMENT_SPEED, 0.3F)
//                .add(Attributes.ATTACK_KNOCKBACK, 1.5)
//                .add(Attributes.ATTACK_DAMAGE, 30.0);
//    }
//
//    @Override
//    public void updateDynamicGameEventListener(@NotNull BiConsumer<DynamicGameEventListener<?>, ServerLevel> listenerConsumer) {
//        if (this.level() instanceof ServerLevel serverlevel) {
//            listenerConsumer.accept(this.dynamicGameEventListener, serverlevel);
//        }
//    }
//
//    @Override
//    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
//        super.defineSynchedData(builder);
//        builder.define(BLOODLUST, 100.0f);
//        builder.define(SOCIALIZATION, 100.0f);
//        builder.define(PROCREATION, 100.0f);
//        builder.define(REST, 100.0f);
//        builder.define(STRESS, 100.0f);
//        builder.define(CLIENT_ANGER_LEVEL, 0);
//    }
//
//    @Override
//    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
//        super.addAdditionalSaveData(compound);
//        compound.putFloat("bloodlust", getBloodlust());
//        compound.putFloat("socialization", getSocialization());
//        compound.putFloat("procreation", getProcreation());
//        compound.putFloat("rest", getRest());
//        compound.putFloat("stress", getStress());
//        Data.CODEC.encodeStart(NbtOps.INSTANCE, this.vibrationData).resultOrPartial(
//                LOGGER::error).ifPresent(tag -> compound.put("listener", tag));
//        AngerManagement.codec(this::canTargetEntity).encodeStart(NbtOps.INSTANCE, this.angerManagement).resultOrPartial(
//                LOGGER::error).ifPresent(tag -> compound.put("anger", tag));
//    }
//
//    @Override
//    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
//        super.readAdditionalSaveData(compound);
//        this.setBloodlust(compound.getFloat("bloodlust"));
//        this.setSocialization(compound.getFloat("socialization"));
//        this.setProcreation(compound.getFloat("procreation"));
//        this.setRest(compound.getFloat("rest"));
//        this.setStress(compound.getFloat("stress"));
//        if (compound.contains("anger")) {
//            AngerManagement.codec(this::canTargetEntity).parse(
//                    new Dynamic<>(NbtOps.INSTANCE, compound.get("anger"))).resultOrPartial(LOGGER::error).ifPresent(
//                    angerM -> this.angerManagement = angerM);
//            this.syncClientAngerLevel();
//        }
//        if (compound.contains("listener", 10)) Data.CODEC.parse(
//                new Dynamic<>(NbtOps.INSTANCE, compound.getCompound("listener"))).resultOrPartial(
//                LOGGER::error).ifPresent(data -> this.vibrationData = data);
//    }
//
//    public void modifyBloodlust(float amount) {
//        this.entityData.set(BLOODLUST, Math.min(100, Math.max(0, getBloodlust() + amount)));
//    }
//
//    @Override
//    public Map<Character, Relationship> getRelationships() {
//        return relationships;
//    }
//
//    public void modifySocialization(float amount) {
//        this.entityData.set(SOCIALIZATION, Math.min(100, Math.max(0, getSocialization() + amount)));
//    }
//
//    public void modifyProcreation(float amount) {
//        this.entityData.set(PROCREATION, Math.min(100, Math.max(0, getProcreation() + amount)));
//    }
//
//    public void modifyRest(float amount) {
//        this.entityData.set(REST, Math.min(100, Math.max(0, getRest() + amount)));
//    }
//
//    public void modifyStress(float amount) {
//        this.entityData.set(STRESS, Math.min(100, Math.max(0, getRest() + amount)));
//    }
//
//    public void setBloodlust(float amount) {
//        this.entityData.set(BLOODLUST, amount);
//    }
//
//    public void setSocialization(float amount) {
//        this.entityData.set(SOCIALIZATION, amount);
//    }
//
//    public void setProcreation(float amount) {
//        this.entityData.set(PROCREATION, amount);
//    }
//
//    public void setRest(float amount) {
//        this.entityData.set(REST, amount);
//    }
//
//    public void setStress(float amount) {
//        this.entityData.set(STRESS, amount);
//    }
//
//    public float getBloodlust() {
//        return this.entityData.get(BLOODLUST);
//    }
//
//    public float getSocialization() {
//        return this.entityData.get(SOCIALIZATION);
//    }
//
//    public float getProcreation() {
//        return this.entityData.get(PROCREATION);
//    }
//
//    public float getRest() {
//        return this.entityData.get(REST);
//    }
//
//    public float getStress() {
//        return this.entityData.get(STRESS);
//    }
//
//
//    public int getClientAngerLevel() {
//        return this.entityData.get(CLIENT_ANGER_LEVEL);
//    }
//
//    private int getActiveAnger() {
//        return this.angerManagement.getActiveAnger(this.getTarget());
//    }
//    public void increaseAngerAt(@Nullable Entity entity) {
//        this.increaseAngerAt(entity, 35);
//    }
//
//    public void increaseAngerAt(@Nullable Entity entity, int offset) {
//        if (!this.isNoAi() && this.canTargetEntity(entity)) {
//            WardenAi.setDigCooldown(this);
//            boolean flag = !(this.getTarget() instanceof Player);
//            int i = this.angerManagement.increaseAnger(entity, offset);
//            if (entity instanceof Player && flag && AngerLevel.byAnger(i).isAngry()) {
//                this.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
//            }
//        }
//    }
//
//    public AngerLevel getAngerLevel() {
//        return AngerLevel.byAnger(this.getActiveAnger());
//    }
//
//    @javax.annotation.Nullable
//    @Override
//    public LivingEntity getTarget() {
//        return this.getTargetFromBrain();
//    }
//
//    @Override
//    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
//        return false;
//    }
//
//
//    @Contract("null->false")
//    public boolean canTargetEntity(@Nullable Entity entity) {
//        return entity instanceof LivingEntity livingentity
//                && this.level() == entity.level()
//                && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity)
//                && !this.isAlliedTo(entity)
//                && livingentity.getType() != EntityType.ARMOR_STAND
//                && livingentity.getType() != EntityType.WARDEN
//                && !livingentity.isInvulnerable()
//                && !livingentity.isDeadOrDying()
//                && this.level().getWorldBorder().isWithinBounds(livingentity.getBoundingBox());
//    }
//
//    @Override
//    public boolean hurt(DamageSource source, float amount) {
//        boolean flag = super.hurt(source, amount);
//        if (!this.level().isClientSide && !this.isNoAi()) {
//            Entity entity = source.getEntity();
//            this.increaseAngerAt(entity, AngerLevel.ANGRY.getMinimumAnger() + 20);
//            if (this.brain.getMemory(MemoryModuleType.ATTACK_TARGET).isEmpty()
//                    && entity instanceof LivingEntity livingentity
//                    && (source.isDirect() || this.closerThan(livingentity, 5.0))) {
//                this.setAttackTarget(livingentity);
//            }
//        }
//
//        return flag;
//    }
//
//    @Override
//    protected void doPush(Entity entity) {
//        if (!this.isNoAi() && !this.getBrain().hasMemoryValue(MemoryModuleType.TOUCH_COOLDOWN)) {
//            this.getBrain().setMemoryWithExpiry(MemoryModuleType.TOUCH_COOLDOWN, Unit.INSTANCE, 20L);
//            this.increaseAngerAt(entity);
//            setDisturbanceLocation(entity.blockPosition(), this);
//        }
//
//        super.doPush(entity);
//    }
//
//    public void setAttackTarget(LivingEntity attackTarget) {
//        this.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, attackTarget);
//        this.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
//    }
//
//    @Override
//    public void tick() {
//        super.tick();
//        if (this.level() instanceof ServerLevel serverlevel) {
//            BitterVibrationSystem.Ticker.tick(serverlevel, this.vibrationData, this.vibrationUser);
//        }
//        modifyBloodlust(BLOODLUST_DECAY);
//        modifySocialization(SOCIALIZATION_DECAY);
//        modifyProcreation(PROCREATION_DECAY);
//        modifyStress(STRESS_REGEN);
//        updateStress();
//    }
//
//    @Override
//    protected void customServerAiStep() {
//        var serverLevel = (ServerLevel) this.level();
//        tickBrain(this);
//        if (this.tickCount % 20 == 0) {
//            this.angerManagement.tick(serverLevel, this::canTargetEntity);
//            this.syncClientAngerLevel();
//        }
//    }
//
//    private void syncClientAngerLevel() {
//        this.entityData.set(CLIENT_ANGER_LEVEL, this.getActiveAnger());
//    }
//
//    @Override
//    protected void sendDebugPackets() {
//        super.sendDebugPackets();
//        DebugPackets.sendEntityBrain(this);
//    }
//
//
//    @Override
//    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
//        return new GroundPathNavigation(this, level) {
//            @Override
//            protected @NotNull PathFinder createPathFinder(int i) {
//                this.nodeEvaluator = new WalkNodeEvaluator();
//                this.nodeEvaluator.setCanPassDoors(true);
//                return new PathFinder(this.nodeEvaluator, i) {
//                    @Override
//                    protected float distance(@NotNull Node entity1, @NotNull Node entity2) {
//                        return entity1.distanceToXZ(entity2);
//                    }
//                };
//            }
//        };
//    }
//
//    @Override
//    protected Brain.@NotNull Provider<SCP939> brainProvider() {
//        return new SmartBrainProvider<>(this);
//    }
//
//    @Override
//    public List<? extends ExtendedSensor<? extends SCP939>> getSensors() {
//        return List.of(
//                new NearbyPlayersSensor<SCP939>().setRadius(1000),
//                new NearbyLivingEntitySensor<>(),
//                new HurtBySensor<>()
//        );
//    }
//
//    @Override
//    public List<Activity> getActivityPriorities() {
//        return ObjectArrayList.of(Activity.FIGHT);
//    }
//
//    @Override
//    public Map<Activity, BrainActivityGroup<? extends SCP939>> getAdditionalTasks() {
//        return Map.of(
//                Activity.INVESTIGATE, getInvestigationTasks(),
//                BitterActivity.HUNT.get(), getHuntTasks(),
//                BitterActivity.PROCREATE.get(), getProcreateTasks(),
//                Activity.REST, getRestTasks(),
//                BitterActivity.SOCIALIZE.get(), getSocializeTasks(),
//                BitterActivity.MENTAL_BREAK.get(), getMentalBreakTasks()
//        );
//    }
//
//    @Override
//    public BrainActivityGroup<? extends SCP939> getCoreTasks() {
//        return BrainActivityGroup.coreTasks(
////                new AvoidSun<>(),
////                new EscapeSun<>().cooldownFor(entity -> 20),
////                TODO: new FleeFireTask<>
//                new LookAtTarget<>(),
//                new MoveToWalkTarget<>(),
//                new ReevaluateDecision<>()
//        );
//    }
//
//    @Override
//    public BrainActivityGroup<? extends SCP939> getIdleTasks() {
//        return BrainActivityGroup.idleTasks(
//
//        );
//    }
//
//    @Override
//    public BrainActivityGroup<? extends SCP939> getFightTasks() {
//        return BrainActivityGroup.fightTasks(
//                new InvalidateAttackTarget<>(),
//                new SetWalkTargetToAttackTarget<>().speedMod((entity, target) -> 1.5f).stopIf(entity -> this.isDeadOrDying()),
//                new AnimatableMeleeAttack<>(0)
////                new LeapAtTarget<>(20)
//        );
//    }
//
//    public BrainActivityGroup<? extends SCP939> getHuntTasks() {
//        return new BrainActivityGroup<SCP939>(BitterActivity.HUNT.get()).behaviours(
////                new SeekNearestPlayer<>()
////                        .cooldownFor(entity -> 120),
//                new OneRandomBehaviour<>(
//                        new Lure<>(20)
//                                .cooldownFor(entity -> BASE_LURE_COOLDOWN),
//                        new Amnesticize<>(60)
//                                .cooldownFor(entity -> 200),
//                        new Listen<>(100)
//                                .cooldownFor(entity -> BASE_LISTEN_COOLDOWN)
//
//                        // TODO: Cooldown as memory type?
//                )
//                        .startCondition(entity -> !BrainUtils.hasMemory(entity, BitterMemoryModuleType.ACTION_COOLDOWN.get()))
//                        .cooldownFor(entity -> 150)
//                        .whenStopping(entity -> BrainUtils.setForgettableMemory(entity, BitterMemoryModuleType.ACTION_COOLDOWN.get(), true, 150)),
//                new OneRandomBehaviour<>(
//                        new SetRandomWalkTarget<>()
//                                .setRadius(getRandom().nextInt(10, 20)),
//                        new Idle<>().runFor(entity -> 60)
//                )
//        );
//    }
//
//    public BrainActivityGroup<? extends SCP939> getInvestigationTasks() {
//        return new BrainActivityGroup<SCP939>(Activity.INVESTIGATE).requireAndWipeMemoriesOnUse(
//                        MemoryModuleType.DISTURBANCE_LOCATION
//                )
//                .behaviours(
//                        new FirstApplicableBehaviour<>(
////                                new InvestigateTarget<>()
////                                        .cooldownFor(entity -> 20),
//                                new AllApplicableBehaviours<>(
//                                        new MoveToWalkTarget<>(),
//                                        new Idle<>().runFor(entity -> 60)
//                                )
//                        )
//                );
//    }
//
//    public BrainActivityGroup<? extends SCP939> getProcreateTasks() {
//        return new BrainActivityGroup<SCP939>(BitterActivity.PROCREATE.get()).behaviours(
//                new Procreate<>(20)
//        );
//    }
//
//    public BrainActivityGroup<? extends SCP939> getRestTasks() {
//        return new BrainActivityGroup<SCP939>(Activity.REST).behaviours(
//                new Rest<>(0.05f)
////                new SequentialBehaviour<>(
//////                        new FindDarkness<>(),
////                        new Rest<>(0.05f)
////                )
//        );
//    }
//
//    public BrainActivityGroup<? extends SCP939> getSocializeTasks() {
//        return new BrainActivityGroup<SCP939>(BitterActivity.SOCIALIZE.get()).behaviours(
//                new GenericInteraction<>()
//                        .closeEnoughDist((entity, partner) -> 8)
//                        .messages(List.of(
//                                        " flickers its bioluminescent spine lights at ",
//                                        " emits a high-pitched tone towards "
//                                )
//                        )
//        );
//    }
//
//    public BrainActivityGroup<? extends SCP939> getMentalBreakTasks() {
//        return new BrainActivityGroup<SCP939>(BitterActivity.MENTAL_BREAK.get()).behaviours(
//                new OneRandomBehaviour<SCP939>(
//                        new WarnHighStress<>(List.of(
//                                " scratches the ground vigorously.",
//                                "'s bioluminescent spine lights flicker in a rapid wave-like rhythm."
//                        )),
////                        new Berserk<>(),
//                        new MurderousRage<>(true),
//                        new SetRandomWalkTarget<>()
//                                .setRadius(getRandom().nextInt(10, 20)),
//                        new Idle<>().runFor(entity -> 30)
//                )
//                        .cooldownFor(entity -> 400)
//        );
//    }
//
//
//    @Override
//    public boolean dampensVibrations() {
//        return true;
//    }
//
//    @Override
//    public @NotNull Data getVibrationData() {
//        return this.vibrationData;
//    }
//
//    @Override
//    public @NotNull User getVibrationUser() {
//        return this.vibrationUser;
//    }
//
//    public static void setDisturbanceLocation(BlockPos pos, SCP939 entity) {
//        BrainUtils.setForgettableMemory(entity, MemoryModuleType.DISTURBANCE_LOCATION, pos, 600);
//        BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(pos, 1, 1));
//    }
//
//    public Component getRandomLureLine() {
//        return Component.literal("HELP");
//    }
//
//    @Override
//    public List<Need<SCP939>> getNeeds() {
//        return List.of(
//                new Need<>(
//                        BLOODLUST,
//                        BitterActivity.HUNT.get(),
//                        value -> (float) Math.pow(100 - value, 1.2),
//                        entity -> true
//                ),
//                new Need<>(
//                        SOCIALIZATION,
//                        BitterActivity.SOCIALIZE.get(),
//                        value -> (float) Math.pow(100 - value, 1.5),
//                        entity -> !entity.level().getNearbyEntities(SCP939.class, TargetingConditions.DEFAULT, entity, entity.getBoundingBox().inflate(16)).isEmpty()
//                ),
//                new Need<>(
//                        PROCREATION,
//                        BitterActivity.PROCREATE.get(),
//                        value -> 100 - value,
//                        entity -> !entity.level().getNearbyEntities(SCP939.class, TargetingConditions.DEFAULT, entity, entity.getBoundingBox().inflate(16)).isEmpty()
//                ),
//                new Need<>(
//                        REST,
//                        Activity.REST,
//                        value -> (float) Math.pow(100 - value, 0.2),
//                        entity -> true
//                ),
//                new Need<>(
//                        STRESS,
//                        BitterActivity.MENTAL_BREAK.get(),
//                        value -> -13 * value + 500,
//                        entity -> true
//                )
//        );
//    }
//
//    @Override
//    public float getMood() {
//        float mood = 0;
//
//        for (Need<SCP939> need : getNeeds()) {
//            mood += this.entityData.get(need.data());
//        }
//
//        return mood / getNeeds().size();
//    }
//}
