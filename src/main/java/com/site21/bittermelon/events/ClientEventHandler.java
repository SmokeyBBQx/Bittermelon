package com.site21.bittermelon.events;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.atmosphere.data.AtmosLevelData;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.content.personnel.registry.PersonnelRegistry;
import com.site21.bittermelon.content.economy.bank.AccountRegistry;
import com.site21.bittermelon.content.personnel.privilege.PrivilegeManager;
import com.site21.bittermelon.content.telecomms.intercom.IntercomManager;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class ClientEventHandler {
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
    public static void onRenderOverlay(RenderGuiLayerEvent.@NotNull Pre event) {
        if (event.getName() == VanillaGuiLayers.EXPERIENCE_LEVEL
                || event.getName() == VanillaGuiLayers.PLAYER_HEALTH
                || event.getName() == VanillaGuiLayers.FOOD_LEVEL) {
            event.setCanceled(true);
        }
    }
}