package net.smokeybbq.bittermelon.systems.atmospherics;

import com.mojang.blaze3d.shaders.FogShape;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.smokeybbq.bittermelon.Bittermelon;
import net.smokeybbq.bittermelon.systems.substances.Substance;
import net.smokeybbq.bittermelon.util.ModLogger;

import java.util.HashMap;
import java.util.Map;

import static net.smokeybbq.bittermelon.util.ColorUtil.mixColorsRGB;

public class AtmosRenderer {
    private static final float MAX_FOG_DENSITY = 0.01f; // Maximum fog density (0.0 - 1.0)
    private static final float DENSITY_THRESHOLD = 100.0f; // Gas amount at which max density is reached
    private static final float BASE_FOG_DISTANCE = 0.25f; // Base fog distance
    @SubscribeEvent
    public static void onFogRender(ViewportEvent.RenderFog event) {
        AtmosCell cell = getAtmosCellAtCamera();
        if (cell == null || cell.getTotalAmount() == 0) {
//            ModLogger.debug("No AtmosCell or empty cell at camera position");
            return;
        }

        float fogDensity = calculateFogDensity(cell);
        float fogStart = BASE_FOG_DISTANCE * (1 - fogDensity);
        float fogEnd = BASE_FOG_DISTANCE;

        event.setNearPlaneDistance(0);
        event.setFarPlaneDistance(5);
        event.setFogShape(FogShape.CYLINDER);
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onFogColors(ViewportEvent.ComputeFogColor event) {
        AtmosCell cell = getAtmosCellAtCamera();
        if (cell == null || cell.getTotalAmount() == 0) {
            return;
        }

        Map<Integer, Float> colors = new HashMap<>();

        for (Map.Entry<Substance, Float> entry : cell.getGasses().entrySet()) {
            colors.put(entry.getKey().getColor(), entry.getValue());
        }

        int[] colorsRGB = mixColorsRGB(colors);
        event.setRed(colorsRGB[0] / 255f);
        event.setGreen(colorsRGB[1] / 255f);
        event.setBlue(colorsRGB[2] / 255f);
    }

    private static float calculateFogDensity(AtmosCell cell) {
        float weightedAmount = 0;
        float totalAmount = cell.getTotalAmount();

        for (Map.Entry<Substance, Float> entry : cell.getGasses().entrySet()) {
            Substance substance = entry.getKey();
            float amount = entry.getValue();
            float transparency = 0.9F;
            weightedAmount += amount * (1 - transparency); // Less transparent gases contribute more to density
        }

        return Math.min(weightedAmount / DENSITY_THRESHOLD, MAX_FOG_DENSITY);
    }

    private static AtmosCell getAtmosCellAtCamera() {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        Entity camera = mc.getCameraEntity();

        if (level == null || camera == null) {
            return null;
        }

        AtmosManager atmosManager = AtmosManager.getInstance();
        if (atmosManager == null) {
            return null;
        }

        BlockPos pos = camera.blockPosition();
        return atmosManager.getCellFromBlockPos(pos);
    }
}
