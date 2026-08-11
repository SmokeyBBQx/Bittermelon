package com.site21.bittermelon.common.content.items.gun;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class Handgun extends Item {
    public Handgun(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        HitResult hitResult = ProjectileUtil.getHitResultOnViewVector(player, (_) -> true, 32);
        ItemStack stack = player.getItemInHand(hand);

        if (hitResult instanceof EntityHitResult entityHitResult && level instanceof ServerLevel serverLevel) {
            DamageSource source = stack.getDamageSource(player, () -> player.damageSources().playerAttack(player));
            entityHitResult.getEntity().hurtServer(serverLevel, source, 0);
        }

        level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.PLAYERS, 1.0f, 2.0f);
        player.getCooldowns().addCooldown(player.getItemInHand(hand), 10);
        return InteractionResult.CONSUME;
    }
}
