package com.site21.bittermelon.content.items.cardboardbox;

import com.site21.bittermelon.content.items.base.BaseItem;
import com.site21.bittermelon.content.items.base.ItemWeight;
import net.minecraft.world.item.ItemStack;

import static com.site21.bittermelon.init.BitterDataComponents.IS_WRAPPED;

public class CardboardBoxItem extends BaseItem {


    public CardboardBoxItem(Properties properties, int width, int height, ItemWeight itemWeight) {
        super(properties, width, height, itemWeight);
    }

    public void setIsWrapped(ItemStack stack, boolean wrapped) {
        stack.set(IS_WRAPPED.get(), wrapped);
    }


}
