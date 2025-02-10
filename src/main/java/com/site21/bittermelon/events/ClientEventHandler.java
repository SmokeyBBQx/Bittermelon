package com.site21.bittermelon.events;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.character.CharacterManager;
import com.site21.bittermelon.database.PersonnelRegistry;
import com.site21.bittermelon.economy.AccountRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void onClientDisconnect(ClientPlayerNetworkEvent.LoggingOut event) {
        AccountRegistry.clearClientData();
        CharacterManager.clearClientData();
        PersonnelRegistry.clearClientData();
    }
}