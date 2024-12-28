package com.site21.bittermelon.entities.scps.chicken;

import com.google.common.collect.ImmutableList;
import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.entities.base.BitterMob;
import com.site21.bittermelon.entities.behavior.attack.Attack;
import com.site21.bittermelon.entities.behavior.attack.AttackTemplate;
import com.site21.bittermelon.entities.behavior.attack.LeapAtTarget;
import com.site21.bittermelon.entities.behavior.misc.FeelsPain;
import com.site21.bittermelon.entities.behavior.mood.mentalbreak.MurderousRage;
import com.site21.bittermelon.entities.behavior.mood.mentalbreak.WarnHighStress;
import com.site21.bittermelon.entities.behavior.needs.Need;
import com.site21.bittermelon.entities.behavior.social.Relationship;
import com.site21.bittermelon.entities.behavior.social.Socializable;
import com.site21.bittermelon.entities.behavior.social.interactions.GenericInteraction;
import com.site21.bittermelon.entities.behavior.target.InvalidateAttackTarget;
import com.site21.bittermelon.init.BitterActivity;
import com.site21.bittermelon.medical.factory.Anatomy;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.Items;
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
import net.tslat.smartbrainlib.api.core.navigation.SmoothGroundNavigation;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class Chicken extends BitterMob<Chicken> implements Socializable, FeelsPain {
    private static final EntityDataAccessor<Float> HUNGER = SynchedEntityData.defineId(Chicken.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> THIRST = SynchedEntityData.defineId(Chicken.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> PROCREATION = SynchedEntityData.defineId(Chicken.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> SOCIALIZATION = SynchedEntityData.defineId(Chicken.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> REST = SynchedEntityData.defineId(Chicken.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DEFECATION = SynchedEntityData.defineId(Chicken.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> MOVEMENT = SynchedEntityData.defineId(Chicken.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> HYGIENE = SynchedEntityData.defineId(Chicken.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> RECREATION = SynchedEntityData.defineId(Chicken.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> STRESS = SynchedEntityData.defineId(Chicken.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ANGER = SynchedEntityData.defineId(Chicken.class, EntityDataSerializers.FLOAT);

    private static final float SOCIALIZATION_DECAY = -0.001f;
    private static final float PROCREATION_DECAY = -0.0001f;
    private static final float STRESS_REGEN = 0.0005f;
    private static final float HUNGER_DECAY = -0.002f;
    private static final float THIRST_DECAY = -0.003f;
    private static final float DEFECATION_DECAY = -0.001f;
    private static final float MOVEMENT_DECAY = -0.001f;
    private static final float HYGIENE_DECAY = -0.001f;
    private static final float RECREATION_DECAY = -0.001f;

    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    public float flapping = 1.0F;
    private float nextFlap = 1.0F;

    public Chicken(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
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
        tasks.put(BitterActivity.PLAY.get(), getPlayTasks());
        return tasks;
    }

    public BrainActivityGroup<? extends Chicken> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new LookAtTarget<>(),
                new MoveToWalkTarget<>(),
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<>()
                                .setRadius(getRandom().nextInt(1, 10)),
                        new Idle<>().runFor(entity -> 30)
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
                        .messages(List.of(
                                        " clucks at "
                                )
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
        );
    }

    public BrainActivityGroup<? extends Chicken> getDrinkTasks() {
        return new BrainActivityGroup<Chicken>(BitterActivity.DRINK.get()).behaviours(
        );
    }

    public BrainActivityGroup<? extends Chicken> getDefecateTasks() {
        return new BrainActivityGroup<Chicken>(BitterActivity.DEFECATE.get()).behaviours(
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
                        chicken.modifyMovement(10.0f);
                    }
                })
        );
    }

    public BrainActivityGroup<? extends Chicken> getGroomTasks() {
        return new BrainActivityGroup<Chicken>(BitterActivity.GROOM.get()).behaviours(
        );
    }

    public BrainActivityGroup<? extends Chicken> getPlayTasks() {
        return new BrainActivityGroup<Chicken>(BitterActivity.PLAY.get()).behaviours(
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
                        value -> (float) Math.pow(100 - value, 1.5),
                        entity -> !entity.level().getNearbyEntities(Chicken.class, TargetingConditions.DEFAULT, entity, entity.getBoundingBox().inflate(16)).isEmpty()
                ),
                new Need<>(
                        PROCREATION,
                        BitterActivity.PROCREATE.get(),
                        value -> 100 - value,
                        entity -> !entity.level().getNearbyEntities(Chicken.class, TargetingConditions.DEFAULT, entity, entity.getBoundingBox().inflate(16)).isEmpty()
                ),
                new Need<>(
                        REST,
                        Activity.REST,
                        value -> (float) Math.pow(100 - value, 0.2),
                        entity -> true
                ),
                new Need<>(
                        STRESS,
                        BitterActivity.MENTAL_BREAK.get(),
                        value -> -13 * value + 500,
                        entity -> true
                ),
                new Need<>(
                        ANGER,
                        Activity.FIGHT,
                        value -> (float) Math.pow(100 - value, 1.8),
                        entity -> true
                ),
                new Need<>(
                        HUNGER,
                        BitterActivity.EAT.get(),
                        value -> (float) Math.pow(100 - value, 2),
                        entity -> !entity.level().getEntitiesOfClass(ItemEntity.class,
                                entity.getBoundingBox().inflate(16),
                                item -> item.getItem().getItem() == Items.WHEAT_SEEDS).isEmpty()
                ),
                new Need<>(
                        THIRST,
                        BitterActivity.DRINK.get(),
                        value -> (float) Math.pow(100 - value, 2),
                        entity -> true
                ),
                new Need<>(
                        DEFECATION,
                        BitterActivity.DEFECATE.get(),
                        value -> (float) Math.pow(value, 2),
                        entity -> true
                ),
                new Need<>(
                        MOVEMENT,
                        BitterActivity.EXPLORE.get(),
                        value -> (float) Math.pow(100 - value, 1.2),
                        entity -> true
                ),
                new Need<>(
                        HYGIENE,
                        BitterActivity.GROOM.get(),
                        value -> (float) Math.pow(100 - value, 1.3),
                        entity -> true
                ),
                new Need<>(
                        RECREATION,
                        BitterActivity.PLAY.get(),
                        value -> (float) Math.pow(100 - value, 1.4),
                        entity -> true
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
                new HurtBySensor<>()
        );
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SOCIALIZATION, 100.0f);
        builder.define(PROCREATION, 100.0f);
        builder.define(REST, 100.0f);
        builder.define(STRESS, 100.0f);
        builder.define(ANGER, 100.0f);
        builder.define(HUNGER, 100.0f);
        builder.define(THIRST, 100.0f);
        builder.define(DEFECATION, 100.0f);
        builder.define(MOVEMENT, 100.0f);
        builder.define(HYGIENE, 100.0f);
        builder.define(RECREATION, 100.0f);
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
        compound.putFloat("recreation", getRecreation());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setSocialization(compound.contains("socialization") ? compound.getFloat("socialization") : 100.0f);
        this.setProcreation(compound.contains("procreation") ? compound.getFloat("procreation") : 100.0f);
        this.setRest(compound.contains("rest") ? compound.getFloat("rest") : 100.0f);
        this.setStress(compound.contains("stress") ? compound.getFloat("stress") : 100.0f);
        this.setHunger(compound.contains("hunger") ? compound.getFloat("hunger") : 100.0f);
        this.setThirst(compound.contains("thirst") ? compound.getFloat("thirst") : 100.0f);
        this.setDefecation(compound.contains("defecation") ? compound.getFloat("defecation") : 100.0f);
        this.setMovement(compound.contains("movement") ? compound.getFloat("movement") : 100.0f);
        this.setHygiene(compound.contains("hygiene") ? compound.getFloat("hygiene") : 100.0f);
        this.setRecreation(compound.contains("recreation") ? compound.getFloat("recreation") : 100.0f);
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

    public float getRecreation() {
        return this.entityData.get(RECREATION);
    }

    @Override
    public Map<Character, Relationship> getRelationships() {
        return Map.of();
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
