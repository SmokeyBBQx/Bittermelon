package com.site21.bittermelon.mixin;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.RAGE;
import static com.site21.bittermelon.init.neoforge.BitterMobEffects.ELECTROCUTED;
import static com.site21.bittermelon.init.neoforge.BitterMobEffects.TASERED;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("RETURN"))
    private void onExtractRenderState(@NotNull T entity, S renderState, float partialTick, CallbackInfo ci) {
        if (entity.hasEffect(ELECTROCUTED) || entity.hasEffect(TASERED) || entity.getData(RAGE) > 50) {
            renderState.isFullyFrozen = true;
        }
    }
}