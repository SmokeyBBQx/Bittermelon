package com.site21.bittermelon.common.content.items;

import com.site21.bittermelon.common.systems.germs.Germ;
import com.site21.bittermelon.common.systems.germs.GermRegistry;
import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import com.site21.bittermelon.common.content.items.base.BaseItem;
import com.site21.bittermelon.common.content.items.base.ItemWeight;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GermTest extends BaseItem {
    public GermTest(Properties properties) {
        super(properties, 1, 1, ItemWeight.VERY_LIGHT);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            Germ germ = Germ.generateRandomGerm(225252);
            stack.set(BitterDataComponents.GERMS, List.of(GermRegistry.get(level).registerGerm(germ)));
        }

        return ItemUtils.startUsingInstantly(level, player, hand);
    }
}
