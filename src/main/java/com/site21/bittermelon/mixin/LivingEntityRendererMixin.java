package com.site21.bittermelon.mixin;

import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.site21.bittermelon.init.neoforge.BitterMobEffects.ELECTROCUTED;
import static com.site21.bittermelon.init.neoforge.BitterMobEffects.TASERED;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    @Inject(method = "isShaking", at = @At("HEAD"), cancellable = true)
    private void onIsShaking(@NotNull LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity.hasEffect(ELECTROCUTED) || entity.hasEffect(TASERED)) {
            cir.setReturnValue(true);
        }
    }
}
