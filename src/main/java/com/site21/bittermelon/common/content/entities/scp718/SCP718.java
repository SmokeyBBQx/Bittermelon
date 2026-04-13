package com.site21.bittermelon.common.content.entities.scp718;

import com.site21.bittermelon.common.content.entities.fluidprojectile.FluidProjectile;
import com.site21.bittermelon.common.systems.ai.behavior.attack.InduceRage;
import com.site21.bittermelon.common.systems.ai.behavior.attack.InduceStress;
import com.site21.bittermelon.common.systems.substance.SubstanceMixture;
import com.site21.bittermelon.common.systems.substance.SubstanceStack;
import com.site21.bittermelon.init.custom.Substances;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SCP718 extends PathfinderMob implements SmartBrainOwner<SCP718> {
    public SCP718(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 0.1).add(Attributes.MOVEMENT_SPEED, 0.1);
    }

    @Override
    public List<? extends ExtendedSensor<? extends SCP718>> getSensors() {
        return List.of(
                new NearbyPlayersSensor<>(),
                new NearbyLivingEntitySensor<>()
        );
    }

    @Override
    public BrainActivityGroup<? extends SCP718> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new LookAtTarget<>(),
                new MoveToWalkTarget<>()
        );
    }

    @Override
    public BrainActivityGroup<? extends SCP718> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new FirstApplicableBehaviour<>(
                        new SetPlayerLookTarget<>(),
                        new SetRandomLookTarget<>()
                ),
                new TargetOrRetaliate<>()
        );
    }

    @Override
    public BrainActivityGroup<? extends SCP718> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>(),
                new InduceStress<>(),
                new InduceRage<>().cooldownFor(entity -> 20)
        );
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        if (deathTime <= 1) {
            level().playSound(null, blockPosition(), SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.HOSTILE, 0.5f, 2.0f);
            summonFluid();
        }
    }

    private void summonFluid() {
        SubstanceMixture substanceMixture = new SubstanceMixture();
        substanceMixture.updateSubstanceNoUpdate(new SubstanceStack(Substances.EYEBALL_FLUID.get(), 100));

        float speed = 0.5f;
        float inaccuracy = 1.0f;

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;

                    FluidProjectile projectile = new FluidProjectile(level(), substanceMixture);
                    projectile.setPos(getX(), getEyeY(), getZ());

                    double len = Math.sqrt(x * x + y * y + z * z);
                    float vx = (float)(x / len);
                    float vy = (float)(y / len);
                    float vz = (float)(z / len);

                    projectile.shoot(vx, vy, vz, speed, inaccuracy);
                    level().addFreshEntity(projectile);
                }
            }
        }
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    protected void customServerAiStep(@NotNull ServerLevel level) {
        super.customServerAiStep(level);
        tickBrain(this);
    }

    @Override
    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendEntityBrain(this);
    }

    @Override
    protected @NotNull SmartBrainProvider<SCP718> brainProvider() {
        return new SmartBrainProvider<>(this);
    }
}
