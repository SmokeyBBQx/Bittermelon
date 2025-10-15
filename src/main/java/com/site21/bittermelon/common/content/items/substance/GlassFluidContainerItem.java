package com.site21.bittermelon.common.content.items.substance;

import com.site21.bittermelon.common.content.items.base.FragileItem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GlassFluidContainerItem extends FluidContainerItem implements FragileItem {
    public GlassFluidContainerItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onEntityItemUpdate(@NotNull ItemStack stack, @NotNull ItemEntity entity) {
        super.onEntityItemUpdate(stack, entity);
        checkForBreak(stack, entity);
        return false;
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull ServerLevel level, @NotNull Entity entity, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, level, entity, slot);

        hasLanded(stack);
    }
}
