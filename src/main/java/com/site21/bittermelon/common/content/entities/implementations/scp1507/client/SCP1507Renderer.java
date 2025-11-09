package com.site21.bittermelon.common.content.entities.implementations.scp1507.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.entities.implementations.scp1507.SCP1507;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class SCP1507Renderer extends MobRenderer<SCP1507, SCP1507Model> {
    public static final ModelLayerLocation SCP1507_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "scp1507_layer"), "main");

    public SCP1507Renderer(EntityRendererProvider.Context context) {
        super(context, new SCP1507Model(context.bakeLayer(SCP1507_LAYER)), 0.2f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SCP1507 scp1507) {
        return ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/entity/scp1507.png");
    }
}
