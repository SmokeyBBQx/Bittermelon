package com.site21.bittermelon.common.content.items.taser;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.*;
import static com.site21.bittermelon.init.neoforge.BitterItems.TASER_CARTRIDGE;
import static com.site21.bittermelon.init.neoforge.BitterSounds.TASER_RELOAD;
import static com.site21.bittermelon.init.neoforge.BitterSounds.TASER_SHOOT;

public class TaserItem extends Item {
    public TaserItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity livingEntity) {
        return 30;
    }

    @Override
    public @NotNull ItemUseAnimation getUseAnimation(@NotNull ItemStack stack) {
        return ItemUseAnimation.BOW;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (level.isClientSide()) return InteractionResult.PASS;

        List<Integer> taseProbes = stack.getOrDefault(TASE_PROBES, List.of());
        if (!taseProbes.isEmpty()) {
            if (player.isShiftKeyDown()) {
                for (Integer i : taseProbes) {
                    if (level.getEntity(i) instanceof TaserProjectile projectile) {
                        projectile.remove(Entity.RemovalReason.DISCARDED);
                    }
                }

                stack.remove(TASE_PROBES);
                level.playSound(null, player.getOnPos(), SoundEvents.CROSSBOW_LOADING_START.value(), SoundSource.PLAYERS, 1, 2);
                return InteractionResult.SUCCESS;
            } else {
                player.sendSystemMessage(Component
                                .literal("Prongs are still attached to the target. Shift + Right Click to remove.")
                                .withStyle(ChatFormatting.RED)
                );
                return InteractionResult.PASS;
            }
        }

        if (stack.getOrDefault(AMMO, 0) > 0) {
            if (stack.getOrDefault(RELOAD_TIMER, 0) > 0) {
                return InteractionResult.PASS;
            }
            player.startUsingItem(usedHand);

            shootProjectile(player, stack);

            stack.set(AMMO, 0);
            level.playSound(null, player.getOnPos(), TASER_SHOOT.value(), SoundSource.PLAYERS);
            return InteractionResult.CONSUME;
        } else if (player.getInventory().contains(new ItemStack(TASER_CARTRIDGE.get()))) {
            return ItemUtils.startUsingInstantly(level, player, usedHand);
        }
        return InteractionResult.PASS;
    }

    private void shootProjectile(@NotNull Player player, @NotNull ItemStack stack) {
        TaserProjectile projectile = new TaserProjectile(player.level());
        TaserProjectile projectile1 = new TaserProjectile(player.level());

        projectile.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
        projectile.setOwner(player);
        projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5f, 2.0f);

        projectile1.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
        projectile1.setOwner(player);
        projectile1.shootFromRotation(player, player.getXRot() + 0.5f, player.getYRot(), 0.0F, 1.5f, 4.0f);

        player.level().addFreshEntity(projectile);
        player.level().addFreshEntity(projectile1);

        stack.set(TASE_PROBES, List.of(projectile.getId(), projectile1.getId()));
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity livingEntity) {
        if (livingEntity instanceof Player player) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack slot = player.getInventory().getItem(i);
                if (slot.is(TASER_CARTRIDGE)) {
                    slot.shrink(1);
                    stack.set(AMMO, 1);
                    stack.set(RELOAD_TIMER, 10);
                    level.playSound(null, player.getOnPos(), TASER_RELOAD.value(), SoundSource.PLAYERS);
                    return stack;
                }
            }
        }
        return stack;
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull ServerLevel level, @NotNull Entity entity, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, level, entity, slot);

        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
            int reloadTimer = stack.getOrDefault(RELOAD_TIMER, 0);
            if (reloadTimer > 0) {
                stack.set(RELOAD_TIMER, reloadTimer - 1);
            }
        }
    }
}
