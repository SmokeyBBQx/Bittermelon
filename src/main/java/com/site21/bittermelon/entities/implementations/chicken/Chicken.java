package com.site21.bittermelon.entities.implementations.chicken;

import com.google.common.collect.ImmutableList;
import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.entities.ai.behavior.basicneeds.Drink;
import com.site21.bittermelon.entities.ai.behavior.basicneeds.EatFood;
import com.site21.bittermelon.entities.ai.behavior.basicneeds.HasBasicNeeds;
import com.site21.bittermelon.entities.ai.behavior.basicneeds.Preen;
import com.site21.bittermelon.entities.ai.behavior.misc.Defecate;
import com.site21.bittermelon.entities.ai.sensors.NearbyDrinkableFluidsSensor;
import com.site21.bittermelon.entities.ai.sensors.NearbyFoodSensor;
import com.site21.bittermelon.entities.base.BitterMob;
import com.site21.bittermelon.entities.ai.behavior.attack.Attack;
import com.site21.bittermelon.combat.AttackTemplate;
import com.site21.bittermelon.entities.ai.behavior.attack.LeapAtTarget;
import com.site21.bittermelon.entities.ai.behavior.misc.FeelsPain;
import com.site21.bittermelon.entities.ai.behavior.mood.mentalbreak.MurderousRage;
import com.site21.bittermelon.entities.ai.behavior.mood.mentalbreak.WarnHighStress;
import com.site21.bittermelon.entities.ai.behavior.needs.Need;
import com.site21.bittermelon.entities.ai.behavior.social.Relationship;
import com.site21.bittermelon.entities.ai.behavior.social.Socializable;
import com.site21.bittermelon.entities.ai.behavior.social.interactions.GenericInteraction;
import com.site21.bittermelon.entities.ai.behavior.target.InvalidateAttackTarget;
import com.site21.bittermelon.entities.implementations.chicken.behavior.PluckAtRandomItem;
import com.site21.bittermelon.init.BitterActivity;
import com.site21.bittermelon.init.BitterMemoryTypes;
import com.site21.bittermelon.medical.factory.Anatomy;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Panic;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.navigation.SmoothGroundNavigation;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.NearbyItemsSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings("unchecked")
public class Chicken extends BitterMob<Chicken> implements Socializable, FeelsPain, HasBasicNeeds {
    private static final EntityDataAccessor<Float> HUNGER = SynchedEntityData.defineId(Chicken.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> THIRST = SynchedEntityData.defineId(Chicken.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> PROCREATION = SynchedEntityData.defineId(Chicken.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> SOCIALIZATION = SynchedEntityData.defineId(Chicken.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> REST = SynchedEntityData.defineId(Chicken.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DEFECATION = SynchedEntityData.defineId(Chicken.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> MOVEMENT = SynchedEntityData.defineId(Chicken.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> HYGIENE = SynchedEntityData.defineId(Chicken.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> RELAXATION = SynchedEntityData.defineId(Chicken.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> RECREATION = SynchedEntityData.defineId(Chicken.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> STRESS = SynchedEntityData.defineId(Chicken.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ANGER = SynchedEntityData.defineId(Chicken.class, EntityDataSerializers.FLOAT);

    private static final float SOCIALIZATION_DECAY = 0.001f;
    private static final float PROCREATION_DECAY = 0.0001f;
    private static final float STRESS_REGEN = -0.0005f;
    private static final float HUNGER_DECAY = 0.002f;
    private static final float THIRST_DECAY = 0.003f;
    private static final float DEFECATION_DECAY = 0.001f;
    private static final float MOVEMENT_DECAY = 0.001f;
    private static final float HYGIENE_DECAY = 0.001f;
    private static final float RELAXATION_DECAY = 0.001f;
    private static final float RECREATION_DECAY = 0.001f;

    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    public float flapping = 1.0F;
    private float nextFlap = 1.0F;

    private Map<Character, Relationship> relationships = new HashMap<>();

    public Chicken(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level, 3);
    }

    @Override
    protected Character initializeCharacter() {
        // TODO: Procgen descriptions
        return new Character(this.uuid, "Chicken", Anatomy.CHICKEN);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    public Map<Activity, BrainActivityGroup<? extends Chicken>> getAdditionalTasks() {
        Map<Activity, BrainActivityGroup<? extends Chicken>> tasks = new HashMap<>();
        tasks.put(BitterActivity.PROCREATE.get(), getProcreateTasks());
        tasks.put(Activity.REST, getRestTasks());
        tasks.put(BitterActivity.SOCIALIZE.get(), getSocializeTasks());
        tasks.put(BitterActivity.MENTAL_BREAK.get(), getMentalBreakTasks());
        tasks.put(BitterActivity.EAT.get(), getEatTasks());
        tasks.put(BitterActivity.DRINK.get(), getDrinkTasks());
        tasks.put(BitterActivity.DEFECATE.get(), getDefecateTasks());
        tasks.put(BitterActivity.EXPLORE.get(), getExploreTasks());
        tasks.put(BitterActivity.GROOM.get(), getGroomTasks());
        tasks.put(BitterActivity.RELAX.get(), getRelaxTasks());
        tasks.put(BitterActivity.PLAY.get(), getPlayTasks());
        return tasks;
    }

    public BrainActivityGroup<? extends Chicken> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new LookAtTarget<>(),
                new MoveToWalkTarget<>()
        );
    }

    public BrainActivityGroup<? extends Chicken> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new SetRandomLookTarget<>(),
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<>()
                                .setRadius(getRandom().nextInt(1, 10)),
                        new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))
                )
        );
    }

    public BrainActivityGroup<? extends Chicken> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>(),
                new SetWalkTargetToAttackTarget<>().stopIf(LivingEntity::isDeadOrDying),
                new Attack<>(10, getAttackTemplates()).cooldownFor(entity -> 40),
                new LeapAtTarget<>(10)
        );
    }

    public BrainActivityGroup<? extends Chicken> getProcreateTasks() {
        return new BrainActivityGroup<Chicken>(BitterActivity.PROCREATE.get()).behaviours(
        );
    }

    public BrainActivityGroup<? extends Chicken> getRestTasks() {
        return new BrainActivityGroup<Chicken>(Activity.REST).behaviours(
        );
    }

    public BrainActivityGroup<? extends Chicken> getSocializeTasks() {
        return new BrainActivityGroup<Chicken>(BitterActivity.SOCIALIZE.get()).behaviours(
                new GenericInteraction<>()
                        .closeEnoughDist((entity, partner) -> 8)
                        .messages(List.of(" clucks at ")
                        )
        );
    }

    public BrainActivityGroup<? extends Chicken> getMentalBreakTasks() {
        return new BrainActivityGroup<Chicken>(BitterActivity.MENTAL_BREAK.get()).behaviours(
                new OneRandomBehaviour<Chicken>(
                        new WarnHighStress<>(List.of(
                                " cries out."
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

    public BrainActivityGroup<? extends Chicken> getEatTasks() {
        return new BrainActivityGroup<Chicken>(BitterActivity.EAT.get()).behaviours(
                        new EatFood<>()
                );
    }

    public BrainActivityGroup<? extends Chicken> getDrinkTasks() {
        return new BrainActivityGroup<Chicken>(BitterActivity.DRINK.get()).behaviours(
                        new Drink<>()
                );

                // TODO: Instead of not attempting to drink at all if there is no source, make an attempt and have it expire with a message and then cooldown attempt

    }

    public BrainActivityGroup<? extends Chicken> getDefecateTasks() {
        return new BrainActivityGroup<Chicken>(BitterActivity.DEFECATE.get()).behaviours(
                new Defecate<>()
        );
    }

    public BrainActivityGroup<? extends Chicken> getExploreTasks() {
        return new BrainActivityGroup<Chicken>(BitterActivity.EXPLORE.get()).behaviours(
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<>()
                                .setRadius(getRandom().nextInt(5, 15)),
                        new Idle<>().runFor(entity -> 30)
                ).whenStarting(entity -> {
                    if (entity instanceof Chicken chicken) {
                        chicken.modifyMovement(-10.0f);
                    }
                })
        );
    }

    public BrainActivityGroup<? extends Chicken> getGroomTasks() {
        return new BrainActivityGroup<Chicken>(BitterActivity.GROOM.get()).behaviours(
                new Preen<>(10)
                        .messages(List.of(
                                        " grooms itself.",
                                        " tidies itself.",
                                        " plucks at its feathers.",
                                        " preens itself.",
                                        " smooths its feathers.",
                                        " arranges its plumage.",
                                        " straightens its feathers.",
                                        " fluffs its feathers.",
                                        " fixes its plumage."
                                )
                        )
                        .cooldownFor(entity -> 60)
        );
    }

    public BrainActivityGroup<? extends Chicken> getRelaxTasks() {
        return new BrainActivityGroup<Chicken>(BitterActivity.RELAX.get()).behaviours(

        );
    }

    public BrainActivityGroup<? extends Chicken> getPlayTasks() {
        return new BrainActivityGroup<Chicken>(BitterActivity.PLAY.get()).behaviours(
                        // TODO: Plucking at mobs
                        new PluckAtRandomItem<>().cooldownFor(entity -> 120)
                );
    }

    public BrainActivityGroup<? extends Chicken> getPanicTasks() {
        return new BrainActivityGroup<Chicken>(Activity.PANIC).behaviours(
                new Panic<>()
        ).requireAndWipeMemoriesOnUse(MemoryModuleType.HURT_BY);
    }


    @Override
    public List<Need<Chicken>> getNeeds() {
        return ImmutableList.of(
                new Need<>(
                        SOCIALIZATION,
                        BitterActivity.SOCIALIZE.get(),
                        value -> (float) Math.pow(value, 1.5),
                        entity -> {
                            NearestVisibleLivingEntities nearestEntities = BrainUtils.getMemory(entity, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
                            return nearestEntities != null && nearestEntities.contains(entityNear -> entityNear instanceof Chicken);
                        }
                ),
                new Need<>(
                        PROCREATION,
                        BitterActivity.PROCREATE.get(),
                        value -> value,
                        entity -> !entity.level().getNearbyEntities(Chicken.class, TargetingConditions.DEFAULT, entity, entity.getBoundingBox().inflate(16)).isEmpty()
                ),
                new Need<>(
                        REST,
                        Activity.REST,
                        value -> (float) Math.pow(value, 0.2),
                        entity -> false
                ),
                new Need<>(
                        STRESS,
                        BitterActivity.MENTAL_BREAK.get(),
                        value -> value,
                        entity -> true
                ),
                new Need<>(
                        ANGER,
                        Activity.FIGHT,
                        value -> (float) Math.pow(value, 1.8),
                        entity -> true
                ),
                new Need<>(
                        HUNGER,
                        BitterActivity.EAT.get(),
                        value -> (float) Math.pow(value, 2),
                        entity -> {
                            List<?> edibleItems = BrainUtils.getMemory(entity, BitterMemoryTypes.NEARBY_EDIBLE_ITEMS.get());
                            return edibleItems != null && !edibleItems.isEmpty();
                        }
                ),
                new Need<>(
                        THIRST,
                        BitterActivity.DRINK.get(),
                        value -> (float) Math.pow(value, 2),
                        entity -> {
                            List<?> edibleItems = BrainUtils.getMemory(entity, BitterMemoryTypes.NEARBY_DRINKABLE_FLUIDS.get());
                            return edibleItems != null && !edibleItems.isEmpty();
                        }
                ),
                new Need<>(
                        DEFECATION,
                        BitterActivity.DEFECATE.get(),
                        value -> value,
                        entity -> true
                ),
                new Need<>(
                        MOVEMENT,
                        BitterActivity.EXPLORE.get(),
                        value -> (float) Math.pow(value, 1.2),
                        entity -> false
                ),
                new Need<>(
                        HYGIENE,
                        BitterActivity.GROOM.get(),
                        value -> (float) Math.pow(value, 1.3),
                        entity -> true
                ),
                new Need<>(
                        RELAXATION,
                        BitterActivity.RELAX.get(),
                        value -> (float) Math.pow(value, 1.3),
                        entity -> false
                ),
                new Need<>(
                        RECREATION,
                        BitterActivity.PLAY.get(),
                        value -> (float) Math.pow(getMood(), 1.3),
                        entity -> {
                            List<?> edibleItems = BrainUtils.getMemory(entity, SBLMemoryTypes.NEARBY_ITEMS.get());
                            return edibleItems != null && !edibleItems.isEmpty();
                        }
                )
        );
    }

    @Override
    public void tick() {
        super.tick();

        modifySocialization(SOCIALIZATION_DECAY);
        modifyProcreation(PROCREATION_DECAY);
        modifyStress(STRESS_REGEN);
        modifyHunger(HUNGER_DECAY);
        modifyThirst(THIRST_DECAY);
        modifyDefecation(DEFECATION_DECAY);
        modifyMovement(MOVEMENT_DECAY);
        modifyHygiene(HYGIENE_DECAY);
        modifyRelaxation(RELAXATION_DECAY);
        modifyRecreation(RECREATION_DECAY);
        updateStress();
    }

    public void aiStep() {
        super.aiStep();
        this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed += (this.onGround() ? -1.0F : 4.0F) * 0.3F;
        this.flapSpeed = Mth.clamp(this.flapSpeed, 0.0F, 1.0F);
        if (!this.onGround() && this.flapping < 1.0F) {
            this.flapping = 1.0F;
        }

        this.flapping *= 0.9F;
        Vec3 vec3 = this.getDeltaMovement();
        if (!this.onGround() && vec3.y < (double) 0.0F) {
            this.setDeltaMovement(vec3.multiply(1.0F, 0.6, 1.0F));
        }

        this.flap += this.flapping * 2.0F;
    }

    protected boolean isFlapping() {
        return this.flyDist > this.nextFlap;
    }

    protected void onFlap() {
        this.nextFlap = this.flyDist + this.flapSpeed / 2.0F;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.CHICKEN_AMBIENT;
    }

    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.CHICKEN_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.CHICKEN_DEATH;
    }

    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState block) {
        this.playSound(SoundEvents.CHICKEN_STEP, 0.15F, 1.0F);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new SmoothGroundNavigation(this, level);
    }

    @Override
    public List<? extends ExtendedSensor<? extends Chicken>> getSensors() {
        return List.of(
                new NearbyLivingEntitySensor<>(),
                new NearbyItemsSensor<>(),
                new HurtBySensor<>(),
                new NearbyFoodSensor<>(),
                new NearbyDrinkableFluidsSensor<>()
        );
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SOCIALIZATION, 0f);
        builder.define(PROCREATION, 0f);
        builder.define(REST, 0f);
        builder.define(STRESS, 0f);
        builder.define(ANGER, 0f);
        builder.define(HUNGER, 0f);
        builder.define(THIRST, 0f);
        builder.define(DEFECATION, 0f);
        builder.define(MOVEMENT, 0f);
        builder.define(HYGIENE, 0f);
        builder.define(RELAXATION, 0f);
        builder.define(RECREATION, 0f);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("socialization", getSocialization());
        compound.putFloat("procreation", getProcreation());
        compound.putFloat("rest", getRest());
        compound.putFloat("stress", getStress());
        compound.putFloat("hunger", getHunger());
        compound.putFloat("thirst", getThirst());
        compound.putFloat("defecation", getDefecation());
        compound.putFloat("movement", getMovement());
        compound.putFloat("hygiene", getHygiene());
        compound.putFloat("relaxation", getRelaxation());
        compound.putFloat("recreation", getRecreation());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setSocialization(compound.getFloat("socialization"));
        this.setProcreation(compound.getFloat("procreation"));
        this.setRest(compound.getFloat("rest"));
        this.setStress(compound.getFloat("stress"));
        this.setHunger(compound.getFloat("hunger"));
        this.setThirst(compound.getFloat("thirst"));
        this.setDefecation(compound.getFloat("defecation"));
        this.setMovement(compound.getFloat("movement"));
        this.setHygiene(compound.getFloat("hygiene"));
        this.setRelaxation(compound.getFloat("relaxation"));
        this.setRecreation(compound.getFloat("recreation"));
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

    public void modifyHunger(float amount) {
        this.entityData.set(HUNGER, Math.min(100, Math.max(0, getHunger() + amount)));
    }

    public void modifyThirst(float amount) {
        this.entityData.set(THIRST, Math.min(100, Math.max(0, getThirst() + amount)));
    }

    public void modifyDefecation(float amount) {
        this.entityData.set(DEFECATION, Math.min(100, Math.max(0, getDefecation() + amount)));
    }

    public void modifyMovement(float amount) {
        this.entityData.set(MOVEMENT, Math.min(100, Math.max(0, getMovement() + amount)));
    }

    public void modifyHygiene(float amount) {
        this.entityData.set(HYGIENE, Math.min(100, Math.max(0, getHygiene() + amount)));
    }

    public void modifyRelaxation(float amount) {
        this.entityData.set(RELAXATION, Math.min(100, Math.max(0, getRelaxation() + amount)));
    }

    public void modifyRecreation(float amount) {
        this.entityData.set(RECREATION, Math.min(100, Math.max(0, getRecreation() + amount)));
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

    public void setHunger(float amount) {
        this.entityData.set(HUNGER, amount);
    }

    public void setThirst(float amount) {
        this.entityData.set(THIRST, amount);
    }

    public void setDefecation(float amount) {
        this.entityData.set(DEFECATION, amount);
    }

    public void setMovement(float amount) {
        this.entityData.set(MOVEMENT, amount);
    }

    public void setHygiene(float amount) {
        this.entityData.set(HYGIENE, amount);
    }

    public void setRelaxation(float amount) {
        this.entityData.set(RELAXATION, amount);
    }

    public void setRecreation(float amount) {
        this.entityData.set(RECREATION, amount);
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

    public float getHunger() {
        return this.entityData.get(HUNGER);
    }

    public float getThirst() {
        return this.entityData.get(THIRST);
    }

    public float getDefecation() {
        return this.entityData.get(DEFECATION);
    }

    public float getMovement() {
        return this.entityData.get(MOVEMENT);
    }

    public float getHygiene() {
        return this.entityData.get(HYGIENE);
    }

    public float getRelaxation() {
        return this.entityData.get(RELAXATION);
    }

    public float getRecreation() {
        return this.entityData.get(RECREATION);
    }

    @Override
    public Map<Character, Relationship> getRelationships() {
        return relationships;
    }

    @Override
    public boolean wantsToEat(@NotNull ItemStack stack) {
        return stack.is(ItemTags.CHICKEN_FOOD);
    }

    @Override
    public String getPainMessage(float pain) {
        List<String> painMessages = List.of(
                "squawks in distress",
                "lets out a pained cluck",
                "flaps frantically in pain",
                "screeches in agony",
                "clucks desperately",
                "lets out a piercing squawk",
                "flutters in distress",
                "cackles in pain",
                "lets out an alarmed bawk",
                "thrashes about with panicked clucks"
        );

        return painMessages.get(this.random.nextInt(painMessages.size()));
    }

    @Override
    public SoundEvent getPainSound(float pain) {
        return SoundEvents.CHICKEN_HURT;
    }

    public @NotNull List<AttackTemplate> getAttackTemplates() {
        List<AttackTemplate> attackTemplates = new ArrayList<>();

        return attackTemplates;
    }
}
