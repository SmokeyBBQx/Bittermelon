package com.site21.bittermelon.entities.scps.SCP939;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.warden.TryToSniff;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.sniffer.SnifferAi;
import net.minecraft.world.entity.schedule.Activity;

import java.util.List;

import static com.site21.bittermelon.init.ActivityInit.*;
import static com.site21.bittermelon.init.MemoryModuleTypeInit.*;

public class SCP939AI {
    private static final List<SensorType<? extends Sensor<? super SCP939>>> SENSOR_TYPES = List.of(
            SensorType.NEAREST_PLAYERS
    );
    private static final List<MemoryModuleType<?>> MEMORY_TYPES = List.of(
            MemoryModuleType.NEAREST_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
            MemoryModuleType.NEAREST_VISIBLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
            MemoryModuleType.NEAREST_VISIBLE_NEMESIS,
            MemoryModuleType.LOOK_TARGET,
            MemoryModuleType.WALK_TARGET,
            MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
            MemoryModuleType.PATH,
            MemoryModuleType.ATTACK_TARGET,
            MemoryModuleType.ATTACK_COOLING_DOWN,
            MemoryModuleType.NEAREST_ATTACKABLE,
            MemoryModuleType.DISTURBANCE_LOCATION,
            EXHALE_COOLDOWN.get(),
            LURE_COOLDOWN.get(),
            LISTEN_COOLDOWN.get(),
            IS_LISTENING.get(),
            IS_EXHALING.get()
    );

    public static Brain.Provider<SCP939> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    protected static Brain<?> makeBrain(SCP939 scp939, Brain<SCP939> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initFightActivity(scp939, brain);
        initInvestigateActivity(brain);
        initExhaleActivity(brain);
        initLureActivity(brain);
        initListenActivity(brain);
        initListenActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    public static void updateActivity(SCP939 scp939) {
        scp939.getBrain()
                .setActiveActivityToFirstValid(
                        ImmutableList.of(LURE.get(), EXHALE.get(), Activity.FIGHT, Activity.INVESTIGATE, LISTEN.get(), Activity.IDLE)
                );
    }

    private static void initCoreActivity(Brain<SCP939> brain) {
        brain.addActivity(
                Activity.CORE,
                0,
                ImmutableList.of(
                        new Swim(0.8F),
                        new LookAtTargetSink(45, 90),
                        new MoveToTargetSink()
                )
        );
    }

    private static void initIdleActivity(Brain<SCP939> brain) {
        brain.addActivity(
                Activity.IDLE,
                10,
                ImmutableList.of(
                        TryToListen.create(),
                        new RunOne<>(
                                ImmutableMap.of(IS_LISTENING.get(), MemoryStatus.VALUE_ABSENT),
                                ImmutableList.of(Pair.of(RandomStroll.stroll(0.5F), 2), Pair.of(new DoNothing(30, 60), 1))
                        )
                )
        );
    }

    private static void initInvestigateActivity(Brain<SCP939> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.INVESTIGATE,
                5,
                ImmutableList.of(
                        GoToTargetLocation.create(MemoryModuleType.DISTURBANCE_LOCATION, 2, 0.7F)
                ),
                MemoryModuleType.DISTURBANCE_LOCATION
        );
    }

    private static void initExhaleActivity(Brain<SCP939> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                EXHALE.get(),
                5,
                ImmutableList.of(),
                IS_EXHALING.get()
        );
    }

    private static void initListenActivity(Brain<SCP939> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                LISTEN.get(),
                5,
                ImmutableList.of(new Listening<>(120)),
                IS_LISTENING.get()
        );
    }

    private static void initLureActivity(Brain<SCP939> brain) {
        brain.addActivity(
                LURE.get(),
                10,
                ImmutableList.of()
        );
    }

    private static void initFightActivity(SCP939 scp939, Brain<SCP939> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.FIGHT,
                10,
                ImmutableList.of(
                        StopAttackingIfTargetInvalid.<SCP939>create(
                                entity -> !scp939.canTargetEntity(entity), SCP939AI::onTargetInvalid, false
                        ),
                        SetEntityLookTarget.create(entity -> isTarget(scp939, entity), (float) scp939.getAttributeValue(Attributes.FOLLOW_RANGE)),
                        SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.2F),
                        MeleeAttack.create(10)
                ),
                MemoryModuleType.ATTACK_TARGET

        );
    }

    private static boolean isTarget(SCP939 scp939, LivingEntity entity) {
        return scp939.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).filter(target -> target == entity).isPresent();
    }

    private static void onTargetInvalid(SCP939 scp939, LivingEntity target) {
        if (!scp939.canTargetEntity(target)) {

        }
    }

    public static void setDisturbanceLocation(SCP939 scp939, BlockPos disturbanceLocation) {
        if (scp939.level().getWorldBorder().isWithinBounds(disturbanceLocation)
//                && scp939.getEntityAngryAt().isEmpty()
                && scp939.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isEmpty()) {
            scp939.getBrain().setMemoryWithExpiry(LISTEN_COOLDOWN.get(), Unit.INSTANCE, 100L);
            scp939.getBrain().setMemoryWithExpiry(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(disturbanceLocation), 100L);
            scp939.getBrain().setMemoryWithExpiry(MemoryModuleType.DISTURBANCE_LOCATION, disturbanceLocation, 100L);
            scp939.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
        }
    }

}
