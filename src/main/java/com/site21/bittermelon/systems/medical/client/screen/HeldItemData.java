package com.site21.bittermelon.systems.medical.client.screen;

import com.site21.bittermelon.systems.medical.client.screen.widget.CompartmentSpaceWidget;
import net.minecraft.world.item.ItemStack;

public record HeldItemData(ItemStack heldItem, CompartmentSpaceWidget parent) {
}
