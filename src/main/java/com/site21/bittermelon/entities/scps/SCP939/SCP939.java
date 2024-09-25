package com.site21.bittermelon.entities.scps.SCP939;

import com.mojang.serialization.Dynamic;
import com.site21.bittermelon.entities.base.BaseEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.DynamicGameEventListener;
import net.minecraft.world.level.gameevent.EntityPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

public class SCP939 extends Monster implements VibrationSystem {
    private final DynamicGameEventListener<VibrationSystem.Listener> dynamicGameEventListener;
    private final VibrationSystem.User vibrationUser;
    private VibrationSystem.Data vibrationData;

    public SCP939(EntityType<? extends Mob> entityType, Level level) {
        super((EntityType<? extends Monster>) entityType, level);
        this.vibrationUser = new SCP939.VibrationUser();
        this.vibrationData = new VibrationSystem.Data();
        this.dynamicGameEventListener = new DynamicGameEventListener<>(new VibrationSystem.Listener(this));
        this.xpReward = 5;
        this.getNavigation().setCanFloat(true);
        this.setPathfindingMalus(PathType.UNPASSABLE_RAIL, 0.0F);
        this.setPathfindingMalus(PathType.DAMAGE_OTHER, 8.0F);
        this.setPathfindingMalus(PathType.POWDER_SNOW, 8.0F);
        this.setPathfindingMalus(PathType.LAVA, 8.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, 0.0F);
        this.setPathfindingMalus(PathType.DANGER_FIRE, 0.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 150.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3F)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5)
                .add(Attributes.ATTACK_DAMAGE, 30.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        RegistryOps<Tag> registryops = this.registryAccess().createSerializationContext(NbtOps.INSTANCE);
        if (compound.contains("listener", 10)) {
            VibrationSystem.Data.CODEC
                    .parse(registryops, compound.getCompound("listener"))
                    .resultOrPartial(p_351914_ -> System.out.println("Failed to parse vibration listener for SCP-939: " + p_351914_))
                    .ifPresent(p_281093_ -> this.vibrationData = p_281093_);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        RegistryOps<Tag> registryops = this.registryAccess().createSerializationContext(NbtOps.INSTANCE);
        VibrationSystem.Data.CODEC
                .encodeStart(registryops, this.vibrationData)
                .resultOrPartial(p_351915_ -> System.out.println("Failed to encode vibration listener for SCP-939: " + p_351915_))
                .ifPresent(p_219418_ -> compound.put("listener", p_219418_));
    }

    @Override
    public boolean dampensVibrations() {
        return true;
    }

    @javax.annotation.Nullable
    @Override
    public LivingEntity getTarget() {
        return this.getTargetFromBrain();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

//    public Optional<Object> getEntityAngryAt() {
//
//    }

    @Override
    protected Brain.@NotNull Provider<SCP939> brainProvider() {
        return SCP939AI.brainProvider();
    }

    @Override
    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        return SCP939AI.makeBrain(this, this.brainProvider().makeBrain(dynamic));
    }

    @Contract("null->false")
    public boolean canTargetEntity(LivingEntity entity) {
        return entity instanceof LivingEntity livingentity
                && this.level() == entity.level()
                && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity)
                && !this.isAlliedTo(entity)
                && livingentity.getType() != EntityType.ARMOR_STAND
                && livingentity.getType() != EntityType.WARDEN
                && !livingentity.isInvulnerable()
                && !livingentity.isDeadOrDying()
                && this.level().getWorldBorder().isWithinBounds(livingentity.getBoundingBox());
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        boolean flag = super.hurt(source, amount);
        if (!this.level().isClientSide && !this.isNoAi()) {
            Entity entity = source.getEntity();
            if (this.brain.getMemory(MemoryModuleType.ATTACK_TARGET).isEmpty()
                    && entity instanceof LivingEntity livingentity
                    && (source.isDirect() || this.closerThan(livingentity, 5.0))) {
                this.setAttackTarget(livingentity);
            }
        }

        return flag;
    }

    public void setAttackTarget(LivingEntity attackTarget) {
        this.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, attackTarget);
        this.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
    }

    @Override
    public @NotNull Data getVibrationData() {
        return this.vibrationData;
    }

    @Override
    public @NotNull User getVibrationUser() {
        return this.vibrationUser;
    }

    @Override
    public void tick() {
        if (this.level() instanceof ServerLevel serverlevel) {
            VibrationSystem.Ticker.tick(serverlevel, this.vibrationData, this.vibrationUser);
        }

        super.tick();


    }

    @Override
    protected void customServerAiStep() {
        ServerLevel serverlevel = (ServerLevel) this.level();
        serverlevel.getProfiler().push("wardenBrain");
        this.getBrain().tick(serverlevel, this);
        this.level().getProfiler().pop();
        SCP939AI.updateActivity(this);
    }

    @Override
    public @NotNull Brain<SCP939> getBrain() {
        return (Brain<SCP939>) super.getBrain();
    }

    @Override
    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendEntityBrain(this);
    }

    @Override
    public void updateDynamicGameEventListener(@NotNull BiConsumer<DynamicGameEventListener<?>, ServerLevel> listenerConsumer) {
        if (this.level() instanceof ServerLevel serverlevel) {
            listenerConsumer.accept(this.dynamicGameEventListener, serverlevel);
        }
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new GroundPathNavigation(this, level) {
            @Override
            protected @NotNull PathFinder createPathFinder(int i) {
                this.nodeEvaluator = new WalkNodeEvaluator();
                this.nodeEvaluator.setCanPassDoors(true);
                return new PathFinder(this.nodeEvaluator, i) {
                    @Override
                    protected float distance(@NotNull Node entity1, @NotNull Node entity2) {
                        return entity1.distanceToXZ(entity2);
                    }
                };
            }
        };
    }


    class VibrationUser implements VibrationSystem.User {
        private final PositionSource positionSource = new EntityPositionSource(SCP939.this, SCP939.this.getEyeHeight());

        @Override
        public int getListenerRadius() {
            return 16;
        }

        @Override
        public @NotNull PositionSource getPositionSource() {
            return this.positionSource;
        }

        @Override
        public boolean canReceiveVibration(@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull Holder<GameEvent> gameEvent, GameEvent.@NotNull Context context) {
            return true;

            // TODO: Add checks for when SCP-939 is allowed to listen
        }

        @Override
        public void onReceiveVibration(@NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull Holder<GameEvent> gameEvent, @Nullable Entity entity, @Nullable Entity playerEntity, float distance) {
            if (!SCP939.this.isDeadOrDying()) {
                SCP939.this.brain.setMemoryWithExpiry(MemoryModuleType.VIBRATION_COOLDOWN, Unit.INSTANCE, 40L);
                level.broadcastEntityEvent(SCP939.this, (byte) 61);
                if (entity instanceof LivingEntity livingEntity) {
                    if (SCP939.this.closerThan(livingEntity, 32)) {
                        if (SCP939.this.canTargetEntity(livingEntity)) {
                            SCP939AI.setDisturbanceLocation(SCP939.this, pos);
                        }
                    }
                }
            }
        }
    }
}
