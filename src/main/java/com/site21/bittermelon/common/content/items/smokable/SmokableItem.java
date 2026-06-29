package com.site21.bittermelon.common.content.items.smokable;

import com.site21.bittermelon.common.content.items.substance.SubstanceContainerItem;
import com.site21.bittermelon.common.systems.component.Smokable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.SMOKABLE;

public class SmokableItem extends SubstanceContainerItem {

    public SmokableItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        return getSmokable(player.getItemInHand(hand)).use(level, player, hand);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        return getSmokable(context.getItemInHand()).useOn(context);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        return getSmokable(stack).finishUsingItem(stack, level, entity);
    }

    @Override
    public boolean releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity, int timeLeft) {
        return getSmokable(stack).releaseUsing(stack, level, entity, timeLeft);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return getSmokable(stack).getUseDuration(stack, entity);
    }

    @Override
    public @NotNull ItemUseAnimation getUseAnimation(@NotNull ItemStack stack) {
        return getSmokable(stack).getUseAnimation(stack);
    }

    @Override
    public boolean onEntityItemUpdate(@NotNull ItemStack stack, @NotNull ItemEntity entity) {
        return getSmokable(stack).onEntityItemUpdate(stack, entity);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull ServerLevel level, @NotNull Entity entity, @Nullable EquipmentSlot slot) {
        getSmokable(stack).inventoryTick(stack, level, entity, slot);
    }

    public Smokable getSmokable(@NotNull ItemStack stack) {
        return stack.getOrDefault(SMOKABLE, Smokable.DEFAULT);
    }
}
