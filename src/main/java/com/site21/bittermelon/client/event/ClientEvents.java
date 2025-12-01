package com.site21.bittermelon.client.event;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.render.TypingIndicatorRenderer;
import com.site21.bittermelon.common.systems.atmosphere.client.AtmosFogRenderer;
import com.site21.bittermelon.common.systems.atmosphere.data.AtmosLevelData;
import com.site21.bittermelon.common.systems.blockdamage.client.BlockDamageRenderer;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.economy.bank.AccountRegistry;
import com.site21.bittermelon.common.systems.personnel.privilege.PrivilegeManager;
import com.site21.bittermelon.common.systems.personnel.registry.PersonnelRegistry;
import com.site21.bittermelon.common.systems.stumble.client.RiseKeyHandler;
import com.site21.bittermelon.common.systems.stumble.client.RiseProgressBar;
import com.site21.bittermelon.common.systems.telecomms.intercom.IntercomManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.TriState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onClientDisconnect(ClientPlayerNetworkEvent.LoggingOut event) {
        AccountRegistry.clearClientData();
        CharacterManager.clearClientData();
        PersonnelRegistry.clearClientData();
        AtmosLevelData.clearClientData();
        IntercomManager.clearClientData();
        PrivilegeManager.clearClientData();
    }

    @SubscribeEvent
    public static void onRenderLevelAfterTranslucent(RenderLevelStageEvent.@NotNull AfterTranslucentBlocks event) {
        BlockDamageRenderer.renderDamaged(event.getLevel(), event.getPoseStack(), event.getCamera(), event.getRenderableSections());
    }

    @SubscribeEvent
    public static void onRenderOverlayPre(RenderGuiLayerEvent.@NotNull Pre event) {
        if (event.getName() == VanillaGuiLayers.EXPERIENCE_LEVEL
                || event.getName() == VanillaGuiLayers.PLAYER_HEALTH
                || event.getName() == VanillaGuiLayers.FOOD_LEVEL) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRenderOverlayPost(RenderGuiLayerEvent.@NotNull Post event) {
        RiseProgressBar.render(event.getGuiGraphics());
    }

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

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent event) {
        RiseKeyHandler.tick();
    }
}
