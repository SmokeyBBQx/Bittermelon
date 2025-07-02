package com.site21.bittermelon.content.medical.client.effects;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class Vignette {
    private static final ResourceLocation VIGNETTE_TEXTURE = ResourceLocation.fromNamespaceAndPath("bittermelon", "textures/misc/vignette.png");

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiEvent.@NotNull Pre event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        Character character = CharacterManager.get(player.level()).getActiveCharacter(player);
        if (character == null) return;

        MedicalStats medicalStats = character.getMedicalStats();
        if (medicalStats.getConsciousness() < 0.1f) {
            renderVignette();
        }
    }

    public static void renderVignette() {
        Minecraft minecraft = Minecraft.getInstance();

        int width = minecraft.getWindow().getGuiScaledWidth();
        int height = minecraft.getWindow().getGuiScaledHeight();

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, VIGNETTE_TEXTURE);
        Color color = computeVignetteColor();
        RenderSystem.setShaderColor(0.1f, 0.1f, 0.1f, 1f);

        BufferBuilder bufferBuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        bufferBuilder.addVertex(0, height, -90)
                .setUv(0, 1);
        bufferBuilder.addVertex(width, height, -90)
                .setUv(1, 1);
        bufferBuilder.addVertex(width, 0, -90)
                .setUv(1, 0);
        bufferBuilder.addVertex(0, 0, -90)
                .setUv(0, 0);

        BufferUploader.drawWithShader(Objects.requireNonNull(bufferBuilder.build()));
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    }

    @Contract(value = " -> new", pure = true)
    private static @NotNull Color computeVignetteColor() {
        return new Color(0.01f, 0.01f, 0.01f, 0.001f);
    }
}
