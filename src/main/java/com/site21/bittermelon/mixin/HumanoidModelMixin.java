package com.site21.bittermelon.mixin;

import com.site21.bittermelon.client.event.ClientSetup;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin {
    @Final
    @Shadow
    public ModelPart leftArm;

    @Final
    @Shadow
    public ModelPart rightArm;

    @Inject(at = @At("RETURN"), method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/HumanoidRenderState;)V")
    private void onSetupAnim(@NotNull HumanoidRenderState state, CallbackInfo ci) {
        float renderWidth = state.getRenderDataOrDefault(ClientSetup.ENTITY_WIDTH, 0f);
        if (renderWidth > 0f) {
            leftArm.x -= renderWidth;
            rightArm.x += renderWidth;
        }
    }
}
