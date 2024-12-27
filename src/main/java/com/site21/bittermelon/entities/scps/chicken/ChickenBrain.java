package com.site21.bittermelon.entities.scps.chicken;

import com.site21.bittermelon.entities.behavior.attack.Attack;
import com.site21.bittermelon.entities.behavior.attack.LeapAtTarget;
import com.site21.bittermelon.entities.behavior.mood.mentalbreak.MurderousRage;
import com.site21.bittermelon.entities.behavior.mood.mentalbreak.WarnHighStress;
import com.site21.bittermelon.entities.behavior.social.interactions.GenericInteraction;
import com.site21.bittermelon.entities.behavior.target.InvalidateAttackTarget;
import com.site21.bittermelon.init.BitterActivity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Panic;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ChickenBrain {
    private final Chicken entity;

    public ChickenBrain(Chicken entity) {
        this.entity = entity;
    }

    public Map<Activity, BrainActivityGroup<? extends Chicken>> getAdditionalTasks() {
        Map<Activity, BrainActivityGroup<? extends Chicken>> tasks = new HashMap<>();
        tasks.put(BitterActivity.PROCREATE.get(), getProcreateTasks());
        tasks.put(Activity.REST, getRestTasks());
        tasks.put(BitterActivity.SOCIALIZE.get(), getSocializeTasks());
        tasks.put(BitterActivity.MENTAL_BREAK.get(), getMentalBreakTasks());
        tasks.put(BitterActivity.EAT.get(), getEatTasks());
        tasks.put(BitterActivity.DRINK.get(), getDrinkTasks());
        tasks.put(BitterActivity.URINATE.get(), getUrinateTasks());
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
                                .setRadius(entity.getRandom().nextInt(1, 10)),
                        new Idle<>().runFor(entity -> 30)
                )
        );
    }

    public BrainActivityGroup<? extends Chicken> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>(),
                new SetWalkTargetToAttackTarget<>().stopIf(LivingEntity::isDeadOrDying),
                new Attack<>(10, entity.getAttackTemplates()).cooldownFor(entity -> 40),
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
                                .setRadius(entity.getRandom().nextInt(1, 10)),
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

    public BrainActivityGroup<? extends Chicken> getUrinateTasks() {
        return new BrainActivityGroup<Chicken>(BitterActivity.URINATE.get()).behaviours(
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
                                .setRadius(entity.getRandom().nextInt(5, 15)),
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
}
