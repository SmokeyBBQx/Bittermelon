package com.site21.bittermelon.content.items.substance;

import com.site21.bittermelon.content.items.base.ItemWeight;
import com.site21.bittermelon.content.items.substance.data.SubstanceContents;
import com.site21.bittermelon.content.substance.SubstanceStack;
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

import java.util.Iterator;

public class EdibleSubstanceItem extends SubstanceContainerItem {
    protected final int CONSUME_RATE = 10;

    public EdibleSubstanceItem(Properties properties, int width, int height, ItemWeight itemWeight, int capacity) {
        super(properties, width, height, itemWeight, capacity);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        playEatingSound(level, player.getOnPos());
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return CONSUME_RATE * 2;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        if (entity instanceof Player player) {
            if (!level.isClientSide()) {
                player.sendSystemMessage(getFlavorMessageComponent(stack));
            }
        }

        playBurpSound(level, entity.getOnPos());

        return consumeSubstances(stack, CONSUME_RATE);
    }

    private void playBurpSound(@NotNull Level level, BlockPos pos) {
        level.playSound(null, pos,
                SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F,
                level.getRandom().nextFloat() * 0.1F + 0.9F);
    }

    private void playEatingSound(@NotNull Level level, BlockPos pos) {
        level.playSound(null, pos,
                SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.5F,
                level.getRandom().nextFloat() * 0.1F + 0.9F);
    }
}
