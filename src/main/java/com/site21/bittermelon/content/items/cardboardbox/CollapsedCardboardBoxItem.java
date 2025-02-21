package com.site21.bittermelon.content.items.cardboardbox;

import com.site21.bittermelon.content.items.base.BaseItem;
import com.site21.bittermelon.content.items.base.ItemWeight;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import static com.site21.bittermelon.init.neoforge.BitterItems.CARDBOARD_BOX;

public class CollapsedCardboardBoxItem extends BaseItem {


    public CollapsedCardboardBoxItem(Properties properties, int width, int height, ItemWeight itemWeight) {
        super(properties, width, height, itemWeight);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        player.setItemInHand(usedHand, new ItemStack(CARDBOARD_BOX.get(), 1));
        return InteractionResultHolder.consume(player.getItemInHand(usedHand));
    }
}
