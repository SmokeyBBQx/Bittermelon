package com.site21.bittermelon.client.shaders;

import com.google.gson.JsonSyntaxException;
import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class BlurShader {
    private static final ResourceLocation BLUR_SHADER = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "shaders/post/blur.json");
    private static PostChain blurShader;

    @SubscribeEvent
    private static void registerShaders(RegisterShadersEvent event) {
        if (blurShader != null) blurShader.close();
        Minecraft client = Minecraft.getInstance();

        try {
            blurShader = new PostChain(client.getTextureManager(), event.getResourceProvider(), client.getMainRenderTarget(), BLUR_SHADER);
            blurShader.resize(client.getWindow().getWidth(), client.getWindow().getHeight());
        } catch (IOException ioexception) {
            Bittermelon.LOGGER.warn("Failed to load shader: {}", BLUR_SHADER, ioexception);
        } catch (JsonSyntaxException jsonsyntaxexception) {
            Bittermelon.LOGGER.warn("Failed to parse shader: {}", BLUR_SHADER, jsonsyntaxexception);
        }
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
