package com.site21.bittermelon.common.content.entities.scp815snake.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.event.LayerDefinitions;
import com.site21.bittermelon.common.content.entities.scp815snake.SCP815Snake;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SCP815SnakeRenderer extends LivingEntityRenderer<SCP815Snake, LivingEntityRenderState, SCP815SnakeModel> {
    public SCP815SnakeRenderer(EntityRendererProvider.Context context) {
        super(context, new SCP815SnakeModel(context.bakeLayer(LayerDefinitions.SCP_815_SNAKE_LAYER)), 0.05f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull LivingEntityRenderState livingEntityRenderState) {
        return ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/entity/scp_815_snake.png");
    }

    @Override
    public @NotNull LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    protected boolean shouldShowName(SCP815Snake entity, double distanceSq) {
        return false;
    }
}