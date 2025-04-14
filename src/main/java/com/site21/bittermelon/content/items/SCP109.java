package com.site21.bittermelon.content.items;

import com.site21.bittermelon.content.items.base.ItemWeight;
import com.site21.bittermelon.content.items.containers.substance.FluidContainerItem;
import com.site21.bittermelon.content.substance.SubstanceStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.custom.Substances.LIQUID_WATER;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.HAS_LANDED;

public class SCP109 extends FluidContainerItem {
    public SCP109(Properties properties) {
        super(properties, 1, 1, ItemWeight.MEDIUM, 0, 10, true);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        updateSubstance(player.getItemInHand(usedHand), new SubstanceStack(LIQUID_WATER.get(), 1000));
        return super.use(level, player, usedHand);
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        return 0;
    }


}
