package com.site21.bittermelon.client.event;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.entities.chicken.client.ChickenModel;
import com.site21.bittermelon.common.content.entities.scp131.client.SCP131Model;
import com.site21.bittermelon.common.content.entities.scp1507.client.SCP1507Model;
import com.site21.bittermelon.common.content.entities.scp650.client.SCP650Model;
import com.site21.bittermelon.common.content.entities.scp939.client.SCP939Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = Bittermelon.MOD_ID)
public class LayerDefinitions {
    public static final ModelLayerLocation SCP_131_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "scp_131"),
            "main"
    );

    public static final ModelLayerLocation SCP_650_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "scp_650"),
            "main"
    );

    public static final ModelLayerLocation SCP_1507_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "scp_1507"),
            "main"
    );

    public static final ModelLayerLocation SCP_939_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "scp_939"),
            "main"
    );

    public static final ModelLayerLocation CHICKEN_LAYER = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "chicken"),
            "main"
    );

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.@NotNull RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SCP_131_LAYER, SCP131Model::createBodyLayer);
        event.registerLayerDefinition(SCP_650_LAYER, SCP650Model::createBodyLayer);
        event.registerLayerDefinition(SCP_1507_LAYER, SCP1507Model::createBodyLayer);
        event.registerLayerDefinition(SCP_939_LAYER, SCP939Model::createBodyLayer);
        event.registerLayerDefinition(CHICKEN_LAYER, ChickenModel::createBodyLayer);
    }
}
