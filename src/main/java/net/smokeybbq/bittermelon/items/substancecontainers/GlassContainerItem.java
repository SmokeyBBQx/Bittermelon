package net.smokeybbq.bittermelon.items.substancecontainers;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.smokeybbq.bittermelon.items.base.FragileItem;
import net.smokeybbq.bittermelon.items.base.ItemSize;
import net.smokeybbq.bittermelon.items.base.ItemWeight;
import org.jetbrains.annotations.NotNull;

public class GlassContainerItem extends SubstanceContainerItem implements FragileItem {
    public GlassContainerItem(Properties pProperties, ItemSize itemSize, ItemWeight itemWeight, int capacity) {
        super(pProperties, itemSize, itemWeight, capacity);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        super.onEntityItemUpdate(stack, entity);
        checkForBreak(stack, entity);
        return false;
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        hasLanded(stack);
    }

    public void projectileHitBlock(ItemStack itemStack, Level level, BlockPos pos) {
        if (!checkForBreakOnHit(itemStack, level, pos)) {
            super.projectileHitBlock(itemStack, level, pos);
        } else {
            super.spillEverything(itemStack, level, pos.above());
            // Bug when hitting walls, it replaces it
        }
    }
}
