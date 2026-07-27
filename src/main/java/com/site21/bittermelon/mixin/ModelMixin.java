package com.site21.bittermelon.mixin;

import com.site21.bittermelon.client.event.ClientSetup;
import com.site21.bittermelon.init.custom.BodyParts;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Mixin(Model.class)
public abstract class ModelMixin {
    @Shadow
    public abstract ModelPart root();

    @Shadow
    @Final
    protected ModelPart root;

    @Inject(
            at = @At("HEAD"),
            method = "setupAnim(Ljava/lang/Object;)V"
    )
    public void setupAnim(Object state, CallbackInfo ci) {
        if (!(state instanceof EntityRenderState renderState)) return;

        Map<String, Boolean> limbVisibility = renderState.getRenderDataOrDefault(ClientSetup.LIMB_VISIBILITY, new HashMap<>());

        for (Map.Entry<String, Boolean> entry : limbVisibility.entrySet()) {
            try {
                root().getChild(entry.getKey()).visible = entry.getValue();
            } catch (NoSuchElementException ignored) {}
        }

        if (!renderState.getRenderDataOrDefault(ClientSetup.ROOT_PART, BodyParts.EMPTY.get().toInstance()).getBodyPart().equals(BodyParts.EMPTY.get())) {
            root.visible = false;
        }
    }
}
