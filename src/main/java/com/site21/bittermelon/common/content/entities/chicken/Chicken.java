package com.site21.bittermelon.common.content.entities.chicken;

import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.combat.AttackTemplate;
import com.site21.bittermelon.common.systems.ai.behavior.attack.Attack;
import com.site21.bittermelon.common.systems.ai.behavior.attack.LeapAtTarget;
import com.site21.bittermelon.common.systems.ai.behavior.basicneeds.Drink;
import com.site21.bittermelon.common.systems.ai.behavior.basicneeds.EatFood;
import com.site21.bittermelon.common.systems.ai.behavior.basicneeds.HasBasicNeeds;
import com.site21.bittermelon.common.systems.ai.behavior.basicneeds.Preen;
import com.site21.bittermelon.common.systems.ai.behavior.misc.Defecate;
import com.site21.bittermelon.common.systems.ai.behavior.misc.FeelsPain;
import com.site21.bittermelon.common.systems.ai.behavior.mentalbreak.MurderousRage;
import com.site21.bittermelon.common.systems.ai.behavior.mentalbreak.WarnHighStress;
import com.site21.bittermelon.common.systems.ai.behavior.social.Relationship;
import com.site21.bittermelon.common.systems.ai.behavior.social.Socializable;
import com.site21.bittermelon.common.systems.ai.behavior.social.interactions.GenericInteraction;
import com.site21.bittermelon.common.systems.ai.behavior.target.InvalidateAttackTarget;
import com.site21.bittermelon.common.systems.ai.sensors.NearbyDrinkableFluidsSensor;
import com.site21.bittermelon.common.systems.ai.sensors.NearbyFoodSensor;
import com.site21.bittermelon.common.systems.ai.base.BitterMob;
import com.site21.bittermelon.common.systems.ai.base.Need;
import com.site21.bittermelon.common.systems.ai.base.NeedInstance;
import com.site21.bittermelon.common.content.entities.chicken.behavior.PluckAtRandomItem;
import com.site21.bittermelon.common.systems.medical.factory.Anatomy;
import com.site21.bittermelon.init.neoforge.BitterActivity;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.navigation.SmoothGroundNavigation;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.custom.NearbyItemsSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.site21.bittermelon.common.systems.ai.base.Need.MOVEMENT;

@SuppressWarnings("unchecked")
public class Chicken extends BitterMob<Chicken> implements Socializable, FeelsPain, HasBasicNeeds {
    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    public float flapping = 1.0F;
    private float nextFlap = 1.0F;

    private final Map<Character, Relationship> relationships;

    public Chicken(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level, 3);
        relationships = new HashMap<>();
    }

    @Override
    protected Character initializeCharacter() {
        // TODO: Procgen descriptions
        return new Character(this.uuid, "Chicken", Anatomy.HUMAN);
    }

    @Override
    protected Map<Need, NeedInstance> initializeNeeds() {
        HashMap<Need, NeedInstance> statConfigs = new HashMap<>();
        statConfigs.put(Need.SOCIALIZATION, new NeedInstance(0.001f, value -> Math.pow(value, 1.5), BitterActivity.SOCIALIZE.get()));
        statConfigs.put(Need.PROCREATION, new NeedInstance(0.0001f, value -> (double) value, BitterActivity.PROCREATE.get()));
        statConfigs.put(Need.REST, new NeedInstance(0f, value -> Math.pow(value, 0.2), Activity.REST));
        statConfigs.put(Need.STRESS, new NeedInstance(-0.0005f, value -> (double) value, BitterActivity.MENTAL_BREAK.get()));
        statConfigs.put(Need.HUNGER, new NeedInstance(0.002f, value -> Math.pow(value, 2), BitterActivity.EAT.get()));
        statConfigs.put(Need.THIRST, new NeedInstance(0.003f, value -> Math.pow(value, 2), BitterActivity.DRINK.get()));
        statConfigs.put(Need.DEFECATION, new NeedInstance(0.001f, value -> (double) value, BitterActivity.DEFECATE.get()));
        statConfigs.put(Need.MOVEMENT, new NeedInstance(0.001f, value -> Math.pow(value, 1.2), BitterActivity.EXPLORE.get()));
        statConfigs.put(Need.HYGIENE, new NeedInstance(0.001f, value -> Math.pow(value, 1.3), BitterActivity.GROOM.get()));
        statConfigs.put(Need.RELAXATION, new NeedInstance(0.001f, value -> Math.pow(value, 1.3), BitterActivity.RELAX.get()));
        statConfigs.put(Need.RECREATION, new NeedInstance(0.001f, value -> Math.pow(getMood(), 1.3), BitterActivity.PLAY.get()));
        statConfigs.put(Need.ANGER, new NeedInstance(0f, value -> Math.pow(value, 1.8), Activity.FIGHT));
        return statConfigs;
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0).add(Attributes.MOVEMENT_SPEED, 0.25);
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
                new GenericInteraction<Chicken>()
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
                        chicken.modifyNeed(MOVEMENT, -10.0f);
                    }
                })
        );
    }

    public BrainActivityGroup<? extends Chicken> getGroomTasks() {
        return new BrainActivityGroup<Chicken>(BitterActivity.GROOM.get()).behaviours(
                new Preen<Chicken>(10)
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
        return new ArrayList<>();
    }
}
