package com.site21.bittermelon.common.content.entities.scp1507.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.entities.scp1507.SCP1507;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.client.event.LayerDefinitions.SCP_1507_LAYER;

public class SCP1507Renderer extends MobRenderer<SCP1507, SCP1507RenderState, SCP1507Model> {
    public SCP1507Renderer(EntityRendererProvider.Context context) {
        super(context, new SCP1507Model(context.bakeLayer(SCP_1507_LAYER)), 0.2f);
    }

    @Override
    public @NotNull SCP1507RenderState createRenderState() {
        return new SCP1507RenderState();
    }

    @Override
    public @NotNull Identifier getTextureLocation(@NotNull SCP1507RenderState renderState) {
        return Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/entity/scp_1507.png");
    }

    @Override
    public void extractRenderState(@NotNull SCP1507 entity, @NotNull SCP1507RenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.attackTime = entity.getAttackTime() > 0 ? entity.getAttackTime() - partialTick : 0.0f;
        reusedState.onGround = entity.onGround();
        reusedState.leftLegAttached = entity.isLeftLegAttached();
        reusedState.rightLegAttached = entity.isRightLegAttached();
    }
}
