package com.site21.bittermelon.content.entities.implementations.scp131;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.entities.ai.behavior.needs.Need;
import com.site21.bittermelon.content.entities.base.BitterMob;
import com.site21.bittermelon.content.entities.base.NeedsStat;
import com.site21.bittermelon.content.entities.base.StatConfig;
import com.site21.bittermelon.content.medical.factory.Anatomy;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.CatVariantTags;
import net.minecraft.tags.StructureTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
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

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class SCP131 extends BitterMob<SCP131> {
    public static final int MAX_VARIANTS = 2;
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(SCP131.class, EntityDataSerializers.INT);

    public SCP131(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level, 1);
    }

    @Override
    protected Map<NeedsStat, StatConfig> initializeStats() {
        return Map.of();
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
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, 0);
    }

    public int getVariant() {
        return entityData.get(VARIANT);
    }

    public void setVariant(int variant) {
        entityData.set(VARIANT, Mth.clamp(variant, 0, MAX_VARIANTS - 1));
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", getVariant());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty,
                                        @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        setVariant(level.getRandom().nextInt(MAX_VARIANTS));
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
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
