package com.site21.bittermelon.client.event;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.render.TypingIndicatorRenderer;
import com.site21.bittermelon.systems.atmosphere.client.AtmosFogRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.TriState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;
import net.neoforged.neoforge.client.event.ViewportEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onFogRender(ViewportEvent.RenderFog event) {
        AtmosFogRenderer.applyFogDistance(event);
    }

    @SubscribeEvent
    public static void onComputeFogColors(ViewportEvent.ComputeFogColor event) {
        AtmosFogRenderer.applyFogColor(event);
    }

    @SubscribeEvent
    public static void onCanRenderNameTag(@NotNull RenderNameTagEvent.CanRender event) {
        if (!TypingIndicatorRenderer.canRender(event.getEntity())) {
            event.setCanRender(TriState.FALSE);
        }
    }

    @SubscribeEvent
    public static void onDoRenderNameTag(@NotNull RenderNameTagEvent.DoRender event) {
        if (Minecraft.getInstance().options.hideGui) return;

        TypingIndicatorRenderer.renderTypingIcon(
                event.getPoseStack(),
                event.getMultiBufferSource(),
                event.getPackedLight()
        );
    }
}
