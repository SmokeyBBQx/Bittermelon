package com.site21.bittermelon.common.content.entities.scp939;

import com.site21.bittermelon.common.content.entities.scp939.behavior.*;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.combat.AttackTemplate;
import com.site21.bittermelon.common.systems.ai.behavior.attack.Attack;
import com.site21.bittermelon.common.systems.ai.behavior.attack.Pull;
import com.site21.bittermelon.common.systems.ai.behavior.attack.Push;
import com.site21.bittermelon.common.systems.ai.behavior.attack.YankItem;
import com.site21.bittermelon.common.systems.ai.behavior.mood.mentalbreak.MurderousRage;
import com.site21.bittermelon.common.systems.ai.behavior.mood.mentalbreak.WarnHighStress;
import com.site21.bittermelon.common.systems.ai.behavior.movement.FindDarkness;
import com.site21.bittermelon.common.systems.ai.behavior.movement.SearchArea;
import com.site21.bittermelon.common.systems.ai.behavior.movement.SeekNearestPlayer;
import com.site21.bittermelon.common.systems.ai.behavior.social.Relationship;
import com.site21.bittermelon.common.systems.ai.behavior.social.Socializable;
import com.site21.bittermelon.common.systems.ai.behavior.social.interactions.GenericInteraction;
import com.site21.bittermelon.common.systems.ai.behavior.target.InvalidateAttackTarget;
import com.site21.bittermelon.common.systems.ai.vibration.BitterAngerManagement;
import com.site21.bittermelon.common.systems.ai.vibration.BitterVibrationSystem;
import com.site21.bittermelon.common.systems.ai.vibration.BitterVibrationUser;
import com.site21.bittermelon.common.systems.ai.base.BitterMob;
import com.site21.bittermelon.common.systems.ai.base.Need;
import com.site21.bittermelon.common.systems.ai.base.NeedInstance;
import com.site21.bittermelon.common.systems.medical.factory.Anatomy;
import com.site21.bittermelon.init.neoforge.BitterActivity;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
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
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.AngerLevel;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
import net.tslat.smartbrainlib.util.BrainUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiConsumer;

import static com.site21.bittermelon.init.neoforge.BitterSounds.SCREAM;

@SuppressWarnings("unchecked")
public class SCP939 extends BitterMob<SCP939> implements Socializable, BitterVibrationSystem {
    private static final EntityDataAccessor<Integer> CLIENT_ANGER_LEVEL = SynchedEntityData.defineId(SCP939.class, EntityDataSerializers.INT);
    private static final List<String> LURE_LINES;

    private final List<UUID> victims;
    private final List<String> remainingLureLines;
    private final Map<Character, Relationship> relationships;
    private final DynamicGameEventListener<Listener> dynamicGameEventListener;
    private final BitterVibrationSystem.User vibrationUser;

    private BitterVibrationSystem.Data vibrationData;
    private BitterAngerManagement angerManagement;

    public SCP939(EntityType<? extends Mob> entityType, Level level) {
        super((EntityType<? extends Monster>) entityType, level);
        victims = new ArrayList<>();
        remainingLureLines = new ArrayList<>(LURE_LINES);
        relationships = new HashMap<>();

        vibrationUser = new BitterVibrationUser(this);
        vibrationData = new BitterVibrationSystem.Data();
        dynamicGameEventListener = new DynamicGameEventListener<>(new BitterVibrationSystem.Listener(this));
        angerManagement = new BitterAngerManagement(this::canTargetEntity, Collections.emptyList());

        initializePathfinding();
    }

    @Override
    protected Character initializeCharacter() {
        return new Character(this.uuid, "SCP-939-" + getRandom().nextInt(1, 24), Anatomy.HUMAN);
    }

