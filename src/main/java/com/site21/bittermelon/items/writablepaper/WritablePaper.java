package com.site21.bittermelon.items.writablepaper;

import com.site21.bittermelon.items.base.BaseItem;
import com.site21.bittermelon.items.base.ItemWeight;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class WritablePaper extends BaseItem {
    public WritablePaper(Properties properties, int width, int height, ItemWeight itemWeight) {
        super(properties, width, height, itemWeight);
    }

//    @Override
//    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
//
//    }
}
