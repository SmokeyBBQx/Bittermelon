package com.site21.bittermelon.content.items.cardboardbox;

import com.site21.bittermelon.content.items.base.BaseItem;
import com.site21.bittermelon.content.items.base.ItemWeight;

public class CollapsedCardboardBoxItem extends BaseItem {


    public CollapsedCardboardBoxItem(Properties properties, int width, int height, ItemWeight itemWeight) {
        super(properties, width, height, itemWeight);
    }

//    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
//        player.setItemInHand(usedHand, new ItemStack(CARDBOARD_BOX.get(), 1));
//        return InteractionResultHolder.consume(player.getItemInHand(usedHand));
//    }


}
