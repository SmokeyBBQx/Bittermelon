package com.site21.bittermelon.content.medical.client.screen;

import com.site21.bittermelon.content.medical.client.screen.widget.CompartmentSpaceWidget;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public record HeldItemData(ItemStack heldItem, CompartmentSpaceWidget parent) {
}
