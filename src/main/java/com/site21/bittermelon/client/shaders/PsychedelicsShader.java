package com.site21.bittermelon.client.shaders;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class PsychedelicsShader {
    private static final ResourceLocation PSYCHEDELICS_SHADER = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "shaders/post/psychedelics.json");
    private static PostChain psychedelicsShader;

    @SubscribeEvent
    private static void registerShaders(RegisterShadersEvent event) {
        psychedelicsShader = ShaderUtils.registerShader(event, PSYCHEDELICS_SHADER, psychedelicsShader);
    }

    public static void resize(int width, int height) {
        if (psychedelicsShader != null) psychedelicsShader.resize(width, height);
    }

    public static void close() {
        if (psychedelicsShader != null) psychedelicsShader.close();
    }

    public static void processPsychedelicsShader(float partialTick) {
        if (psychedelicsShader == null) return;

        psychedelicsShader.process(partialTick);
    }
}
