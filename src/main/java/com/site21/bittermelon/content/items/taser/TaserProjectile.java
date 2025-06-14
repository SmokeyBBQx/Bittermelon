package com.site21.bittermelon.content.items.taser;

import com.site21.bittermelon.client.visualeffects.screenshake.StartScreenshake;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.content.entities.miscellaneous.ThrownItemProjectile;
import com.site21.bittermelon.content.medical.compartments.FunctionType;
import com.site21.bittermelon.content.stumble.StumbleHandler;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.SHAKE_TICKS;
import static com.site21.bittermelon.init.neoforge.BitterEntities.TASER_PROJECTILE;

public class TaserProjectile extends Projectile {
    public TaserProjectile(EntityType<? extends TaserProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public TaserProjectile(Level level) {
        this(TASER_PROJECTILE.get(), level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {

    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        if (!(result.getEntity() instanceof LivingEntity entity)) return;

        if (entity.level().isClientSide) return;
        Character character = CharacterManager.get(entity.level()).getActiveCharacter(entity);
        // TODO: A way determine whether they would be affected by the taser

        StumbleHandler.stumble(entity, 600, entity.getLookAngle(), false);

        entity.setData(SHAKE_TICKS, 300);
        PacketDistributor.sendToPlayersTrackingEntity(entity, new SetShakeTicks(entity.getId(), 300));

        if (entity instanceof ServerPlayer player) {
            PacketDistributor.sendToPlayer(player, new StartScreenshake(2400, 10));
            PacketDistributor.sendToPlayer(player, new SetShakeTicks(player.getId(), 300));
        }

        this.level().broadcastEntityEvent(this, (byte) 3);
        this.discard();
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        super.onHitBlock(result);

        this.level().broadcastEntityEvent(this, (byte) 3);
        this.discard();
    }

    public void tick() {
        super.tick();
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hitresult.getType() != HitResult.Type.MISS && !EventHooks.onProjectileImpact(this, hitresult)) {
            this.hitTargetOrDeflectSelf(hitresult);
        }

        this.checkInsideBlocks();
        Vec3 vec3 = this.getDeltaMovement();
        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        this.updateRotation();
        float f;
        if (this.isInWater()) {
            for (int i = 0; i < 4; ++i) {
                float f1 = 0.25F;
                this.level().addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * (double) 0.25F, d1 - vec3.y * (double) 0.25F, d2 - vec3.z * (double) 0.25F, vec3.x, vec3.y, vec3.z);
            }

            f = 0.8F;
        } else {
            f = 0.99F;
        }

        this.setDeltaMovement(vec3.scale((double) f));
        this.applyGravity();
        this.setPos(d0, d1, d2);
    }

    protected double getDefaultGravity() {
        return 0.03;
    }
}
