package com.site21.bittermelon.client.render;

import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import com.site21.bittermelon.init.neoforge.BitterMobEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Helper class for managing and processing custom shaders and post effects.
 */
public class ShaderManager {
    private static final Identifier BLUR_SHADER = Identifier.withDefaultNamespace("blur");
    private static final Identifier SPIDER_SHADER = Identifier.withDefaultNamespace("spider");
    private static final Set<Identifier> POST_EFFECTS = new LinkedHashSet<>();

    /**
     * Processes all active post effects. Called within the GameRenderer mixin {@link com.site21.bittermelon.mixin.GameRendererMixin}.
     */
    public static void processPostEffects(Minecraft minecraft, CrossFrameResourcePool resourcePool) {
        for (Identifier shader : POST_EFFECTS) {
            PostChain postChain = minecraft.getShaderManager().getPostChain(shader, LevelTargetBundle.MAIN_TARGETS);
            if (postChain != null) {
                postChain.process(minecraft.getMainRenderTarget(), resourcePool);
            }
        }
    }

    /**
     * Handles updating the list of active post chains each client tick. Post chains are stored in a linked set and order of insertion therefore matters. <br>
     * <br>
     * Called on the client tick event in {@link com.site21.bittermelon.client.event.ClientEvents}.
     */
    public static void updatePostEffects() {
        POST_EFFECTS.clear();

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        if (player.hasEffect(BitterMobEffects.EYE_IRRITATION)) {
            POST_EFFECTS.add(BLUR_SHADER);
        }

        MobEffectInstance eyeballEffect = player.getEffect(BitterMobEffects.EYEBALL_GROWTH);
        if (eyeballEffect != null && eyeballEffect.getAmplifier() > 1) {
            POST_EFFECTS.add(SPIDER_SHADER);
        }
    }
}
