package com.site21.bittermelon.client.render.shaders;

import com.google.gson.JsonSyntaxException;
import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class ShaderUtils {
    public static @Nullable PostChain registerShader(RegisterShadersEvent event, ResourceLocation shaderLocation, PostChain shader) {
        if (shader != null) shader.close();
        Minecraft client = Minecraft.getInstance();

        try {
            shader = new PostChain(client.getTextureManager(), event.getResourceProvider(), client.getMainRenderTarget(), shaderLocation);
            shader.resize(client.getWindow().getWidth(), client.getWindow().getHeight());
            return shader;
        } catch (IOException ioexception) {
            Bittermelon.LOGGER.warn("Failed to load shader: {}", shaderLocation, ioexception);
        } catch (JsonSyntaxException jsonsyntaxexception) {
            Bittermelon.LOGGER.warn("Failed to parse shader: {}", shaderLocation, jsonsyntaxexception);
        }
        return null;
    }
}
