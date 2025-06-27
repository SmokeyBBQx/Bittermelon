package com.site21.bittermelon.content.items.medical.tools;

import com.site21.bittermelon.content.items.base.BaseItem;
import com.site21.bittermelon.content.items.base.ItemWeight;
import com.site21.bittermelon.content.items.medical.IBandage;

public class Bandage extends BaseItem implements IBandage {

    public Bandage(Properties properties, int width, int height, ItemWeight itemWeight) {
        super(properties, width, height, itemWeight);
    }
}
