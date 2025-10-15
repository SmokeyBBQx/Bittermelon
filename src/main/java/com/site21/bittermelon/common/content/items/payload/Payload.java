package com.site21.bittermelon.common.content.items.payload;

import com.site21.bittermelon.common.content.items.base.BaseItem;
import com.site21.bittermelon.common.content.items.base.ItemWeight;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class Payload extends BaseItem {
    public Payload(Properties properties, int width, int height, ItemWeight itemWeight) {
        super(properties, width, height, itemWeight);
    }

    public void detonate(Level level, BlockPos pos) {

    }
}
