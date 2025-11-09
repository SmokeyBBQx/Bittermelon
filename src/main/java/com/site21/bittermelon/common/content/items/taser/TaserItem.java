package com.site21.bittermelon.common.content.items.taser;

import com.site21.bittermelon.common.content.items.base.BitterItem;
import com.site21.bittermelon.common.content.items.base.ItemWeight;
import com.site21.bittermelon.common.content.items.gun.IGunItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.*;
import static com.site21.bittermelon.init.neoforge.BitterItems.TASER_CARTRIDGE;
import static com.site21.bittermelon.init.neoforge.BitterSounds.TASER_RELOAD;
import static com.site21.bittermelon.init.neoforge.BitterSounds.TASER_SHOOT;

public class TaserItem extends BitterItem implements IGunItem {
    public TaserItem(Properties properties, int width, int height, ItemWeight itemWeight) {
        super(properties);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity livingEntity) {
        return 30;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        if (level.isClientSide) return InteractionResultHolder.fail(stack);

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
                return InteractionResultHolder.success(stack);
            } else {
                player.displayClientMessage(Component.literal("Prongs are still attached to the target. Shift + Right Click to remove.").withStyle(ChatFormatting.RED), true);
                return InteractionResultHolder.fail(stack);
            }
        }

        if (stack.getOrDefault(AMMO, 0) > 0) {
            if (stack.getOrDefault(RELOAD_TIMER, 0) > 0) {
                return InteractionResultHolder.fail(stack);
            }
            player.startUsingItem(usedHand);

            shootProjectile(player, stack);

            stack.set(AMMO, 0);
            level.playSound(null, player.getOnPos(), TASER_SHOOT.get(), SoundSource.PLAYERS);
            return InteractionResultHolder.consume(stack);
        } else if (player.getInventory().contains(new ItemStack(TASER_CARTRIDGE.get()))) {
            return ItemUtils.startUsingInstantly(level, player, usedHand);
        }
        return InteractionResultHolder.fail(stack);
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
                    level.playSound(null, player.getOnPos(), TASER_RELOAD.get(), SoundSource.PLAYERS);
                    return stack;
                }
            }
        }
        return stack;
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        if (isSelected) {
            if (stack.getOrDefault(RELOAD_TIMER, 0) > 0) {
                stack.set(RELOAD_TIMER, stack.get(RELOAD_TIMER) - 1);
            }
        }
    }

    @Override
    public int getMaxAmmo() {
        return 1;
    }
}
