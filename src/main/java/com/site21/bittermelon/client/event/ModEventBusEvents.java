package com.site21.bittermelon.client.event;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.entities.chicken.client.ChickenModel;
import com.site21.bittermelon.common.content.entities.scp131.client.SCP131Model;
import com.site21.bittermelon.common.content.entities.scp1507.client.SCP1507Model;
import com.site21.bittermelon.common.content.entities.scp650.client.SCP650Model;
import com.site21.bittermelon.common.content.entities.scp939.client.SCP939Model;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.common.content.entities.client.ModelLayers.CHICKEN_LAYER;
import static com.site21.bittermelon.common.content.entities.client.ModelLayers.SCP939_LAYER;
import static com.site21.bittermelon.common.content.entities.scp131.client.SCP131Model.SCP131_LAYER;
import static com.site21.bittermelon.common.content.entities.scp1507.client.SCP1507Renderer.SCP1507_LAYER;
import static com.site21.bittermelon.common.content.entities.scp650.client.SCP650Model.SCP650_LAYER;

@EventBusSubscriber(modid = Bittermelon.MOD_ID)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.@NotNull RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SCP939_LAYER, SCP939Model::createBodyLayer);
        event.registerLayerDefinition(CHICKEN_LAYER, ChickenModel::createBodyLayer);
        event.registerLayerDefinition(SCP650_LAYER, SCP650Model::createBodyLayer);
        event.registerLayerDefinition(SCP131_LAYER, SCP131Model::createBodyLayer);
        event.registerLayerDefinition(SCP1507_LAYER, SCP1507Model::createBodyLayer);
    }
}
