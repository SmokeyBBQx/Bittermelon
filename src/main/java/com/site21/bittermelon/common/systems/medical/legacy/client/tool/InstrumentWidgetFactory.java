package com.site21.bittermelon.common.systems.medical.legacy.client.tool;

import com.site21.bittermelon.common.systems.medical.legacy.client.HealthScreen;
import net.minecraft.world.item.ItemStack;

public interface InstrumentWidgetFactory {
    InstrumentWidget create(ItemStack stack, int x, int y, int width, int height, HealthScreen screen);
}
