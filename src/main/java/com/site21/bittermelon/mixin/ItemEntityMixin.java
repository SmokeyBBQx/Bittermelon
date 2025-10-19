package com.site21.bittermelon.mixin;

import com.site21.bittermelon.common.systems.component.temperature.HeatBehavior;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.HEAT_BEHAVIOR;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Shadow
    public abstract ItemStack getItem();

    @Inject(method = "fireImmune", at = @At("HEAD"), cancellable = true)
    private void onFireImmune(@NotNull CallbackInfoReturnable<Boolean> cir) {
        // TODO: Temporary: make all items fire immune until we have a better system
         cir.setReturnValue(true);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onEntityItemUpdate(CallbackInfo ci) {
        ItemEntity entity = (ItemEntity) (Object) this;
        ItemStack stack = this.getItem();

        stack.getOrDefault(HEAT_BEHAVIOR, HeatBehavior.DEFAULT).onEntityItemUpdate(stack, entity, entity.level());
    }
}
