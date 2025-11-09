package com.site21.bittermelon.common.content.items.substance;

import com.site21.bittermelon.common.content.items.base.ItemWeight;
import com.site21.bittermelon.init.neoforge.BitterSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
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

public class PowderedSubstanceItem extends SubstanceContainerItem {
    protected final int SNORT_RATE = 20;

    public PowderedSubstanceItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        playSnortSound(level, player.getOnPos());
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return SNORT_RATE;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        if (entity instanceof Player player) {
            if (!level.isClientSide()) {
                Component smell = getSmellMessageComponent(stack);
                if (!smell.getString().isBlank()) {
                    player.sendSystemMessage(smell);
                }
            }
        }

        stack = consumeSubstances(stack, 2, entity);
        if (getTotalVolume(stack) <= 0) {
            return ItemStack.EMPTY;
        }

        return stack;
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return false;
    }

    public static int getTextureLevel(@NotNull ItemStack stack) {
        if (stack.getItem() instanceof SubstanceContainerItem item) {
            float volume = item.getTotalVolume(stack);

            if (volume < 5) {
                return 1;
            } else if (volume < 10) {
                return 2;
            } else if (volume < 15) {
                return 3;
            } else if (volume > 15) {
                return 4;
            }
        }

        return 4;
    }

    private void playSnortSound(@NotNull Level level, BlockPos pos) {
        level.playSound(null, pos,
                BitterSounds.SNORT.get(), SoundSource.PLAYERS, 0.5F,
                level.getRandom().nextFloat() * 0.1F + 0.9F);
    }

    public @NotNull SoundEvent getEatingSound() {
        return SoundEvents.EMPTY;
    }
}
