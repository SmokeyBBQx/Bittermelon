package com.site21.bittermelon.common.content.entities.scp1507.client;

import net.minecraft.client.model.animal.bee.BeeStingerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.StuckInBodyLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;

public class EmbeddedLegLayer<M extends PlayerModel> extends StuckInBodyLayer<M, Unit> {
    private static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/entity/bee/bee_stinger.png");

    public EmbeddedLegLayer(LivingEntityRenderer<?, AvatarRenderState, M> renderer, EntityRendererProvider.Context context) {
        super(
                renderer,
                new BeeStingerModel(context.bakeLayer(ModelLayers.BEE_STINGER)),
                Unit.INSTANCE,
                TEXTURE,
                StuckInBodyLayer.PlacementStyle.IN_CUBE
        );
    }

    @Override
    protected int numStuck(AvatarRenderState state) {
        return 0;
    }
}
