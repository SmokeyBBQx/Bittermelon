package com.site21.bittermelon.events;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.entities.implementations.chicken.client.ChickenModel;
import com.site21.bittermelon.content.entities.implementations.scp650.client.SCP650Model;
import com.site21.bittermelon.content.entities.implementations.scp939.client.SCP939Model;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.content.entities.client.ModelLayers.CHICKEN_LAYER;
import static com.site21.bittermelon.content.entities.client.ModelLayers.SCP939_LAYER;
import static com.site21.bittermelon.content.entities.implementations.scp650.client.SCP650Model.SCP650_LAYER;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.@NotNull RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SCP939_LAYER, SCP939Model::createBodyLayer);
        event.registerLayerDefinition(CHICKEN_LAYER, ChickenModel::createBodyLayer);
        event.registerLayerDefinition(SCP650_LAYER, SCP650Model::createBodyLayer);
    }
}
