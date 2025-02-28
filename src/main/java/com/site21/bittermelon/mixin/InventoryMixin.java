package com.site21.bittermelon.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public class InventoryMixin {
    @Shadow @Final public NonNullList<ItemStack> items;
    @Shadow @Final public NonNullList<ItemStack> offhand;
    @Shadow public int selected;

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static int getSelectionSize() {
        return 1;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public static boolean isHotbarSlot(int index) {
        return index == 0;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void swapPaint(double direction) {
        selected = 0;
    }

    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getSuitableHotbarSlot() {
        return 0;
    }


}
