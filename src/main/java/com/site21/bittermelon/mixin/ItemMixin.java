package com.site21.bittermelon.mixin;

import com.site21.bittermelon.common.systems.component.temperature.HeatBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.HEAT_BEHAVIOR;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "inventoryTick", at = @At("HEAD"))
    private void onInventoryTick(@NotNull ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot, CallbackInfo ci) {
        stack.getOrDefault(HEAT_BEHAVIOR, HeatBehavior.DEFAULT).inventoryTick(stack, level, entity, slot);
    }
}
