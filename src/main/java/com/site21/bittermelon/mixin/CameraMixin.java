package com.site21.bittermelon.mixin;

import com.github.stephengold.joltjni.RVec3;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.site21.bittermelon.common.content.entities.ragdoll.RagdollEntity;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Camera.class)
public class CameraMixin {
    @WrapOperation(
            method = "alignWithEntity",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setPosition(DDD)V")
    )
    private void bittermelon$offsetCamera(Camera camera, double x, double y, double z, Operation<Void> original,
                                    @Local(argsOnly = true) float partialTicks) {
        if (!(camera.entity() instanceof RagdollEntity ragdollEntity)) {
            original.call(camera, x, y, z);
            return;
        }

        RVec3 offset = ragdollEntity.getPartTransformations().getFirst().interpolatedPos(partialTicks, new RVec3());
        original.call(camera, offset.xx(), offset.yy(), offset.zz());
    }
}
