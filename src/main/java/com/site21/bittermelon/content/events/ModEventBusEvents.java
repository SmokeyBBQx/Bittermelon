package com.site21.bittermelon.content.events;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.models.entity.ChickenModel;
import com.site21.bittermelon.client.models.entity.SCP939Model;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import static com.site21.bittermelon.client.models.ModelLayers.CHICKEN_LAYER;
import static com.site21.bittermelon.client.models.ModelLayers.SCP939_LAYER;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SCP939_LAYER, SCP939Model::createBodyLayer);
        event.registerLayerDefinition(CHICKEN_LAYER, ChickenModel::createBodyLayer);
    }
}
