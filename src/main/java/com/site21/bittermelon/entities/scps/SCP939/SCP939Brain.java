package com.site21.bittermelon.entities.scps.SCP939;

import com.site21.bittermelon.entities.behavior.attack.Attack;
import com.site21.bittermelon.entities.behavior.attack.Pull;
import com.site21.bittermelon.entities.behavior.attack.Push;
import com.site21.bittermelon.entities.behavior.mood.mentalbreak.MurderousRage;
import com.site21.bittermelon.entities.behavior.mood.mentalbreak.WarnHighStress;
import com.site21.bittermelon.entities.behavior.movement.FindDarkness;
import com.site21.bittermelon.entities.behavior.social.interactions.GenericInteraction;
import com.site21.bittermelon.entities.behavior.target.InvalidateAttackTarget;
import com.site21.bittermelon.entities.scps.SCP939.behavior.*;
import com.site21.bittermelon.init.BitterActivity;
import com.site21.bittermelon.init.BitterMemoryModuleType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.AllApplicableBehaviours;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SCP939Brain {
    private final SCP939 entity;

    public SCP939Brain(SCP939 entity) {
        this.entity = entity;
    }

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

    public BrainActivityGroup<? extends SCP939> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new LookAtTarget<>(),
                new MoveToWalkTarget<>()
        );
    }

    public BrainActivityGroup<? extends SCP939> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new RegenBloodlust<>(),
                new InvalidateAttackTarget<>(),
                new SetWalkTargetToAttackTarget<>().stopIf(LivingEntity::isDeadOrDying),
                new OneRandomBehaviour<>(
                        new Push<>(10).cooldownFor(scp939 -> 120),
                        new Attack<>(10, entity.getAttackTemplates()),
                        new Pull<>(10).cooldownFor(scp939 -> 120)
                ).cooldownFor(scp939 -> 40)

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

                        // TODO: Cooldown as memory type?
                )
                        .startCondition(entity -> !BrainUtils.hasMemory(entity, BitterMemoryModuleType.ACTION_COOLDOWN.get()))
                        .cooldownFor(entity -> 150)
                        .whenStopping(entity -> BrainUtils.setForgettableMemory(entity, BitterMemoryModuleType.ACTION_COOLDOWN.get(), true, 150)),
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<>()
                                .setRadius(entity.getRandom().nextInt(10, 20)),
                        new Idle<>().runFor(entity -> 60)
                )
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
                                .setRadius(entity.getRandom().nextInt(1, 10)),
                        new Idle<>().runFor(entity -> 30)
                )
        );
    }
}
