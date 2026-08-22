package com.site21.bittermelon.common.content.entities.scp718;

import com.mojang.serialization.Codec;
import com.site21.bittermelon.common.content.entities.fluidprojectile.FluidProjectile;
import com.site21.bittermelon.common.systems.ai.behavior.attack.InduceRage;
import com.site21.bittermelon.common.systems.ai.behavior.attack.InduceStress;
import com.site21.bittermelon.common.systems.substance.SubstanceMixture;
import com.site21.bittermelon.common.systems.substance.SubstanceStack;
import com.site21.bittermelon.init.custom.Substances;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.behaviour.base.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
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
    private static final int GROWTH_INTERVAL = 100;
    private static final float GROWTH_RATE = 0.01f;
    private static final EntityDataAccessor<Float> GROWTH = SynchedEntityData.defineId(SCP718.class,
            EntityDataSerializers.FLOAT);

    public SCP718(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 0.1)
                .add(Attributes.MOVEMENT_SPEED, 0.075);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(GROWTH, 0.0f);
    }

    @Override
    public List<? extends ExtendedSensor<?>> getSensors(SCP718 owner) {
        return List.of(
                new NearbyPlayersSensor<>(),
                new NearbyLivingEntitySensor<>()
        );
    }

    @Override
    public List<? extends BehaviorControl<?>> getAlwaysRunningBehaviours(SCP718 owner) {
        return List.of(
                new LookAtTarget<>(),
                new MoveToWalkTarget<>()
        );
    }

    @Override
    public List<? extends BehaviorControl<?>> getIdleBehaviours(SCP718 owner) {
        return List.of(
                new FirstApplicableBehaviour<>(
                        new SetPlayerLookTarget<>(),
                        new SetRandomLookTarget<>().cooldownFor(_ -> 500)
                ),
                new TargetOrRetaliate<>(),
                new InvalidateAttackTarget<>().invalidateIf((_, target) -> target instanceof SCP718)
        );
    }

    @Override
    public List<? extends BehaviorControl<?>> getFightingBehaviours(SCP718 owner) {
        return List.of(
                new InvalidateAttackTarget<>(),
                new SetWalkTargetToAttackTarget<>()
                        .closeEnoughDist((_, _) -> 12),
                new InduceStress<>(),
                new InduceRage<>().cooldownFor(_ -> 20)
        );
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        if (deathTime <= 1) {
            DamageSource lastDamageSource = getLastDamageSource();
            boolean killedByCommand = lastDamageSource != null && lastDamageSource.is(DamageTypes.GENERIC_KILL);

            if (!killedByCommand) {
                level().playSound(null, blockPosition(), SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.HOSTILE, 0.5f, 2.0f);
                explodeFluid(level(), getX(), getEyeY(), getZ());
            }
        }
    }

    public static void explodeFluid(Level level, double x, double y, double z) {
        SubstanceMixture substanceMixture = new SubstanceMixture();
        substanceMixture.updateSubstanceNoUpdate(new SubstanceStack(Substances.EYEBALL_FLUID.get(), 100));

        float speed = 0.5f;
        float inaccuracy = 1.0f;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dy == 0 && dz == 0) {
                        continue;
                    }

                    FluidProjectile projectile = new FluidProjectile(level, substanceMixture);
                    projectile.setPos(x, y, z);

                    double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
                    float vx = (float) (dx / len);
                    float vy = (float) (dy / len);
                    float vz = (float) (dz / len);

                    projectile.shoot(vx, vy, vz, speed, inaccuracy);
                    level.addFreshEntity(projectile);
                }
            }
        }
    }

    public float getGrowth() {
        return entityData.get(GROWTH);
    }

    public void setGrowth(float growth) {
        entityData.set(GROWTH, growth);
    }

    public void grow() {
        float growth = getGrowth();
        if (growth < 1.0f) {
            setGrowth(growth + GROWTH_RATE);
        }
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    protected void customServerAiStep(@NotNull ServerLevel level) {
        super.customServerAiStep(level);

        if (level.getGameTime() % GROWTH_INTERVAL == 0) {
            grow();
        }
    }

    @Override
    protected void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.store("Growth", Codec.FLOAT, entityData.get(GROWTH));
    }

    @Override
    protected void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        entityData.set(GROWTH, input.read("Growth", Codec.FLOAT).orElse(0.0f));
    }
}
