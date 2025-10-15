package com.site21.bittermelon.events;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.systems.atmosphere.data.AtmosLevelData;
import com.site21.bittermelon.systems.character.CharacterManager;
import com.site21.bittermelon.systems.economy.bank.AccountRegistry;
import com.site21.bittermelon.systems.personnel.privilege.PrivilegeManager;
import com.site21.bittermelon.systems.personnel.registry.PersonnelRegistry;
import com.site21.bittermelon.systems.telecomms.intercom.IntercomManager;
import net.minecraft.client.renderer.block.model.SimpleModelWrapper;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class ClientEventHandler {
    public static final StandaloneModelKey<SimpleModelWrapper> LEFT_DOOR = new StandaloneModelKey<>(
            () -> "large_sliding_door_left"
    );

    public static final StandaloneModelKey<SimpleModelWrapper> RIGHT_DOOR = new StandaloneModelKey<>(
            () -> "large_sliding_door_right"
    );

    public static final StandaloneModelKey<SimpleModelWrapper> FRAME = new StandaloneModelKey<>(
            () -> "large_sliding_door_frame"
    );

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

    @SubscribeEvent
    public static void registerStandaloneModels(ModelEvent.@NotNull RegisterStandalone event) {
        event.register(LEFT_DOOR, SimpleUnbakedStandaloneModel.simpleModelWrapper(
                ResourceLocation.fromNamespaceAndPath("bittermelon", "block/large_sliding_door_left")
        ));
        event.register(RIGHT_DOOR, SimpleUnbakedStandaloneModel.simpleModelWrapper(
                ResourceLocation.fromNamespaceAndPath("bittermelon", "block/large_sliding_door_right")
        ));
        event.register(FRAME, SimpleUnbakedStandaloneModel.simpleModelWrapper(
                ResourceLocation.fromNamespaceAndPath("bittermelon", "block/large_sliding_door_frame")
        ));
    }
}