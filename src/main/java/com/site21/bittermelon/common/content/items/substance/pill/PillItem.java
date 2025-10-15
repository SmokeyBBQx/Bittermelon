package com.site21.bittermelon.common.content.items.substance.pill;

import com.site21.bittermelon.common.content.items.base.ItemWeight;
import com.site21.bittermelon.common.content.items.substance.SubstanceContainerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class PillItem extends SubstanceContainerItem {
    public PillItem(Properties properties, int width, int height, ItemWeight itemWeight) {
        super(properties, width, height, itemWeight);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return (int) (getTotalVolume(stack) * 2);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        playBurpSound(level, entity.getOnPos());
        consumeSubstances(stack, getTotalAmount(stack), entity);

        return ItemStack.EMPTY;
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return false;
    }

    private void playBurpSound(@NotNull Level level, BlockPos pos) {
        level.playSound(null, pos,
                SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F,
                level.getRandom().nextFloat() * 0.1F + 0.9F);
    }
}
