package com.site21.bittermelon.content.entities.implementations.scp131.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.entities.implementations.scp131.SCP131;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.content.entities.implementations.scp131.client.SCP131Model.SCP131_LAYER;

@OnlyIn(Dist.CLIENT)
public class SCP131Renderer extends MobRenderer<SCP131, SCP131Model> {
    private static final ResourceLocation SCP131_LOCATION = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/entity/scp131.png");

    public SCP131Renderer(EntityRendererProvider.Context context) {
        super(context, new SCP131Model(context.bakeLayer(SCP131_LAYER)), 0.1f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SCP131 scp131) {
        return SCP131_LOCATION;
    }
}
