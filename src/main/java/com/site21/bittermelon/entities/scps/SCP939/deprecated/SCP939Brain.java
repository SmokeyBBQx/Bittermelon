package com.site21.bittermelon.entities.scps.SCP939.deprecated;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.site21.bittermelon.entities.behavior.mood.mentalbreak.MurderousRage;
import com.site21.bittermelon.entities.behavior.mood.mentalbreak.WarnHighStress;
import com.site21.bittermelon.entities.behavior.needs.Need;
import com.site21.bittermelon.entities.behavior.needs.ReevaluateDecision;
import com.site21.bittermelon.entities.behavior.social.interactions.GenericInteraction;
import com.site21.bittermelon.entities.scps.SCP939.SCP939;
import com.site21.bittermelon.entities.scps.SCP939.behavior.*;
import com.site21.bittermelon.init.BitterActivity;
import com.site21.bittermelon.init.BitterMemoryModuleType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.warden.SetRoarTarget;
import net.minecraft.world.entity.ai.behavior.warden.SetWardenLookTarget;
import net.minecraft.world.entity.ai.behavior.warden.TryToSniff;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.HurtBySensor;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.schedule.Activity;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class SCP939Brain {
    private static final List<SensorType<? extends Sensor<? super SCP939>>> SENSOR_TYPES = List.of(
            SensorType.NEAREST_LIVING_ENTITIES,
            SensorType.HURT_BY,
            SensorType.NEAREST_PLAYERS
    );
    private static final List<MemoryModuleType<?>> MEMORY_TYPES = List.of(
            MemoryModuleType.NEAREST_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.PATH,
            MemoryModuleType.ATTACK_TARGET,
            MemoryModuleType.NEAREST_ATTACKABLE,
            MemoryModuleType.DISTURBANCE_LOCATION,
            BitterMemoryModuleType.ACTION_COOLDOWN.get(),
            BitterMemoryModuleType.SOCIALIZE_TARGET.get()
    );

    protected static Brain<?> makeBrain(SCP939 scp939, Dynamic<?> ops) {
        Brain.Provider<SCP939> provider = Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
        Brain<SCP939> brain = provider.makeBrain(ops);
        initCoreActivity(brain);
        initIdleActivity(brain, scp939);
        initFightActivity(brain, scp939);
        initHuntActivity(brain, scp939);
        initProcreateActivity(brain);
        initRestActivity(brain);
        initSocializeActivity(brain);
        initMentalBreakActivity(brain, scp939);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    public static void updateActivity(SCP939 scp939) {
        List<Need<SCP939>> needs = new ArrayList<>(scp939.getNeeds());

        needs.removeIf(need -> !need.canBeFulfilled().test(scp939));

        if (!needs.isEmpty()) {
            needs.sort((n1, n2) -> {
                float priority1 = n1.priorityFunction().apply(scp939.getEntityData().get(n1.data()));
                float priority2 = n2.priorityFunction().apply(scp939.getEntityData().get(n2.data()));
                return Float.compare(priority2, priority1);
            });

            List<Activity> activities = needs.stream()
                    .map(Need::activity)
                    .collect(Collectors.toList());

            scp939.getBrain().setActiveActivityToFirstValid(activities);
        }
    }

    private static void initCoreActivity(Brain<SCP939> brain) {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new Swim(0.8F),
                        new LookAtTarget<>(),
                        new MoveToWalkTarget<>()
                )
        );
    }

    private static void initIdleActivity(Brain<SCP939> brain, SCP939 scp939) {
        brain.addActivity(
                Activity.IDLE,
                10,
                ImmutableList.of(
                        new OneRandomBehaviour<>(
                                new SetRandomWalkTarget<>()
                                        .setRadius(scp939.getRandom().nextInt(10, 20)),
                                new Idle<>().runFor(entity -> 60)
                        )
                )
        );
    }

    private static void initFightActivity(Brain<SCP939> brain, SCP939 scp939) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.FIGHT,
                10,
                ImmutableList.of(
                        new InvalidateAttackTarget<>(),
                        new SetWalkTargetToAttackTarget<>().speedMod((entity, target) -> 1.5f).stopIf(entity -> scp939.isDeadOrDying()),
                        new AnimatableMeleeAttack<>(0)
                ),
                MemoryModuleType.ATTACK_TARGET
        );
    }

    private static void initHuntActivity(Brain<SCP939> brain, SCP939 scp939) {
        brain.addActivity(
                BitterActivity.HUNT.get(),
                5,
                ImmutableList.of(
                        new OneRandomBehaviour<>(
                                new Lure<>(20)
                                        .cooldownFor(entity -> 200),
                                new Amnesticize<>(60)
                                        .cooldownFor(entity -> 200),
                                new Listen<>(100)
                                        .cooldownFor(entity -> 300)

                                // TODO: Cooldown as memory type?
                        )
                                .startCondition(entity -> !BrainUtils.hasMemory(entity, BitterMemoryModuleType.ACTION_COOLDOWN.get()))
                                .cooldownFor(entity -> 150)
                                .whenStopping(entity -> BrainUtils.setForgettableMemory(entity, BitterMemoryModuleType.ACTION_COOLDOWN.get(), true, 150)),
                        new OneRandomBehaviour<>(
                                new SetRandomWalkTarget<>()
                                        .setRadius(scp939.getRandom().nextInt(10, 20)),
                                new Idle<>().runFor(entity -> 60)
                        )
                )
        );
    }

    private static void initProcreateActivity(Brain<SCP939> brain) {
        brain.addActivity(
                BitterActivity.PROCREATE.get(),
                5,
                ImmutableList.of(
                        new Procreate<>(20)
                )
        );
    }

    private static void initRestActivity(Brain<SCP939> brain) {
        brain.addActivity(
                Activity.REST,
                5,
                ImmutableList.of(
                        new Rest<>(0.05f)
                )
        );
    }

    private static void initSocializeActivity(Brain<SCP939> brain) {
        brain.addActivity(
                BitterActivity.SOCIALIZE.get(),
                5,
                ImmutableList.of(
                        new GenericInteraction<>()
                                .closeEnoughDist((entity, partner) -> 8)
                                .messages(List.of(
                                                " flickers its bioluminescent spine lights at ",
                                                " emits a high-pitched tone towards "
                                        )
                                )
                )
        );
    }

    private static void initMentalBreakActivity(Brain<SCP939> brain, SCP939 scp939) {
        brain.addActivity(
                BitterActivity.MENTAL_BREAK.get(),
                5,
                ImmutableList.of(
                        new OneRandomBehaviour<SCP939>(
                                new WarnHighStress<>(List.of(
                                        " scratches the ground vigorously.",
                                        "'s bioluminescent spine lights flicker in a rapid wave-like rhythm."
                                )),
//                        new Berserk<>(),
                                new MurderousRage<>(true),
                                new SetRandomWalkTarget<>()
                                        .setRadius(scp939.getRandom().nextInt(10, 20)),
                                new Idle<>().runFor(entity -> 30)
                        )
                                .cooldownFor(entity -> 400)
                )
        );
    }
}
