package com.site21.bittermelon.client.renderer;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;

//@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class ShaderHandler {
    private static PostChain shader;
    private static final ResourceLocation SHADER_LOCATION =
            ResourceLocation.fromNamespaceAndPath("minecraft", "shaders/post/ntsc.json");

    @SubscribeEvent
    public static void onRenderLevel(@NotNull RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            if (shader == null) {
                try {
                    shader = new PostChain(
                            Minecraft.getInstance().getTextureManager(),
                            Minecraft.getInstance().getResourceManager(),
                            Minecraft.getInstance().getMainRenderTarget(),
                            SHADER_LOCATION
                    );
                    shader.resize(
                            Minecraft.getInstance().getWindow().getWidth(),
                            Minecraft.getInstance().getWindow().getHeight()
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (shader != null) {
                shader.process(Minecraft.getInstance().getFrameTimeNs());
            }
        }
    }
}
