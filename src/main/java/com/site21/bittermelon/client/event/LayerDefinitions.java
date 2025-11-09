package com.site21.bittermelon.client.event;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.entities.implementations.chicken.client.ChickenModel;
import com.site21.bittermelon.common.content.entities.implementations.scp131.client.SCP131Model;
import com.site21.bittermelon.common.content.entities.implementations.scp1507.client.SCP1507Model;
import com.site21.bittermelon.common.content.entities.implementations.scp650.client.SCP650Model;
import com.site21.bittermelon.common.content.entities.implementations.scp939.client.SCP939Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.common.content.entities.implementations.scp131.client.SCP131Model.SCP131_LAYER;
import static com.site21.bittermelon.common.content.entities.implementations.scp1507.client.SCP1507Renderer.SCP1507_LAYER;
import static com.site21.bittermelon.common.content.entities.implementations.scp650.client.SCP650Model.SCP650_LAYER;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class LayerDefinitions {
    public static final ModelLayerLocation SCP939_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "scp939_layer"), "main");

    public static final ModelLayerLocation CHICKEN_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "chicken_layer"), "main");

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.@NotNull RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SCP939_LAYER, SCP939Model::createBodyLayer);
        event.registerLayerDefinition(CHICKEN_LAYER, ChickenModel::createBodyLayer);
        event.registerLayerDefinition(SCP650_LAYER, SCP650Model::createBodyLayer);
        event.registerLayerDefinition(SCP131_LAYER, SCP131Model::createBodyLayer);
        event.registerLayerDefinition(SCP1507_LAYER, SCP1507Model::createBodyLayer);
    }
}
