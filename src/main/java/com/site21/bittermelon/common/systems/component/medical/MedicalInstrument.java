package com.site21.bittermelon.common.systems.component.medical;

import com.site21.bittermelon.common.systems.medical.client.HealthScreen;
import com.site21.bittermelon.common.systems.medical.client.tool.ToolWidget;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public interface MedicalInstrument {
    Optional<ResourceLocation> icon();
    ToolWidget createWidget(ItemStack stack, int x, int y, int width, int height, HealthScreen screen);
}
