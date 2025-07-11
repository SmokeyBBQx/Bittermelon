package com.site21.bittermelon.content.entities.implementations.scp1507;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.entities.ai.behavior.needs.Need;
import com.site21.bittermelon.content.entities.base.BitterMob;
import com.site21.bittermelon.content.entities.base.NeedsStat;
import com.site21.bittermelon.content.entities.base.StatConfig;
import com.site21.bittermelon.content.entities.implementations.chicken.Chicken;
import com.site21.bittermelon.content.medical.factory.Anatomy;
import com.site21.bittermelon.init.neoforge.BitterActivity;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import org.jetbrains.annotations.NotNull;

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
    protected Map<NeedsStat, StatConfig> initializeStats() {
        return Map.of(
                NeedsStat.SOCIALIZATION, createStatConfig(0.001f),
                NeedsStat.MOVEMENT, createStatConfig(0.001f),
                NeedsStat.STRESS, createStatConfig(-0.0005f),
                NeedsStat.ANGER, createStatConfig(0f)
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
                        scp1507.modifyStat(NeedsStat.MOVEMENT, -10.0f);
                    }
                })
        );
    }



    @Override
    protected void customServerAiStep() {
        tickBrain(this);
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide) {
            attackAnimationState.animateWhen(true, tickCount);
        }
     }

    @Override
    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendEntityBrain(this);
    }

    @Override
    protected @NotNull SmartBrainProvider<SCP1507> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    public List<Need<SCP1507>> getNeeds() {
        return List.of();
    }
}