    @Override
    protected Map<Need, NeedInstance> initializeNeeds() {
        return Map.of(
                Need.BLOODLUST, new NeedInstance(0.001f, value -> 200.0, BitterActivity.HUNT.get()),
                Need.SOCIALIZATION, new NeedInstance(0.001f, value -> Math.pow(value, 1.5), BitterActivity.SOCIALIZE.get()),
                Need.PROCREATION, new NeedInstance(0.0001f, value -> (double) value, BitterActivity.PROCREATE.get()),
                Need.REST, new NeedInstance(0f, value -> Math.pow(value, 0.2), Activity.REST),
                Need.STRESS, new NeedInstance(-0.0005f, value -> (double) (-13 * value + 500), BitterActivity.MENTAL_BREAK.get()),
                Need.ANGER, new NeedInstance(0f, value -> Math.pow(value, 1.8), Activity.FIGHT)
        );
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.MAX_HEALTH, 150.0)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5)
                .add(Attributes.ATTACK_DAMAGE, 30.0);
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

    @Override
    public void updateDynamicGameEventListener(@NotNull BiConsumer<DynamicGameEventListener<?>, ServerLevel> listenerConsumer) {
        if (level() instanceof ServerLevel serverlevel) {
            listenerConsumer.accept(this.dynamicGameEventListener, serverlevel);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CLIENT_ANGER_LEVEL, 0);
    }

    @Override
    protected void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);

        output.store("listener", Data.CODEC, vibrationData);
        output.store("anger", BitterAngerManagement.codec(this::canTargetEntity), angerManagement);

        ValueOutput.TypedOutputList<UUID> victimsOutput = output.list("victims", UUIDUtil.CODEC);
        for (UUID uuid : victims) {
            victimsOutput.add(uuid);
        }

    }

    @Override
    protected void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);

        input.read("listener", Data.CODEC).ifPresent(data -> vibrationData = data);
        input.read("anger", BitterAngerManagement.codec(this::canTargetEntity))
                .ifPresent(angerM -> angerManagement = angerM);

        victims.clear();
        input.list("victims", UUIDUtil.CODEC).ifPresent(
                list -> victims.addAll(list.stream().toList()));
    }

    @Override
    public Map<Character, Relationship> getRelationships() {
        return relationships;
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
    public boolean hurtServer(@NotNull ServerLevel level, @NotNull DamageSource source, float amount) {
        boolean flag = super.hurtServer(level, source, amount);
        if (!isNoAi()) {
            Entity entity = source.getEntity();
            increaseAngerAt(entity, AngerLevel.ANGRY.getMinimumAnger() + 20);

            if (brain.getMemory(MemoryModuleType.ATTACK_TARGET).isEmpty()
                    && entity instanceof LivingEntity livingentity
                    && (source.isDirect() || closerThan(livingentity, 5.0))) {
                setAttackTarget(livingentity);
            }
        }

        return flag;
    }

    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SCREAM.value();
    }

    @Nullable
    protected SoundEvent getAmbientSound() {
        return this.getAngerLevel().getAmbientSound();
    }

    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
        super.playStepSound(pos, state);
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

        setNeed(Need.ANGER, getActiveAnger());
    }

    @Override
    protected void customServerAiStep(@NotNull ServerLevel level) {
        super.customServerAiStep(level);

        if (this.tickCount % 20 == 0) {
            this.angerManagement.tick(level, this::canTargetEntity);
            this.syncClientAngerLevel();
        }

        angerManagement.getActiveEntity().ifPresent(activeEntity -> {
            if (angerManagement.getActiveAnger(activeEntity) > 90) {
                setAttackTarget(activeEntity);
            }
        });
    }

    private void syncClientAngerLevel() {
        entityData.set(CLIENT_ANGER_LEVEL, getActiveAnger());
        setNeed(Need.ANGER, 100 - getActiveAnger());
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
        BrainUtil.setForgettableMemory(entity, MemoryModuleType.DISTURBANCE_LOCATION, pos, 600);
        BrainUtil.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(pos, 1.2f, 1));
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
                new SeekNearestPlayer<>()
                        .cooldownFor(entity -> 120),
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
                new GenericInteraction<SCP939>()
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

    public @NotNull List<AttackTemplate> getAttackTemplates() {
        return SCP939AttackTemplates.ATTACK_TEMPLATES;
    }

    static {
        LURE_LINES = List.of(
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
    }
}
