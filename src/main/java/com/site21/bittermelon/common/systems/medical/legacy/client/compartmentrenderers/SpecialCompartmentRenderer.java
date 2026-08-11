package com.site21.bittermelon.common.systems.medical.legacy.client.compartmentrenderers;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.Entity;

public interface SpecialCompartmentRenderer {
    void extract(GuiGraphicsExtractor graphics, int x, int y, int width, int height, Entity entity);
}
