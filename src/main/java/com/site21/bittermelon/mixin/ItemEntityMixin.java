package com.site21.bittermelon.mixin;

import com.site21.bittermelon.common.systems.component.temperature.HeatBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.BURN_TIME;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.HEAT_BEHAVIOR;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Shadow
    public abstract ItemStack getItem();

    @Inject(method = "fireImmune", at = @At("HEAD"), cancellable = true)
    private void onFireImmune(@NotNull CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(getItem().get(BURN_TIME) == null);
    }

    @ModifyVariable(
            method = "hurtServer",
            at = @At("HEAD"),
            argsOnly = true,
            name = "damage")
    private float modifyDamageAmount(float damage, ServerLevel level, DamageSource source) {
        ItemEntity self = (ItemEntity) (Object) this;
        if (self.isOnFire()) {
            return 0.0f;
        }
        return damage;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onEntityItemUpdate(CallbackInfo ci) {
        ItemEntity entity = (ItemEntity) (Object) this;
        ItemStack stack = this.getItem();

        stack.getOrDefault(HEAT_BEHAVIOR, HeatBehavior.DEFAULT).onEntityItemUpdate(stack, entity, entity.level());
    }
}
