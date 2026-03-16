package com.site21.bittermelon.mixin;

import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {

    // Pretty scuffed, but it works as a starting point
    @Inject(method = "moveItemStackTo", at = @At("HEAD"))
    private void onMoveItemStackTo(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection, CallbackInfoReturnable<ItemStack> cir) {
        AbstractContainerMenu menu = (AbstractContainerMenu) (Object) this;
        if (stack.getCount() != 0) {
            // Find the slot containing the stack
            Slot stack_slot = null;
            for (Slot slot : menu.slots) {
                if (slot.getItem().equals(stack)) {
                    stack_slot = slot;
                    break;
                }
            }
            if (stack_slot != null) {
                int space_found = 0;
                NonNullList<Slot> marked_slots = NonNullList.create();
                // Identify needed slots that can be moved to
                for (int i = startIndex; i < endIndex; i++) {
                    Slot slot = menu.slots.get(i);
                    if (slot.getItem().is(stack.getItem())) {
                        if (!slot.allowModification(Minecraft.getInstance().player)) continue;
                        ItemStack other = slot.getItem();
                        if (other.getMaxStackSize() != other.getCount()) {
                            marked_slots.add(slot);
                            space_found += other.getMaxStackSize() - other.getCount();
                        }
                        if (space_found > stack.getCount()) break;
                    }
                }

                // Averages the temperatures of all the targets to a uniform temperature (not ideal, but functional)
                int total_count = stack.getCount();
                int combined_temperature = stack.getOrDefault(BitterDataComponents.TEMPERATURE, 273) * total_count;
                for (Slot slot : marked_slots) {
                    total_count += slot.getItem().getCount();
                    combined_temperature += slot.getItem().getOrDefault(BitterDataComponents.TEMPERATURE, 273) * slot.getItem().getCount();
                }

                int average_temperature = combined_temperature / total_count;
                stack.set(BitterDataComponents.TEMPERATURE, average_temperature);
                for (Slot slot : marked_slots) {
                    slot.getItem().set(BitterDataComponents.TEMPERATURE, average_temperature);
                }
            }
        }
    }
}
