package com.site21.bittermelon.common.content.items.clownhammer;

import com.site21.bittermelon.init.neoforge.BitterParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import net.minecraft.core.particles.ParticleOptions;

import java.util.List;

public class ClownHammer extends Item {
    private static final List<ParticleOptions> PARTICLES = List.of(
            BitterParticles.BOOM.get(),
            BitterParticles.KAPOW.get()
    );

    public ClownHammer(Properties properties) {
        super(properties);
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target.level() instanceof ServerLevel serverLevel) {
            removeEntity(target, serverLevel);
        }
    }

    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getHand() == InteractionHand.MAIN_HAND
                && event.getEntity().getMainHandItem().getItem() instanceof ClownHammer
                && event.getTarget() instanceof LivingEntity target
                && target.isAlive()
                && target.level() instanceof ServerLevel serverLevel) {
            event.setCanceled(true);
            removeEntity(target, serverLevel);
        }
    }

    private static void removeEntity(LivingEntity target, ServerLevel serverLevel) {
        ParticleOptions particle = PARTICLES.get(serverLevel.getRandom().nextInt(PARTICLES.size()));
        serverLevel.sendParticles(particle, target.getX(), target.getY() + 2.0, target.getZ(), 1, 0, 0, 0, 0);
        target.discard();
    }
}