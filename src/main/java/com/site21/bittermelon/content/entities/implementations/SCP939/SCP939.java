package com.site21.bittermelon.content.entities.implementations.SCP939;

import com.google.common.collect.ImmutableList;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.content.entities.BitterAngerManagement;
import com.site21.bittermelon.content.entities.BitterVibrationSystem;
import com.site21.bittermelon.content.entities.ai.behavior.attack.YankItem;
import com.site21.bittermelon.content.entities.ai.behavior.movement.SearchArea;
import com.site21.bittermelon.content.entities.base.BitterMob;
import com.site21.bittermelon.content.entities.ai.behavior.attack.Attack;
import com.site21.bittermelon.content.combat.AttackTemplate;
import com.site21.bittermelon.content.entities.ai.behavior.attack.Pull;
import com.site21.bittermelon.content.entities.ai.behavior.attack.Push;
import com.site21.bittermelon.content.entities.ai.behavior.mood.mentalbreak.MurderousRage;
import com.site21.bittermelon.content.entities.ai.behavior.mood.mentalbreak.WarnHighStress;
import com.site21.bittermelon.content.entities.ai.behavior.movement.FindDarkness;
import com.site21.bittermelon.content.entities.ai.behavior.needs.Need;
import com.site21.bittermelon.content.entities.ai.behavior.social.Relationship;
import com.site21.bittermelon.content.entities.ai.behavior.social.Socializable;
import com.site21.bittermelon.content.entities.ai.behavior.social.interactions.GenericInteraction;
import com.site21.bittermelon.content.entities.ai.behavior.target.InvalidateAttackTarget;
import com.site21.bittermelon.content.entities.ai.BitterVibrationUser;
import com.site21.bittermelon.content.entities.implementations.SCP939.behavior.*;
import com.site21.bittermelon.content.medical.damage.generators.*;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.init.neoforge.BitterActivity;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.factory.Anatomy;
import com.site21.bittermelon.content.miscellaneous.stumble.StumbleHandler;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Unit;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.AngerLevel;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.AllApplicableBehaviours;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.navigation.SmoothGroundNavigation;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.UnreachableTargetSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;

import static com.site21.bittermelon.init.neoforge.BitterSounds.*;

