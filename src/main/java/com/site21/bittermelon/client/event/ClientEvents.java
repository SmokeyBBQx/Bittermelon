package com.site21.bittermelon.client.event;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.render.ShaderManager;
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
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.TriState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = Bittermelon.MOD_ID)
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
        if (event.getName() == VanillaGuiLayers.PLAYER_HEALTH || event.getName() == VanillaGuiLayers.FOOD_LEVEL) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRenderOverlayPost(RenderGuiLayerEvent.@NotNull Post event) {
        RiseProgressBar.render(event.getGuiGraphics());
    }

    // TODO: Shit doesn't work
    // TODO: WHY DO YOU FAIL ME EVENTS
    @SubscribeEvent
    public static void onRenderLiving(RenderLivingEvent.@NotNull Post<?, ?, ?> event) {
        if (event.getRenderer().getModel() instanceof HumanoidModel<?> model) {
            float renderWidth = event.getRenderState().getRenderDataOrDefault(ClientSetup.ENTITY_WIDTH, 0.0f);

            if (renderWidth > 0f) {
                float widthFactor = Math.min(renderWidth / 0.5f, 2.0f);
                float x = -1.0f;
                float z = 0.005f * widthFactor;
                model.rightArm.xRot = x;
                model.leftArm.xRot = x;
                model.rightArm.zRot = z;
                model.leftArm.zRot = -z;
            }
        }
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
    public static void onClientTick(ClientTickEvent.Post event) {
        RiseKeyHandler.tick();
    }

    @SubscribeEvent
    public static void onClientTickPre(ClientTickEvent.Pre event) {
        ShaderManager.updatePostEffects();
    }
}
