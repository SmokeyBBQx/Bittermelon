package com.site21.bittermelon.common.content.entities.scp939;

import com.site21.bittermelon.common.systems.ai.base.BitterMob;
import com.site21.bittermelon.common.systems.ai.base.Need;
import com.site21.bittermelon.common.systems.ai.base.NeedInstance;
import com.site21.bittermelon.common.systems.ai.vibration.BitterVibrationSystem;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.combat.AttackTemplate;
import com.site21.bittermelon.init.neoforge.BitterActivity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.tslat.smartbrainlib.api.core.navigation.SmoothGroundNavigation;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.util.BrainUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

import static com.site21.bittermelon.init.neoforge.BitterSounds.SCREAM;

@SuppressWarnings("unchecked")
public class SCP939Old extends BitterMob<SCP939Old> implements BitterVibrationSystem {
    private static final EntityDataAccessor<Integer> CLIENT_ANGER_LEVEL = SynchedEntityData.defineId(SCP939Old.class, EntityDataSerializers.INT);
    private static final List<String> LURE_LINES;

    private final List<UUID> victims;
    private final List<String> remainingLureLines;

    public SCP939Old(EntityType<? extends Mob> entityType, Level level) {
        super((EntityType<? extends Monster>) entityType, level);
        victims = new ArrayList<>();
        remainingLureLines = new ArrayList<>(LURE_LINES);
    }

    @Override
    protected Character initializeCharacter() {
        return new Character(this.uuid, "SCP-939-" + getRandom().nextInt(1, 24));
    }

    @Override
    protected Map<Need, NeedInstance> initializeNeeds() {
        return Map.of(
                Need.BLOODLUST, new NeedInstance(0.001f, _ -> 200.0, BitterActivity.HUNT.get()),
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

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CLIENT_ANGER_LEVEL, 0);
    }

    @Override
    protected void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);

        ValueOutput.TypedOutputList<UUID> victimsOutput = output.list("victims", UUIDUtil.CODEC);
        for (UUID uuid : victims) {
            victimsOutput.add(uuid);
        }

    }

    @Override
    protected void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);

        victims.clear();
        input.list("victims", UUIDUtil.CODEC).ifPresent(
                list -> victims.addAll(list.stream().toList()));
    }

    public void addVictim(@NotNull Character victim) {
        // TODO: Human condition
        UUID uuid = victim.getId();
        if (!victims.contains(uuid)) {
            victims.add(victim.getId());
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

    @Nullable
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

    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SCREAM.value();
    }

    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
        super.playStepSound(pos, state);
    }

    public void setAttackTarget(LivingEntity attackTarget) {
        this.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, attackTarget);
        this.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new SmoothGroundNavigation(this, level);
    }

    @Override
    public boolean dampensVibrations() {
        return true;
    }

    public static void setDisturbanceLocation(BlockPos pos, SCP939Old entity) {
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
        // TODO: Store lines?

        String lureLine = remainingLureLines.get(getRandom().nextInt(remainingLureLines.size()));
        remainingLureLines.remove(lureLine);

        return Component.literal(name + ": \"" + lureLine + "\"");
    }

//    @Override
//    public List<? extends ExtendedSensor<?>> getSensors(T owner) {
//        return List.of(
//                new NearbyLivingEntitySensor<>(),
//                new UnreachableTargetSensor<>(),
//                new HurtBySensor<>()
//        );
//    }
//
//    @Override
//    public List<? extends BehaviorControl<?>> getAlwaysRunningBehaviours(T owner) {
//        return super.getAlwaysRunningBehaviours(owner);
//    }
//
//    @Override
//    public List<? extends BehaviorControl<?>> getIdleBehaviours(T owner) {
//        return List.of(
//                new OneRandomBehaviour<>(
//                        new SetRandomWalkTarget<>()
//                                .setRadius(getRandom().nextInt(10, 20)),
//                        new Idle<>().runFor(_ -> 60)
//                )
//        );
//    }
//
//    @Override
//    public List<? extends BehaviorControl<?>> getFightingBehaviours(T owner) {
//        return List.of(
//                new BitterInvalidateAttackTarget<>(),
//                new SetWalkTargetToAttackTarget<>()
//                        .speedModifier((_, _) -> 1.4f)
//                        .stopIf(LivingEntity::isDeadOrDying),
//                // new Attack<>(10, getAttackTemplates())
//                //        .cooldownFor(scp939 -> 60),
//                new OneRandomBehaviour<>(
//                        new Push<>(10),
//                        new YankItem<>(10),
//                        new Pull<>(10)
//                ).cooldownFor(_ -> 120)
//        );
//    }
//
//    @Override
//    public List<? extends ExtendedSensor<?>> getSensors(SCP939Old owner) {
//        return List.of();
//    }
//
//    @SuppressWarnings("rawtypes")
//    @Override
//    public ActivityBuilder<? extends T> getActivityGroupFor(Activity activity) {
//        ActivityBuilder<T> builder = ActivityBuilder.create(activity);
//        if (activity.equals(Activity.INVESTIGATE)) {
//            builder.behaviours((List)getInvestigationBehaviours(this))
//                    .requireAndClearMemoriesOnUse(MemoryModuleType.DISTURBANCE_LOCATION);
//        } else if (activity.equals(BitterActivity.HUNT.get())) {
//            builder.behaviours((List) getHuntBehaviours(this));
//        }
//
//        return builder;
//    }
//
//    public List<BehaviorControl<?>> getInvestigationBehaviours(SCP939Old ignoredOwner) {
//        return List.of(
//                new MoveToWalkTarget<>(),
//                new Idle<>().runFor(_ -> 60)
//        );
//    }
//
//    public List<Object> getHuntBehaviours(SCP939Old ignoredOwner) {
//        return List.of(
//                new SeekNearestPlayer<>()
//                        .cooldownFor(_ -> 120),
//                new SearchArea<>()
//        );
//    }

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

    @Override
    public List<? extends ExtendedSensor<?>> getSensors(SCP939Old owner) {
        return List.of();
    }
}