@SuppressWarnings("unchecked")
public class SCP939 extends BitterMob<SCP939> implements Socializable, BitterVibrationSystem {
    private static final EntityDataAccessor<Float> BLOODLUST = SynchedEntityData.defineId(SCP939.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> SOCIALIZATION = SynchedEntityData.defineId(SCP939.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> PROCREATION = SynchedEntityData.defineId(SCP939.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> REST = SynchedEntityData.defineId(SCP939.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> STRESS = SynchedEntityData.defineId(SCP939.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ANGER = SynchedEntityData.defineId(SCP939.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> CLIENT_ANGER_LEVEL = SynchedEntityData.defineId(SCP939.class, EntityDataSerializers.INT);

    private static final float BLOODLUST_DECAY = 0.001f;
    private static final float SOCIALIZATION_DECAY = 0.001f;
    private static final float PROCREATION_DECAY = 0.0001f;
    private static final float STRESS_REGEN = -0.0005f;

    private final List<UUID> victims = new ArrayList<>();

    private static final Logger LOGGER = LogUtils.getLogger();
    private final Map<Character, Relationship> relationships = new HashMap<>();
    private final DynamicGameEventListener<Listener> dynamicGameEventListener;
    private final BitterVibrationSystem.User vibrationUser;
    private BitterVibrationSystem.Data vibrationData;
    private BitterAngerManagement angerManagement;

    private static final List<String> LURE_LINES = List.of(
            "Help me please!",
            "I'm hurt, I need help!",
            "Someone please help!",
            "Over here! Help!",
            "I'm bleeding! Help!",
            "Please, I'm injured!",
            "Medic! I need a medic!",
            "I can't move! Help me!",
            "Is anyone there? Help!",
            "Oh god, help me please!",
            "Come out now!",
            "Hey, what's up man?",
            "Jeez, you scared me.",
            "Cool, cool.",
            "That's great!",
            "We'll get out of here in no time.",
            "What's that?",
            "Did you hear that?",
            "Get down!",
            "We're here to help you.",
            "Come out, it's safe.",
            "This is SD, come out!",
            "I think he's scared.",
            "There's nothing to be afraid of.",
            "Did you get that?"
    );

    private final List<String> remainingLureLines;

    public SCP939(EntityType<? extends Mob> entityType, Level level) {
        super((EntityType<? extends Monster>) entityType, level);
        this.vibrationUser = new BitterVibrationUser(this);
        this.vibrationData = new BitterVibrationSystem.Data();
        this.dynamicGameEventListener = new DynamicGameEventListener<>(new BitterVibrationSystem.Listener(this));
        this.angerManagement = new BitterAngerManagement(this::canTargetEntity, Collections.emptyList());
        this.xpReward = 5;
        remainingLureLines = new ArrayList<>(LURE_LINES);

        initializePathfinding();
    }

    private void initializePathfinding() {
        this.getNavigation().setCanFloat(true);
        this.setPathfindingMalus(PathType.UNPASSABLE_RAIL, 0.0F);
        this.setPathfindingMalus(PathType.DAMAGE_OTHER, 8.0F);
        this.setPathfindingMalus(PathType.POWDER_SNOW, 8.0F);
        this.setPathfindingMalus(PathType.LAVA, 8.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, 0.0F);
        this.setPathfindingMalus(PathType.DANGER_FIRE, 0.0F);
    }

    protected Character initializeCharacter() {
        return new Character(this.uuid, "SCP-939-" + getRandom().nextInt(1, 24), Anatomy.HUMAN);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.MAX_HEALTH, 150.0)
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
        builder.define(BLOODLUST, 0f);
        builder.define(SOCIALIZATION, 0f);
        builder.define(PROCREATION, 0f);
        builder.define(REST, 0f);
        builder.define(STRESS, 0f);
        builder.define(ANGER, 0f);
        builder.define(CLIENT_ANGER_LEVEL, 0);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("bloodlust", getBloodlust());
        compound.putFloat("socialization", getSocialization());
        compound.putFloat("procreation", getProcreation());
        compound.putFloat("rest", getRest());
        compound.putFloat("stress", getStress());

        ListTag list = new ListTag();
        for (UUID uuid : victims) {
            CompoundTag uuidTag = new CompoundTag();
            uuidTag.putUUID("UUID", uuid);
            list.add(uuidTag);
        }
        compound.put("Victims", list);

        Data.CODEC.encodeStart(NbtOps.INSTANCE, this.vibrationData).resultOrPartial(
                LOGGER::error).ifPresent(tag -> compound.put("listener", tag));
        BitterAngerManagement.codec(this::canTargetEntity).encodeStart(NbtOps.INSTANCE, this.angerManagement).resultOrPartial(
                LOGGER::error).ifPresent(tag -> compound.put("anger", tag));
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setBloodlust(compound.getFloat("bloodlust"));
        this.setSocialization(compound.getFloat("socialization"));
        this.setProcreation(compound.getFloat("procreation"));
        this.setRest(compound.getFloat("rest"));
        this.setStress(compound.getFloat("stress"));

        ListTag list = compound.getList("Victims", CompoundTag.TAG_COMPOUND);
        victims.clear();
        for (int i = 0; i < list.size(); i++) {
            CompoundTag uuidTag = list.getCompound(i);
            victims.add(uuidTag.getUUID("UUID"));
        }

        if (compound.contains("anger")) {
            BitterAngerManagement.codec(this::canTargetEntity).parse(
                    new Dynamic<>(NbtOps.INSTANCE, compound.get("anger"))).resultOrPartial(LOGGER::error).ifPresent(
                    angerM -> this.angerManagement = angerM);
            this.syncClientAngerLevel();
        }
        if (compound.contains("listener", 10)) Data.CODEC.parse(
                new Dynamic<>(NbtOps.INSTANCE, compound.getCompound("listener"))).resultOrPartial(
                LOGGER::error).ifPresent(data -> this.vibrationData = data);
    }

    @Override
    public Map<Character, Relationship> getRelationships() {
        return relationships;
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

    public void modifyRest(float amount) {
        this.entityData.set(REST, Math.min(100, Math.max(0, getRest() + amount)));
    }

    public void modifyStress(float amount) {
        this.entityData.set(STRESS, Math.min(100, Math.max(0, getRest() + amount)));
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

    public void setRest(float amount) {
        this.entityData.set(REST, amount);
    }

    public void setStress(float amount) {
        this.entityData.set(STRESS, amount);
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

    public float getRest() {
        return this.entityData.get(REST);
    }

    public float getStress() {
        return this.entityData.get(STRESS);
    }

    public List<UUID> getVictims() {
        return victims;
    }

    public void addVictim(@NotNull Character victim) {
        // TODO: Human condition
        UUID uuid = victim.getUUID();
        if (!victims.contains(uuid)) {
            victims.add(victim.getUUID());
        }
    }

    public Character getRandomVictim() {
        if (victims.isEmpty()) return null;

        List<Character> validVictims = victims.stream()
                .map(CharacterManager.get(level())::getCharacter)
                .filter(Objects::nonNull)
                .toList();

        return validVictims.isEmpty() ? null : validVictims.get(getRandom().nextInt(validVictims.size()));
    }

    public int getClientAngerLevel() {
        return this.entityData.get(CLIENT_ANGER_LEVEL);
    }

    private int getActiveAnger() {
        return this.angerManagement.getActiveAnger(this.getTarget());
    }

    public void increaseAngerAt(@Nullable Entity entity) {
        this.increaseAngerAt(entity, 35);
    }

    public void increaseAngerAt(@Nullable Entity entity, int offset) {
        if (!this.isNoAi() && this.canTargetEntity(entity)) {
            if (!(entity instanceof LivingEntity livingEntity)) return;

            int i = this.angerManagement.increaseAnger(entity, offset);
            if (AngerLevel.byAnger(i).isAngry()) {
                setAttackTarget(livingEntity);
            }
        }
    }

    public AngerLevel getAngerLevel() {
        return AngerLevel.byAnger(this.getActiveAnger());
    }

    @javax.annotation.Nullable
    @Override
    public LivingEntity getTarget() {
        return this.getTargetFromBrain();
    }

    @Contract("null->false")
    public boolean canTargetEntity(@Nullable Entity entity) {
        return entity instanceof LivingEntity livingentity
                && this.level() == entity.level()
                && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity)
                && !this.isAlliedTo(entity)
                && livingentity.getType() != EntityType.ARMOR_STAND
                && livingentity.getType() != EntityType.WARDEN
                && !livingentity.isInvulnerable()
                && !livingentity.isDeadOrDying()
                && livingentity != this
                && this.level().getWorldBorder().isWithinBounds(livingentity.getBoundingBox());
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        boolean flag = super.hurt(source, amount);
        if (!this.level().isClientSide && !this.isNoAi()) {
            Entity entity = source.getEntity();
            this.increaseAngerAt(entity, AngerLevel.ANGRY.getMinimumAnger() + 20);
            if (this.brain.getMemory(MemoryModuleType.ATTACK_TARGET).isEmpty()
                    && entity instanceof LivingEntity livingentity
                    && (source.isDirect() || this.closerThan(livingentity, 5.0))) {
                this.setAttackTarget(livingentity);
            }
        }

        return flag;
    }

    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SCREAM.get();
    }

    @Override
    protected void doPush(@NotNull Entity entity) {
        if (!this.isNoAi() && !this.getBrain().hasMemoryValue(MemoryModuleType.TOUCH_COOLDOWN)) {
            if (entity.getType() != this.getType()) {
                this.getBrain().setMemoryWithExpiry(MemoryModuleType.TOUCH_COOLDOWN, Unit.INSTANCE, 20L);
                this.increaseAngerAt(entity);
                setDisturbanceLocation(entity.blockPosition(), this);
            }
        }

        super.doPush(entity);
    }

    public void setAttackTarget(LivingEntity attackTarget) {
        this.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, attackTarget);
        this.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level() instanceof ServerLevel serverlevel) {
            BitterVibrationSystem.Ticker.tick(serverlevel, this.vibrationData, this.vibrationUser);
        }

        modifyBloodlust(BLOODLUST_DECAY);
        modifySocialization(SOCIALIZATION_DECAY);
        modifyProcreation(PROCREATION_DECAY);
        modifyStress(STRESS_REGEN);
        updateStress();

        this.entityData.set(ANGER, Math.min(100, Math.max(0, (float) getActiveAnger())));
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        var serverLevel = (ServerLevel) this.level();

        if (this.tickCount % 20 == 0) {
            this.angerManagement.tick(serverLevel, this::canTargetEntity);
            this.syncClientAngerLevel();
        }

        angerManagement.getActiveEntity().ifPresent(activeEntity -> {
            if (angerManagement.getActiveAnger(activeEntity) > 90) {
                setAttackTarget(activeEntity);
            }
        });
    }

    private void syncClientAngerLevel() {
        this.entityData.set(CLIENT_ANGER_LEVEL, this.getActiveAnger());
        this.entityData.set(ANGER, 100.0f - this.getActiveAnger());
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new SmoothGroundNavigation(this, level);
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
        BrainUtils.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(pos, 1.2f, 1));

        System.out.println("Disturbance location set");
    }

    public Component getRandomLureLine() {
        String name;
        Character victim = getRandomVictim();

        if (victim == null) {
            String[] firstNames = {"John", "Sarah", "Mike", "Emma", "David", "Lisa", "James", "Anna", "Chris", "Amy"};
            String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez"};

            name = firstNames[getRandom().nextInt(firstNames.length)] + " " + lastNames[getRandom().nextInt(lastNames.length)];
        } else {
            name = victim.getName();
        }

        if (remainingLureLines.isEmpty()) {
            remainingLureLines.addAll(LURE_LINES);
        }

        // TODO: Sounds
        // TODO: Personality sequences and conversations
        // TODO: Store messages?

        String lureLine = remainingLureLines.get(getRandom().nextInt(remainingLureLines.size()));
        remainingLureLines.remove(lureLine);

        return Component.literal(name + ": \"" + lureLine + "\"");
    }

    @Override
    public List<? extends ExtendedSensor<? extends SCP939>> getSensors() {
        return ObjectArrayList.of(
                new NearbyPlayersSensor<>(),
                new NearbyLivingEntitySensor<>(),
                new UnreachableTargetSensor<>(),
                new HurtBySensor<>()
        );
    }

    @Override
    public Map<Activity, BrainActivityGroup<? extends SCP939>> getAdditionalTasks() {
        return Map.of(
                Activity.INVESTIGATE, getInvestigationTasks(),
                BitterActivity.HUNT.get(), getHuntTasks(),
                BitterActivity.PROCREATE.get(), getProcreateTasks(),
                Activity.REST, getRestTasks(),
                BitterActivity.SOCIALIZE.get(), getSocializeTasks(),
                BitterActivity.MENTAL_BREAK.get(), getMentalBreakTasks()
        );
    }

    @Override
    public BrainActivityGroup<? extends SCP939> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new LookAtTarget<>(),
                new MoveToWalkTarget<>(),
                new FindDarkness<>()
        );
    }

    @Override
    public BrainActivityGroup<? extends SCP939> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<>()
                                .setRadius(getRandom().nextInt(10, 20)),
                        new Idle<>().runFor(entity -> 60)
                )
        );
    }

    @Override
    public BrainActivityGroup<? extends SCP939> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new RegenBloodlust<>(),
                new InvalidateAttackTarget<>(),
                new SetWalkTargetToAttackTarget<>()
                        .speedMod((entity, target) -> 1.4f)
                        .stopIf(LivingEntity::isDeadOrDying),
                new Attack<>(10, getAttackTemplates())
                        .cooldownFor(scp939 -> 60),
                new OneRandomBehaviour<>(
                        new Push<>(10),
                        new YankItem<>(10),
                        new Pull<>(10)
                ).cooldownFor(scp939 -> 120)

        );
    }

    public BrainActivityGroup<? extends SCP939> getHuntTasks() {
        return new BrainActivityGroup<SCP939>(BitterActivity.HUNT.get()).behaviours(
//                new SeekNearestPlayer<>()
//                        .cooldownFor(entity -> 120),
                new OneRandomBehaviour<>(
                        new Lure<>(20)
                                .cooldownFor(entity -> 200),
                        new Amnesticize<>(60)
                                .cooldownFor(entity -> 200),
                        new Listen<>(100)
                                .cooldownFor(entity -> 300)
                )
                        .cooldownFor(entity -> 150),
                new SearchArea<>()
//                new OneRandomBehaviour<>(
//                        new SetRandomWalkTarget<>()
//                                .setRadius(getRandom().nextInt(10, 20)),
//                        new Idle<>().runFor(entity -> 60)
//                )
        );
    }

    public BrainActivityGroup<? extends SCP939> getInvestigationTasks() {
        return new BrainActivityGroup<SCP939>(Activity.INVESTIGATE).requireAndWipeMemoriesOnUse(
                        MemoryModuleType.DISTURBANCE_LOCATION
                )
                .behaviours(
                        new FirstApplicableBehaviour<>(
                                new AllApplicableBehaviours<>(
                                        new MoveToWalkTarget<>(),
                                        new Idle<>().runFor(entity -> 60)
                                )
                        )
                );
    }

    public BrainActivityGroup<? extends SCP939> getProcreateTasks() {
        return new BrainActivityGroup<SCP939>(BitterActivity.PROCREATE.get()).behaviours(
                new Procreate<>(20)
        );
    }

    public BrainActivityGroup<? extends SCP939> getRestTasks() {
        return new BrainActivityGroup<SCP939>(Activity.REST).behaviours(
                new FindDarkness<>(),
                new Rest<>(0.05f)
        );
    }

    public BrainActivityGroup<? extends SCP939> getSocializeTasks() {
        return new BrainActivityGroup<SCP939>(BitterActivity.SOCIALIZE.get()).behaviours(
                new GenericInteraction<>()
                        .closeEnoughDist((entity, partner) -> 8)
                        .messages(List.of(
                                        " flickers its bioluminescent spine lights at ",
                                        " emits a high-pitched tone towards "
                                )
                        )
        );
    }

    public BrainActivityGroup<? extends SCP939> getMentalBreakTasks() {
        return new BrainActivityGroup<SCP939>(BitterActivity.MENTAL_BREAK.get()).behaviours(
                new OneRandomBehaviour<SCP939>(
                        new WarnHighStress<>(List.of(
                                " scratches the ground vigorously.",
                                "'s bioluminescent spine lights flicker in a rapid wave-like rhythm."
                        )),
                        new MurderousRage<>(true)
                )
                        .cooldownFor(entity -> 400),
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<>()
                                .setRadius(getRandom().nextInt(1, 10)),
                        new Idle<>().runFor(entity -> 30)
                )
        );
    }

    @Override
    public List<Need<SCP939>> getNeeds() {
        return ImmutableList.of(
                new Need<>(
                        BLOODLUST,
                        BitterActivity.HUNT.get(),
                        value -> (float) 200,
                        entity -> true
                ),
                new Need<>(
                        SOCIALIZATION,
                        BitterActivity.SOCIALIZE.get(),
                        value -> (float) Math.pow(value, 1.5),
                        entity -> !entity.level().getNearbyEntities(SCP939.class, TargetingConditions.DEFAULT, entity, entity.getBoundingBox().inflate(16)).isEmpty()
                ),
                new Need<>(
                        PROCREATION,
                        BitterActivity.PROCREATE.get(),
                        value -> value,
                        entity -> !entity.level().getNearbyEntities(SCP939.class, TargetingConditions.DEFAULT, entity, entity.getBoundingBox().inflate(16)).isEmpty()
                ),
                new Need<>(
                        REST,
                        Activity.REST,
                        value -> (float) Math.pow(value, 0.2),
                        entity -> true
                ),
                new Need<>(
                        STRESS,
                        BitterActivity.MENTAL_BREAK.get(),
                        value -> -13 * value + 500,
                        entity -> false
                ),
                new Need<>(
                        ANGER,
                        Activity.FIGHT,
                        value -> (float) Math.pow(value, 1.8),
                        entity -> true
                )
        );
    }

    public @NotNull List<AttackTemplate> getAttackTemplates() {
        List<AttackTemplate> attackTemplates = new ArrayList<>();

        attackTemplates.add(new AttackTemplate.AttackTemplateBuilder()
                .setDamageSupplier(() -> new BoneBreakingBite(EnumSet.of(CompartmentTag.SOFT_TISSUE, CompartmentTag.HARD_TISSUE)))
                .setArea(2)
                .setDepthRange(1, 6)
                .setDamage(15)
                .addModifier(MedicalStats::getMovement, 0.2f)
                .addModifier(MedicalStats::getBite, 0.7f)
                .addModifier(MedicalStats::getSight, 0.1f)
                .setMessages(
                        "%s sinks its fangs deep into %s's %s",
                        "%s snaps its jaws at %s's %s viciously",
                        "%s tears into %s's %s with razor-sharp teeth",
                        "%s chomps down on %s's %s with crushing force",
                        "%s lunges with open maw at %s's %s",
                        "%s clamps its jaws around %s's %s",
                        "%s gnashes its teeth into %s's %s",
                        "%s rips and tears at %s's %s with serrated fangs",
                        "%s mauls %s's %s with powerful jaws",
                        "%s bites down on %s's %s with bone-crushing force",
                        "%s savagely bites into %s's %s",
                        "%s's fangs pierce into %s's %s",
                        "%s latches onto %s's %s with its teeth",
                        "%s snaps its fangs at %s's %s",
                        "%s's jaws close around %s's %s with frightening speed",
                        "%s violently bites down on %s's %s",
                        "%s tries to take a chunk out of %s's %s",
                        "%s's teeth flash as it bites %s's %s",
                        "%s lunges with gnashing teeth at %s's %s",
                        "%s attempts to sink its teeth into %s's %s"
                )
                .setSound(BITE.get())
                .build()
        );

        attackTemplates.add(new AttackTemplate.AttackTemplateBuilder()
                .setDamageSupplier(() -> new Lacerations(EnumSet.of(CompartmentTag.SOFT_TISSUE, CompartmentTag.HARD_TISSUE)))
                .setArea(3)
                .setDepthRange(1, 6)
                .setDamage(15)
                .addModifier(MedicalStats::getMovement, 0.3f)
                .addModifier(MedicalStats::getManipulation, 0.6f)
                .addModifier(MedicalStats::getSight, 0.1f)
                .setMessages(
                        "%s rakes its claws across %s's %s",
                        "%s slashes viciously at %s's %s with razor claws",
                        "%s tears into %s's %s with deadly claws",
                        "%s swipes its massive claws at %s's %s",
                        "%s rips through %s's %s with sharp claws",
                        "%s slices at %s's %s with lethal precision",
                        "%s shreds at %s's %s with wicked claws",
                        "%s carves through %s's %s's defenses",
                        "%s slashes wildly at %s's %s",
                        "%s cleaves at %s's %s with savage claws",
                        "%s's claws flash as they slice toward %s's %s",
                        "%s tears viciously at %s's %s with hooked claws",
                        "%s launches a devastating slash at %s's %s",
                        "%s's claws cut through the air toward %s's %s",
                        "%s swipes with murderous intent at %s's %s",
                        "%s slashes with frightening speed at %s's %s",
                        "%s rends at %s's %s with cruel claws",
                        "%s's claws whistle through the air at %s's %s",
                        "%s unleashes a frenzied series of slashes at %s's %s"
                )
                .setSound(SLASH.get())
                .build()
        );

        attackTemplates.add(new AttackTemplate.AttackTemplateBuilder()
                .setDamageSupplier(() -> new BluntForceTrauma(EnumSet.of(CompartmentTag.SOFT_TISSUE, CompartmentTag.HARD_TISSUE)))
                .setArea(4)
                .setDepthRange(1, 5)
                .setDamage(15)
                .addModifier(MedicalStats::getMovement, 0.7f)
                .addModifier(MedicalStats::getManipulation, 0.2f)
                .addModifier(MedicalStats::getSight, 0.1f)
                .setMessages(
                        "%s smashes into %s's %s with devastating force",
                        "%s rams full force into %s's %s",
                        "%s crashes down upon %s's %s",
                        "%s hammers %s's %s with bone-crushing strength",
                        "%s slams bodily into %s's %s",
                        "%s batters %s's %s with overwhelming power",
                        "%s thunders into %s's %s",
                        "%s crushes %s's %s with unstoppable momentum",
                        "%s pummels %s's %s with immense force",
                        "%s drives into %s's %s with crushing weight",
                        "%s pounds %s's %s with devastating impact",
                        "%s bulldozes into %s's %s mercilessly",
                        "%s batters %s's %s with tremendous force",
                        "%s delivers a crushing blow to %s's %s"
                )
                .setCondition((attacker, target) -> !StumbleHandler.isStumbled(target)
                        && !StumbleHandler.isStumbled(attacker))
                .setSpecialAction((attacker, target) -> StumbleHandler.stumble(target))
                .setSound(SMASH.get())
                .build()
        );

        attackTemplates.add(new AttackTemplate.AttackTemplateBuilder()
                .setDamageSupplier(() -> new Bite(EnumSet.of(CompartmentTag.SOFT_TISSUE, CompartmentTag.HARD_TISSUE)))
                .setArea(3)
                .setDepthRange(1, 5)
                .setDamage(12)
                .setPriority(4)
                .addModifier(MedicalStats::getMovement, 0.45f)
                .addModifier(MedicalStats::getManipulation, 0.45f)
                .addModifier(MedicalStats::getSight, 0.1f)
                .setMessages(
                        "%s violently yanks at %s's %s with its jaws",
                        "%s grabs and tears viciously at %s's %s",
                        "%s latches onto %s's %s and pulls with savage force",
                        "%s seizes %s's %s in its teeth and wrenches back",
                        "%s clamps down on %s's %s and thrashes wildly",
                        "%s grips %s's %s tightly and rips away",
                        "%s snags %s's %s and jerks its head violently",
                        "%s catches hold of %s's %s and tears brutally",
                        "%s locks its jaws on %s's %s and yanks hard",
                        "%s grabs %s's %s and shakes ferociously",
                        "%s clutches %s's %s in its teeth and pulls savagely",
                        "%s bites down on %s's %s and drags forcefully",
                        "%s seizes and wrenches at %s's %s ruthlessly",
                        "%s catches %s's %s and rips with terrifying strength",
                        "%s snaps onto %s's %s and pulls with brutal force",
                        "%s grabs hold of %s's %s and tears viciously",
                        "%s clamps onto %s's %s and yanks mercilessly",
                        "%s latches onto %s's %s and thrashes with deadly force",
                        "%s seizes %s's %s and pulls with crushing strength",
                        "%s grips %s's %s and tears with savage intensity"
                )
                .setCondition((attacker, target) -> StumbleHandler.isStumbled(target))
                .setSpecialAction((attacker, target) -> {
                    Vec3 pullDirection = attacker.getLookAngle().multiply(-2, 1, -2);
                    target.setDeltaMovement(pullDirection);
                    target.hurtMarked = true;
                })
                .setSound(DRAG.get())
                .build()
        );

        attackTemplates.add(new AttackTemplate.AttackTemplateBuilder()
                .setDamageSupplier(Stab::new)
                .setArea(2)
                .setDepthRange(5, 12)
                .setDamage(20)
                .setPriority(0.8f)
                .addModifier(MedicalStats::getMovement, 0.2f)
                .addModifier(MedicalStats::getManipulation, 0.6f)
                .addModifier(MedicalStats::getSight, 0.2f)
                .setMessages(
                        "%s drives its claws deep into %s's %s",
                        "%s punctures %s's %s with razor-sharp claws",
                        "%s sinks its claws into %s's %s with deadly force",
                        "%s impales %s's %s with wickedly sharp claws",
                        "%s pierces through %s's %s with cruel claws",
                        "%s plunges its claws straight into %s's %s",
                        "%s thrusts its claws into %s's %s viciously",
                        "%s drives its claws through %s's %s with brutal force",
                        "%s skewers %s's %s with razor-tipped claws",
                        "%s stabs its claws deep within %s's %s",
                        "%s gouges its claws into %s's %s",
                        "%s punctures straight through %s's %s with its claws",
                        "%s impales deeply into %s's %s with its claws",
                        "%s sinks its claws with frightening speed into %s's %s",
                        "%s plunges its cruel claws into %s's %s"
                )
                .setSound(STAB.get())
                .build()
        );

        attackTemplates.add(new AttackTemplate.AttackTemplateBuilder()
                .setDamageSupplier(() -> new BluntForceTrauma(EnumSet.of(CompartmentTag.SOFT_TISSUE, CompartmentTag.HARD_TISSUE)))
                .setArea(4)
                .setDepthRange(1, 5)
                .setDamage(15)
                .setPriority(4)
                .addModifier(MedicalStats::getMovement, 0.4f)
                .addModifier(MedicalStats::getManipulation, 0.5f)
                .addModifier(MedicalStats::getSight, 0.1f)
                .setMessages(
                        "%s stomps down on %s's %s",
                        "%s stomps heavily onto %s's %s",
                        "%s brings its foot down on %s's %s",
                        "%s stomps at %s's %s",
                        "%s steps down hard on %s's %s",
                        "%s stomps forcefully on %s's %s",
                        "%s drives its foot down on %s's %s",
                        "%s stomps powerfully onto %s's %s",
                        "%s brings its weight down on %s's %s",
                        "%s stomps straight down at %s's %s",
                        "%s slams its foot onto %s's %s",
                        "%s steps violently onto %s's %s",
                        "%s stomps directly on %s's %s",
                        "%s brings its foot crashing onto %s's %s",
                        "%s stomps swiftly at %s's %s",
                        "%s stomps hard on %s's %s",
                        "%s drives its weight onto %s's %s",
                        "%s stomps viciously at %s's %s",
                        "%s brings its foot heavily onto %s's %s",
                        "%s stomps ruthlessly on %s's %s"
                )
                .setCondition((attacker, target) -> StumbleHandler.isStumbled(target)
                        && !StumbleHandler.isStumbled(attacker))
                .setSound(WRESTLE.get())
                .build()
        );

        return attackTemplates;
    }
}
