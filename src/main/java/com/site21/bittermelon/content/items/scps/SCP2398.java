package com.site21.bittermelon.content.items.scps;

import com.site21.bittermelon.content.entities.miscellaneous.ThrownItemProjectile;
import com.site21.bittermelon.content.items.base.BaseItem;
import com.site21.bittermelon.content.items.base.ItemWeight;
import com.site21.bittermelon.init.neoforge.BitterItemTags;
import com.site21.bittermelon.init.neoforge.BitterItems;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterSounds.BAT_IMPACT;

public class SCP2398 extends BaseItem {
    private static final int COOLDOWN_TICKS = 40;
    private static final int USE_DURATION_TICKS = 20;

    public SCP2398(Properties properties, int width, int height, ItemWeight itemWeight) {
        super(properties, width, height, itemWeight);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity livingEntity) {
        return USE_DURATION_TICKS;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        ItemStack itemStack = player.getItemInHand(usedHand);
        InteractionHand otherHand;

        if (usedHand == InteractionHand.MAIN_HAND) {
            otherHand = InteractionHand.OFF_HAND;
        } else {
            otherHand = InteractionHand.MAIN_HAND;
        }

        ItemStack otherHandStack = player.getItemInHand(otherHand);

        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(itemStack);
        }

        if (otherHandStack.is(BitterItemTags.BASEBALL) || otherHandStack.is(BitterItems.BASEBALL.get())) {
            player.startUsingItem(usedHand);
            return InteractionResultHolder.consume(itemStack);
        }

        return InteractionResultHolder.fail(itemStack);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity livingEntity) {
        if (livingEntity instanceof Player player) {
            InteractionHand otherHand = player.getUsedItemHand() == InteractionHand.MAIN_HAND
                    ? InteractionHand.OFF_HAND
                    : InteractionHand.MAIN_HAND;

            ItemStack otherHandStack = player.getItemInHand(otherHand);

            if (otherHandStack.is(BitterItemTags.BASEBALL) || otherHandStack.is(BitterItems.BASEBALL.get())) {
                SCP2398Projectile projectile = new SCP2398Projectile(level, player, otherHandStack.copy());
                projectile.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
                projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 4.5F, 1.0F);
                player.level().addFreshEntity(projectile);
                otherHandStack.shrink(1);
                level.playSound(null, player.getOnPos(), BAT_IMPACT.get(), SoundSource.PLAYERS, 2, 1);

                player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
            }
        }

        return stack;
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity livingEntity, int timeLeft) {
        if (getUseDuration(stack, livingEntity) - timeLeft >= USE_DURATION_TICKS / 2) {
            if (livingEntity instanceof Player player) {
                InteractionHand otherHand = player.getUsedItemHand() == InteractionHand.MAIN_HAND
                        ? InteractionHand.OFF_HAND
                        : InteractionHand.MAIN_HAND;

                ItemStack otherHandStack = player.getItemInHand(otherHand);

                if (otherHandStack.is(BitterItemTags.BASEBALL) || otherHandStack.is(BitterItems.BASEBALL.get())) {
                    SCP2398Projectile projectile = new SCP2398Projectile(level, player, otherHandStack.copy());
                    float powerFactor = (float)(getUseDuration(stack, livingEntity) - timeLeft) / USE_DURATION_TICKS;
                    projectile.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
                    projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 4.5F * powerFactor, 1.0F);
                    player.level().addFreshEntity(projectile);
                    otherHandStack.shrink(1);
                    level.playSound(null, player.getOnPos(), BAT_IMPACT.get(), SoundSource.PLAYERS, 2, 1);

                    player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
                }
            }
        }
    }

    @Override
    public void projectileHitEntity(ItemStack stack, @NotNull Entity entity, @NotNull DamageSources damageSources, ThrownItemProjectile thrownItemProjectile, Entity owner) {
        if (entity instanceof LivingEntity livingEntity) {
            explodeEntity(stack, livingEntity, owner);
            shootExplosiveProjectile(stack, livingEntity);
        }
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        attacker.level().playSound(null, attacker.getOnPos(), BAT_IMPACT.get(), SoundSource.PLAYERS, 2, 1);
        explodeEntity(stack, target, attacker);

        if (attacker instanceof Player player) {
            player.getCooldowns().addCooldown(this, COOLDOWN_TICKS);
        }

        return true;
    }

    private void explodeEntity(@NotNull ItemStack stack, @NotNull LivingEntity target, Entity attacker) {
        float explosionScaleFactor = (float) target.getHitbox().getSize();

        target.level().explode(attacker, target.getX(), target.getY(), target.getZ(),
                2.0f * explosionScaleFactor,
                Level.ExplosionInteraction.MOB);
    }

    private void shootExplosiveProjectile(ItemStack stack, @NotNull LivingEntity target) {
        Level level = target.level();

        if (!level.isClientSide) {
            float explosionScaleFactor = (float) target.getHitbox().getSize();
            float explosionPower = 2.0f * explosionScaleFactor;

            ItemStack projectileStack = stack.copy();
            projectileStack.setCount(1);

            ThrownItemProjectile projectile = new ThrownItemProjectile(
                    level,
                    target.getX(),
                    target.getY(),
                    target.getZ(),
                    stack
            );

            double angle = level.random.nextDouble() * Math.PI * 2;
            double upwardBias = level.random.nextDouble() * 0.8;
            double horizontalScale = 0.4 + (level.random.nextDouble() * 0.6);


            Vec3 direction = new Vec3(
                    Math.cos(angle) * horizontalScale,
                    upwardBias,
                    Math.sin(angle) * horizontalScale
            ).normalize();

            float speed = 0.5F + (explosionPower * 0.3F);

            projectile.shoot(
                    direction.x,
                    direction.y,
                    direction.z,
                    speed,
                    1.0F
            );

            level.addFreshEntity(projectile);
        }
    }
}
