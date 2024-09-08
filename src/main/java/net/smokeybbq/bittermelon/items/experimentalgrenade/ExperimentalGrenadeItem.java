package net.smokeybbq.bittermelon.items.experimentalgrenade;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.smokeybbq.bittermelon.items.base.BaseItem;
import net.smokeybbq.bittermelon.items.base.ItemSize;
import net.smokeybbq.bittermelon.items.base.ItemWeight;
import net.smokeybbq.bittermelon.items.substancecontainers.SubstanceItem;
import org.jetbrains.annotations.NotNull;

public class ExperimentalGrenadeItem extends SubstanceItem {
    public ExperimentalGrenadeItem(Properties pProperties) {
        super(pProperties, 50, ItemSize.SMALL, ItemWeight.MEDIUM);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        Level level = entity.level();
        if (!level.isClientSide && !entity.isNoGravity() && entity.onGround()) {
            CompoundTag tag = stack.getOrCreateTag();
            if (!tag.getBoolean("hasLanded")) {
                tag.putBoolean("hasLanded", true);
                explodeOnLanding(stack, level, entity.blockPosition());
            }
        }
        return false;
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        CompoundTag tag = stack.getOrCreateTag();
        if (tag.getBoolean("hasLanded")) {
            tag.putBoolean("hasLanded", false);
        }
    }

    private void explodeOnLanding(ItemStack stack, Level level, BlockPos blockPos) {

    }


}
