package com.site21.bittermelon.common.content.items.screwdriver;

import com.site21.bittermelon.common.systems.component.Screwdriver;
import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ScrewdriverItem extends Item {
    private static final int USE_DURATION = 60;

    public ScrewdriverItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        Screwdriver screwdriver = stack.get(BitterDataComponents.SCREWDRIVER);
        if (screwdriver != null) {
            return screwdriver.useOn(context);
        }

        return InteractionResult.PASS;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return getScrewdriver(stack).screwDuration();
    }

    @Override
    public @NotNull ItemUseAnimation getUseAnimation(@NotNull ItemStack stack) {
        return ItemUseAnimation.BOW;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        return getScrewdriver(stack).finishUsingItem(stack, level, entity);
    }

    @Override
    public void onUseTick(@NotNull Level level, @NotNull LivingEntity entity, ItemStack stack, int remainingUseDuration) {
       getScrewdriver(stack).playScrewSound(level, entity, remainingUseDuration);
    }

    private Screwdriver getScrewdriver(@NotNull ItemStack stack) {
        return stack.get(BitterDataComponents.SCREWDRIVER) == null ? Screwdriver.DEFAULT : stack.get(BitterDataComponents.SCREWDRIVER);
    }
}
