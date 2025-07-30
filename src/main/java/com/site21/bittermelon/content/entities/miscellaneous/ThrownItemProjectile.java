package com.site21.bittermelon.content.entities.miscellaneous;

import com.site21.bittermelon.content.items.base.BaseItem;
import com.site21.bittermelon.content.items.scps.scp2398.SCP2398;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterEntities.THROWN_ITEM_PROJECTILE;
import static com.site21.bittermelon.init.neoforge.BitterItems.SCP_2398;
import static net.minecraft.world.item.Items.SNOWBALL;

public class ThrownItemProjectile extends ThrowableItemProjectile {
    private int bounceCount = 0;
    private final int maxBounces;
    private final float energyLossOnBounce;
    private static final float BASE_GRAVITY = 0.03F;
    private static final float MIN_BOUNCE_VELOCITY = 0.1f;

    public ThrownItemProjectile(EntityType<? extends ThrownItemProjectile> entityType, Level level) {
        super(entityType, level);
        energyLossOnBounce = 0.7f;
        maxBounces = 50;
    }

    public ThrownItemProjectile(Level level, LivingEntity player, ItemStack stack, float energyLossOnBounce, int maxBounces) {
        super(THROWN_ITEM_PROJECTILE.get(), player, level);
        this.setItem(stack);
        this.energyLossOnBounce = energyLossOnBounce;
        this.maxBounces = maxBounces;
    }

    public ThrownItemProjectile(Level pLevel, double pX, double pY, double pZ, ItemStack stack, float energyLossOnBounce, int maxBounces) {
        super(THROWN_ITEM_PROJECTILE.get(), pX, pY, pZ, pLevel);
        this.setItem(stack);
        this.energyLossOnBounce = energyLossOnBounce;
        this.maxBounces = maxBounces;
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return SNOWBALL;
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) return;

        Vec3 currentPos = position();
        Vec3 velocity = getDeltaMovement();
        Vec3 nextPos = currentPos.add(velocity);

        // Ray trace from current position to next position
        BlockHitResult hitResult = level().clip(
                new ClipContext(
                        currentPos,
                        nextPos,
                        ClipContext.Block.COLLIDER,
                        ClipContext.Fluid.NONE,
                        this
                )
        );

