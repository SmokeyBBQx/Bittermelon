package com.site21.bittermelon.atmosphere;

import com.mojang.blaze3d.shaders.FogShape;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.substance.SubstanceStack;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.site21.bittermelon.util.ColorUtil.mixColorsRGB;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class AtmosFogRenderer {
    private static final float MAX_FOG_DENSITY = 0.01f; // Maximum fog density (0.0 - 1.0)
    private static final float DENSITY_THRESHOLD = 100.0f; // Gas amount at which max density is reached
    private static final float BASE_FOG_DISTANCE = 0.25f; // Base fog distance

    @SubscribeEvent
    public static void onFogRender(ViewportEvent.RenderFog event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        AtmosInstance instance = AtmosHandler.getAtmosInstanceAt(mc.level, mc.player.getOnPos());

        if (instance == null) return;

        float fogDensity = calculateFogDensity(instance);
        float fogStart = BASE_FOG_DISTANCE * (1 - fogDensity);

        event.setNearPlaneDistance(fogStart);
        event.setFarPlaneDistance(BASE_FOG_DISTANCE);
        event.setFogShape(FogShape.CYLINDER);
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onComputeFogColors(ViewportEvent.ComputeFogColor event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        AtmosInstance instance = AtmosHandler.getAtmosInstanceAt(mc.level, mc.player.getOnPos());
        if (instance == null) return;

        Map<Integer, Float> colors = new HashMap<>();

        for (SubstanceStack stack : instance.getGases()) {
            colors.put(stack.getSubstance().getColor(), stack.getAmount());
        }

        int[] colorsRGB = mixColorsRGB(colors);
        event.setRed(colorsRGB[0] / 255f);
        event.setGreen(colorsRGB[1] / 255f);
        event.setBlue(colorsRGB[2] / 255f);
    }

    private static float calculateFogDensity(@NotNull AtmosInstance instance) {
        float weightedAmount = 0;

        for (SubstanceStack stack : instance.getGases()) {
            float amount = stack.getAmount();
            float transparency = 0.9F;
            weightedAmount += amount * (1 - transparency); // Less transparent gases contribute more to density
        }

        return Math.min(weightedAmount / DENSITY_THRESHOLD, MAX_FOG_DENSITY);
    }
}
