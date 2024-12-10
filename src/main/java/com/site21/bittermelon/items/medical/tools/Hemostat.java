package com.site21.bittermelon.items.medical.tools;

import com.site21.bittermelon.items.base.BaseItem;
import com.site21.bittermelon.items.base.ItemWeight;
import com.site21.bittermelon.items.medical.AbstractClamp;
import net.minecraft.world.item.Item;

public class Hemostat extends BaseItem implements AbstractClamp {
    public Hemostat(Properties properties, int width, int height, ItemWeight itemWeight) {
        super(properties, width, height, itemWeight);
    }

}
