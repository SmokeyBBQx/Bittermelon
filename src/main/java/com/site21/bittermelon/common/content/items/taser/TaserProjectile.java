package com.site21.bittermelon.common.content.items.taser;

import com.site21.bittermelon.init.neoforge.BitterSounds;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

import static com.site21.bittermelon.init.neoforge.BitterEntities.TASER_PROJECTILE;
import static com.site21.bittermelon.init.neoforge.BitterItems.TASER;
import static com.site21.bittermelon.init.neoforge.BitterMobEffects.TASERED;

public class TaserProjectile extends Projectile {
    private static final EntityDataAccessor<Integer> DATA_HOOKED_ENTITY = SynchedEntityData.defineId(TaserProjectile.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_RANDOM_OFFSET = SynchedEntityData.defineId(TaserProjectile.class, EntityDataSerializers.FLOAT);
    private Entity hookedIn;
    private float randomOffset = 0;

    public TaserProjectile(EntityType<? extends TaserProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public TaserProjectile(Level level) {
        this(TASER_PROJECTILE.get(), level);
    }

    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        builder.define(DATA_HOOKED_ENTITY, 0);
        builder.define(DATA_RANDOM_OFFSET, 0f);
    }

    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> key) {
        if (DATA_HOOKED_ENTITY.equals(key)) {
            int i = getEntityData().get(DATA_HOOKED_ENTITY);
            hookedIn = i > 0 ? level().getEntity(i - 1) : null;
        } else if (DATA_RANDOM_OFFSET.equals(key)) {
            randomOffset = getEntityData().get(DATA_RANDOM_OFFSET);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (hookedIn != null && !hookedIn.isRemoved()) {
            setPos(hookedIn.getX() + randomOffset, hookedIn.getY() + hookedIn.getEyeHeight() / 2, hookedIn.getZ() + randomOffset);
            return;
        }

        Vec3 movement = this.getDeltaMovement();
        HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);

        if (hitResult.getType() != HitResult.Type.MISS) {
            this.onHit(hitResult);
        }

        this.move(MoverType.SELF, movement);
        this.updateRotation();
//        this.checkInsideBlocks();

        Vec3 currentMovement = this.getDeltaMovement();
        this.setDeltaMovement(currentMovement.scale(0.99D).subtract(0, this.getDefaultGravity(), 0));

        if (this.tickCount > 1200) {
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        Random random1 = new Random();
        if (!(result.getEntity() instanceof LivingEntity entity)) return;
        if (!(getOwner() instanceof Player player)) return;
        ItemStack taser = player.getMainHandItem();
        if (!taser.is(TASER)) return;
        setHookedIn(entity);
        setRandomOffset(random1.nextFloat(0, 0.5f));

        entity.addEffect(new MobEffectInstance(TASERED, 300, 1, false, false));
        this.level().broadcastEntityEvent(this, (byte) 3);
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        super.onHitBlock(result);
        this.setDeltaMovement(this.getDeltaMovement().normalize().scale(result.distanceTo(this)));
    }

    @Override
    public void onRemovedFromLevel() {
        super.onRemovedFromLevel();
        if (hookedIn instanceof LivingEntity livingEntity) {
            livingEntity.removeEffect(TASERED);

            ClientboundStopSoundPacket stopSoundPacket = new ClientboundStopSoundPacket(BitterSounds.TASER.getKey().identifier(), SoundSource.PLAYERS);
            AABB stopSoundArea = new AABB(livingEntity.getOnPos()).inflate(15);
            for (Player player : level().getEntitiesOfClass(Player.class, stopSoundArea)) {
                if (player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.connection.send(stopSoundPacket);
                }
            }
        }
    }

    public void setHookedIn(Entity entity) {
        this.hookedIn = entity;
        this.getEntityData().set(DATA_HOOKED_ENTITY, entity == null ? 0 : entity.getId() + 1);
    }

    public void setRandomOffset(float randomOffset) {
        this.randomOffset = randomOffset;
        this.getEntityData().set(DATA_RANDOM_OFFSET, randomOffset);
    }

    protected double getDefaultGravity() {
        return 0.03;
    }
}
