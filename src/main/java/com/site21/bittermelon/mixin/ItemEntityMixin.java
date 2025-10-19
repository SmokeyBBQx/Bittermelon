package com.site21.bittermelon.mixin;

import net.minecraft.world.entity.item.ItemEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Inject(method = "fireImmune", at = @At("HEAD"), cancellable = true)
    private void onFireImmune(@NotNull CallbackInfoReturnable<Boolean> cir) {
        // TODO: Temporary: make all items fire immune until we have a better system
         cir.setReturnValue(true);
    }
}
