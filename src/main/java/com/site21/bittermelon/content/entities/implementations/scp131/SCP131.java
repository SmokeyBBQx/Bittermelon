package com.site21.bittermelon.content.entities.implementations.scp131;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.entities.ai.behavior.needs.Need;
import com.site21.bittermelon.content.entities.base.BitterMob;
import com.site21.bittermelon.content.entities.base.NeedsStat;
import com.site21.bittermelon.content.entities.base.StatConfig;
import com.site21.bittermelon.content.medical.factory.Anatomy;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class SCP131 extends BitterMob<SCP131> {
    @Override
    protected Map<NeedsStat, StatConfig> initializeStats() {
        return Map.of();
    }

    public SCP131(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level, 1);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 4.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected Character initializeCharacter() {
        return new Character(this.uuid, "SCP-131", Anatomy.HUMAN);
    }

    @Override
    public @NotNull Vec3 handleRelativeFrictionAndCalculateMovement(@NotNull Vec3 deltaMovement, float friction) {
//        return super.handleRelativeFrictionAndCalculateMovement(deltaMovement, friction * 0.05f);
        return super.handleRelativeFrictionAndCalculateMovement(deltaMovement, friction);
    }

    @Override
    public void tick() {
        super.tick();

        if (horizontalCollision) {
            if (getDeltaMovement().x > 1 || getDeltaMovement().z > 1) {

            }
        }
    }

    @Override
    public BrainActivityGroup<? extends SCP131> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new LookAtTarget<>(),
                new MoveToWalkTarget<>()
        );
    }

    @Override
    public BrainActivityGroup<? extends SCP131> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new OneRandomBehaviour<>(
                        new SetRandomLookTarget<>(),
                        new SetPlayerLookTarget<>()
                ),
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<>()
                                .setRadius(getRandom().nextInt(1, 10)),
                        new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))
                )
        );
    }


    @Override
    public List<Need<SCP131>> getNeeds() {
        return List.of();
    }

    @Override
    public List<? extends ExtendedSensor<? extends SCP131>> getSensors() {
        return ObjectArrayList.of(new NearbyPlayersSensor<>());
    }
}
