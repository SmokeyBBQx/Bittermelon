package com.site21.bittermelon.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.site21.bittermelon.client.render.SleepRotations;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.site21.bittermelon.client.event.ClientSetup.SLEEP_TRANSFORM;
import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.RAGE;
import static com.site21.bittermelon.init.neoforge.BitterMobEffects.ELECTROCUTED;
import static com.site21.bittermelon.init.neoforge.BitterMobEffects.TASERED;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> {

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("RETURN"))
    private void onExtractRenderState(@NotNull T entity, S state, float partialTicks, CallbackInfo ci) {
        if (entity.hasEffect(ELECTROCUTED) || entity.hasEffect(TASERED) || entity.getData(RAGE) > 50) {
            state.isFullyFrozen = true;
        }
    }

    @Redirect(
            method = "setupRotations",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V",
                    ordinal = 6
            )
    )
    private void redirectSleepFlip(PoseStack poseStack, org.joml.Quaternionfc rotation,
                                   S state, PoseStack outerPoseStack, float bodyRot, float entityScale) {
        SleepRotations.SleepTransform transform = state.getRenderData(SLEEP_TRANSFORM);
        if (transform != SleepRotations.SleepTransform.DEFAULT) {
            poseStack.translate(0.0F, transform.yOffset(), 0.0F);
            poseStack.mulPose(Axis.XP.rotationDegrees(transform.rollDegrees()));
        } else {
            poseStack.mulPose(rotation);
        }
    }
}