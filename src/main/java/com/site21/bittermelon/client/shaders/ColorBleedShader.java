package com.site21.bittermelon.client.shaders;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ColorBleedShader {
    private static final ResourceLocation COLOR_BLEED = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "shaders/post/collapse.json");
    private static PostChain colorBleedShader;

    @SubscribeEvent
    private static void registerShaders(RegisterShadersEvent event) {
        colorBleedShader = ShaderUtils.registerShader(event, COLOR_BLEED, colorBleedShader);
    }

    public static void resize(int width, int height) {
        if (colorBleedShader != null) colorBleedShader.resize(width, height);
    }

    public static void close() {
        if (colorBleedShader != null) colorBleedShader.close();
    }

    public static void processBlurShader(float partialTick, float blurRadius) {
        if (colorBleedShader == null) return;

        colorBleedShader.process(partialTick);
    }
}
