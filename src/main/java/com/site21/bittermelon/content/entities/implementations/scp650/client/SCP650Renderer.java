package com.site21.bittermelon.content.entities.implementations.scp650.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.entities.implementations.scp650.SCP650;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.content.entities.implementations.scp650.client.SCP650Model.SCP650_LAYER;

@OnlyIn(Dist.CLIENT)
public class SCP650Renderer extends MobRenderer<SCP650, SCP650Model<SCP650>> {
    private static final ResourceLocation SCP650_LOCATION = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "textures/entity/scp650.png");

    public SCP650Renderer(EntityRendererProvider.Context context) {
        super(context, new SCP650Model<>(context.bakeLayer(SCP650_LAYER)), 0.25f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SCP650 scp650) {
        return SCP650_LOCATION;
    }
}
