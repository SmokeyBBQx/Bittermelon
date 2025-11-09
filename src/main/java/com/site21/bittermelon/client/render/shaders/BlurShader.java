package com.site21.bittermelon.client.render.shaders;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class BlurShader {
    private static final ResourceLocation BLUR_SHADER = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "shaders/post/blur.json");
    private static PostChain blurShader;

    @SubscribeEvent
    private static void registerShaders(RegisterShadersEvent event) {
        blurShader = ShaderUtils.registerShader(event, BLUR_SHADER, blurShader);
    }

    public static void resize(int width, int height) {
        if (blurShader != null) blurShader.resize(width, height);
    }

    public static void close() {
        if (blurShader != null) blurShader.close();
    }

    public static void processBlurShader(float partialTick, float blurRadius) {
        if (blurShader == null) return;

        blurShader.setUniform("Radius", blurRadius);
        blurShader.process(partialTick);
    }
}
