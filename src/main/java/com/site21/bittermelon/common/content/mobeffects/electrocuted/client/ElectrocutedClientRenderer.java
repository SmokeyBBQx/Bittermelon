package com.site21.bittermelon.common.content.mobeffects.electrocuted.client;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.Minecraft;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterMobEffects.*;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class ElectrocutedClientRenderer {
    @SubscribeEvent
    public static void onComputeCameraAngles(ViewportEvent.ComputeCameraAngles event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        MobEffectInstance instance = player.getEffect(ELECTROCUTED);
        if (instance == null) {
            instance = player.getEffect(TASERED);
        }

        if (instance != null) {
            RandomSource random = mc.player.getRandom();

            float intensity = instance.getAmplifier();
            float offsetYaw = (random.nextFloat() - 0.5f) * intensity;
            float offsetPitch = (random.nextFloat() - 0.5f) * intensity;
            float offsetRoll = (random.nextFloat() - 0.5f) * intensity;

            event.setYaw(event.getYaw() + offsetYaw);
            event.setPitch(event.getPitch() + offsetPitch);
            event.setRoll(event.getRoll() + offsetRoll);
        }
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiEvent.@NotNull Pre event) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

//        if (player.hasEffect(TASERED) || player.hasEffect(ELECTROCUTED)) {
//            MobEffectInstance instance = player.getEffect(ELECTROCUTED);
//            if (instance == null) {
//                instance = player.getEffect(TASERED);
//            }
//
//            float amplifier = instance.getAmplifier();
//            renderVignette(event.getGuiGraphics(), 0, amplifier, amplifier);
//        }
    }
}
