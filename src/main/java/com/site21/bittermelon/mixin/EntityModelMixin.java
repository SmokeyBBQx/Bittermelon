package com.site21.bittermelon.mixin;

import com.site21.bittermelon.client.event.ClientSetup;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Mixin(EntityModel.class)
public abstract class EntityModelMixin<T extends EntityRenderState> extends Model {
    public EntityModelMixin(ModelPart root, Function<Identifier, RenderType> renderType) {
        super(root, renderType);
    }

    @Inject(at = @At("HEAD"), method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;)V")
    public void setupAnim(T renderState, CallbackInfo ci) {
        Map<String, Boolean> limbVisibility = renderState.getRenderDataOrDefault(ClientSetup.LIMB_VISIBILITY, new HashMap<>());
        Function<String, ModelPart> partLookup = root().createPartLookup();
        for (Map.Entry<String, Boolean> entry : limbVisibility.entrySet()) {
            partLookup.apply(entry.getKey()).visible = entry.getValue();
        }
    }
}
