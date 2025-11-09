package com.site21.bittermelon.common.content.items.substance;

import com.site21.bittermelon.common.content.items.base.ItemWeight;
import com.site21.bittermelon.common.content.items.base.FragileItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class GlassFluidContainerItem extends FluidContainerItem implements FragileItem {
    public GlassFluidContainerItem(Properties properties, boolean hasLid) {
        super(properties, hasLid);
    }

    @Override
    public boolean onEntityItemUpdate(@NotNull ItemStack stack, @NotNull ItemEntity entity) {
        super.onEntityItemUpdate(stack, entity);
        checkForBreak(stack, entity);
        return false;
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        hasLanded(stack);
    }

//    public void projectileHitBlock(ItemStack stack, Level level, @NotNull BlockPos pos) {
//        if (!checkForBreakOnHit(stack, level, pos)) {
//            super.projectileHitBlock(stack, level, pos);
//        } else {
//            super.spill(stack, level, pos.above(), getTotalVolume(stack));
//            // Bug when hitting walls, it replaces it
//        }
//    }
}
