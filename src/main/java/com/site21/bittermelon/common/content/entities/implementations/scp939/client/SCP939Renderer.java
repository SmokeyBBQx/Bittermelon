package com.site21.bittermelon.common.content.entities.implementations.scp939.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.entities.implementations.scp939.SCP939;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.client.event.LayerDefinitions.SCP939_LAYER;

@OnlyIn(Dist.CLIENT)
public class SCP939Renderer extends MobRenderer<SCP939, SCP939Model<SCP939>> {

    private static final ResourceLocation SCP939_LOCATION = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/entity/scp939.png");
    public SCP939Renderer(EntityRendererProvider.Context context) {
        super(context, new SCP939Model<>(context.bakeLayer(SCP939_LAYER)), 0.5f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SCP939 entity) {
        return SCP939_LOCATION;
    }
}
