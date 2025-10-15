package com.site21.bittermelon.content.entities.scp1507;

import com.site21.bittermelon.systems.character.Character;
import com.site21.bittermelon.systems.ai.base.BitterMob;
import com.site21.bittermelon.systems.ai.base.Need;
import com.site21.bittermelon.systems.ai.base.NeedInstance;
import com.site21.bittermelon.systems.medical.factory.Anatomy;
import com.site21.bittermelon.init.neoforge.BitterActivity;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SCP1507 extends BitterMob<SCP1507> implements SmartBrainOwner<SCP1507> {
    public final AnimationState attackAnimationState = new AnimationState();

    public SCP1507(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected Character initializeCharacter() {
        return new Character(uuid, "SCP-1507-" + getRandom().nextInt(1, 24), Anatomy.HUMAN);
    }

    @Override
    protected Map<Need, NeedInstance> initializeNeeds() {
        return Map.of(
                Need.SOCIALIZATION, new NeedInstance(0.001f, value -> Math.pow(value, 1.2), BitterActivity.SOCIALIZE.get()),
                Need.MOVEMENT, new NeedInstance(0.001f, value -> Math.pow(value, 1.5), BitterActivity.EXPLORE.get()),
                Need.STRESS, new NeedInstance(-0.0005f, value -> Math.pow(value, 0.8), BitterActivity.MENTAL_BREAK.get()),
                Need.ANGER, new NeedInstance(0f, value -> Math.pow(value, 2.0), Activity.FIGHT)
        );
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20).add(Attributes.MOVEMENT_SPEED, 0.23f);
    }

    @Override
    public List<? extends ExtendedSensor<? extends SCP1507>> getSensors() {
        return List.of(
                new NearbyLivingEntitySensor<>(),
                new HurtBySensor<>()
        );
    }

    public Map<Activity, BrainActivityGroup<? extends SCP1507>> getAdditionalTasks() {
        Map<Activity, BrainActivityGroup<? extends SCP1507>> tasks = new HashMap<>();
        tasks.put(BitterActivity.EXPLORE.get(), getExploreTasks());
        return tasks;
    }

    @Override
    public BrainActivityGroup<? extends SCP1507> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new LookAtTarget<>(),
                new MoveToWalkTarget<>()
        );
    }

    public BrainActivityGroup<? extends SCP1507> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<>()
                                .setRadius(getRandom().nextInt(1, 10)),
                        new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))
                )
        );
    }

    public BrainActivityGroup<? extends SCP1507> getExploreTasks() {
        return new BrainActivityGroup<SCP1507>(BitterActivity.EXPLORE.get()).behaviours(
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<>()
                                .setRadius(getRandom().nextInt(5, 15)),
                        new Idle<>().runFor(entity -> 30)
                ).whenStarting(entity -> {
                    if (entity instanceof SCP1507 scp1507) {
                        scp1507.modifyNeed(Need.MOVEMENT, -10.0f);
                    }
                })
        );
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide) {
            attackAnimationState.animateWhen(true, tickCount);
        }
     }
}