        // If we would hit a block, trigger the collision early
        if (hitResult.getType() != HitResult.Type.MISS) {
            // Move to just before the collision point
            Vec3 hitPos = hitResult.getLocation();
            Vec3 direction = nextPos.subtract(currentPos).normalize();
            Vec3 safePos = hitPos.subtract(direction.scale(0.1)); // Back up slightly

            setPos(safePos);
            onHitBlock(hitResult);
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        super.onHitBlock(result);

        if (!this.level().isClientSide) {
            BlockPos pos = result.getBlockPos();
            BlockState state = level().getBlockState(pos);

            // Block interactions
            if (state.getBlock() instanceof BellBlock block) {
                block.attemptToRing(level(), pos, result.getDirection());
            } else if (state.getBlock() instanceof ButtonBlock block) {
                block.press(state, level(), pos, null);
            }

            // Bounce sound
            level().playSound(null, pos, SoundEvents.STONE_FALL, SoundSource.PLAYERS, 2, 1);

            if (bounceCount < maxBounces) {
                Vec3 newVelocity = getNewVelocity(result);

                // Ensure above minimum velocity threshold
                if (newVelocity.length() > 0.25f) {
                    this.setDeltaMovement(newVelocity);
                    this.bounceCount++;
                    return; // Continue bouncing
                }
            }

            if (this.getItem().getItem() instanceof BaseItem item) {
                item.projectileHitBlock(this.getItem(), this.level(), getOnPos());
            } else {
                spawnAtLocation(this.getItem());
            }
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    private @NotNull Vec3 getNewVelocity(@NotNull BlockHitResult result) {
        // Reflection Formula {w = v - 2 * (v∙n) * n}

        // v
        Vec3 velocity = new Vec3(this.getDeltaMovement().toVector3f());
        // n
        Vec3 normal = new Vec3(result.getDirection().getStepX(), result.getDirection().getStepY(), result.getDirection().getStepZ());
        // v∙n
        double dot = velocity.dot(normal);
        // w
        Vec3 newVelocity = new Vec3(
                velocity.x - 2 * dot * normal.x,
                velocity.y - 2 * dot * normal.y,
                velocity.z - 2 * dot * normal.z
        );

        // Energy loss
        newVelocity = newVelocity.scale(energyLossOnBounce);

        double maxVelocity = 3.0;
        if (newVelocity.length() > maxVelocity) {
            newVelocity = newVelocity.normalize().scale(maxVelocity);
        }

        // Ensure minimum y velocity
        // Check if vertical bounce is too small and what face of the block we're hitting
        if (Math.abs(newVelocity.y) < 0.2 && result.getDirection().getStepY() != 0) {
            // For floor collision, Math.signum will make it bounce upwards and vice versa for ceiling collision.
            newVelocity = new Vec3(newVelocity.x, 0.2 * Math.signum(result.getDirection().getStepY()), newVelocity.z);
        }

        return newVelocity;
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();

        if (!this.level().isClientSide) {
            // v
            Vec3 velocity = new Vec3(this.getDeltaMovement().toVector3f());
            // n
            Vec3 normal = new Vec3(getDirection().getStepX(), getDirection().getStepY(), getDirection().getStepZ());
            // v∙n
            double dot = velocity.dot(normal);
            // w
            Vec3 newVelocity = new Vec3(
                    velocity.x - 2 * dot * normal.x,
                    velocity.y - 2 * dot * normal.y,
                    velocity.z - 2 * dot * normal.z
            );

            // Energy loss
            newVelocity.scale(energyLossOnBounce);

            // Ensure minimum y velocity
            // Check if vertical bounce is too small and what face of the block we're hitting
            if (Math.abs(newVelocity.y) < 0.2 && getMotionDirection().getStepY() != 0) {
                // For floor collision, Math.signum will make it bounce upwards and vice versa for ceiling collision.
                newVelocity = new Vec3(newVelocity.x, 0.2 * Math.signum(getMotionDirection().getStepY()), newVelocity.z);
            }

            this.setDeltaMovement(newVelocity);
            if (this.getItem().getItem() instanceof BaseItem item) {
                item.projectileHitEntity(this.getItem(), entity, this.damageSources(), this, this.getOwner(), velocity);
            } else {
                float dmg = 1;
                dmg *= this.getItem().getCount();
                dmg /= this.getItem().getMaxStackSize() / 4f;
                entity.hurt(this.damageSources().thrown(this, this.getOwner()), dmg);
            }
        }

        if (this.getItem().getItem() instanceof SCP2398) {
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }

    @Override
    protected double getDefaultGravity() {
        if (this.getItem().getItem() instanceof BaseItem item) {
            return BASE_GRAVITY + (float) item.getItemWeight().value / 100;
        }
        return BASE_GRAVITY;
    }

    @Override
    public boolean hurt(@NotNull net.minecraft.world.damagesource.DamageSource source, float amount) {
        if (source.is(DamageTypes.EXPLOSION) ||
                source.is(DamageTypes.PLAYER_EXPLOSION) ||
                source.is(DamageTypes.BAD_RESPAWN_POINT)) {
            return false;
        }

        if (source.getEntity() instanceof Player player && !level().isClientSide) {
            Vec3 hitDirection = player.getLookAngle();

            double hitStrength = 0.8 + (player.isSprinting() ? 0.3 : 0) + (player.getMainHandItem().is(SCP_2398.get()) ? 1.0f : 0);

            this.setDeltaMovement(
                    hitDirection.x * hitStrength,
                    hitDirection.y * hitStrength,
                    hitDirection.z * hitStrength
            );

            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.PLAYER_ATTACK_KNOCKBACK, SoundSource.PLAYERS,
                    0.8F, 0.8F + this.random.nextFloat() * 0.4F);

            return true;
        }

        return super.hurt(source, amount);
    }

    @Override
    public boolean isPickable() {
        return true;
    }
}

